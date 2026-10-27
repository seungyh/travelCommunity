package com.travel.community.webSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.dto.SocketUser;
import com.travel.community.pages.account.entity.ProfileEntity;
import com.travel.community.pages.account.entity.UserEntity;
import com.travel.community.pages.account.enums.ProfileImgType;
import com.travel.community.pages.account.exception.enums.AccountErrorCode;
import com.travel.community.pages.account.repository.AccountRepository;
import com.travel.community.pages.account.repository.ProfileRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanMemberRepository;
import com.travel.community.pages.travelPlan.repository.postgre.TravelPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanWebSocketHandler extends BinaryWebSocketHandler {

    // 해당 여행 계획에 접속한 webSocket 세션들
    private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    // 현재 소켓 세션 사용자 정보
    private final Map<String, SocketUser> socketUsers = new ConcurrentHashMap<>();

    // planId -> Y.Doc 초기 동기화를 기다리는 sessionId Queue
    private final Map<String, Queue<String>> pendingSyncQueues = new ConcurrentHashMap<>();

    private final byte YJS_UPDATE = 0; // 스케줄 데이터 변경
    private final byte AWARENESS = 1; // 누가 어디를 수정/선택했는지
    private final byte PRESENCE = 2; // 접속/참여 사용자
    private final byte INIT = 3; // 기존 참가자가 다음 참가자에게 yDoc init 정보 전달

    private final TravelPlanRepository travelPlanRepository;
    private final TravelPlanMemberRepository travelPlanMemberRepository;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException, JsonProcessingException {

        String planId = getPlanId(session);
        String userId = (String) session.getAttributes().get("userId");

        boolean isMember = travelPlanMemberRepository.existsByPlanIdAndUserId(Long.valueOf(planId),
                userId);

        boolean isOwner = travelPlanRepository
                .existsByIdAndUserId(Long.valueOf(planId), userId);

        // 권한 없는 사용자
        if (!isMember && !isOwner) {
            session.close(CloseStatus.POLICY_VIOLATION);

            log.debug("[WEB SOCKET] No Exist Plan Permission UserId: {}", userId);
            return;
        }

        rooms.computeIfAbsent(planId, key -> ConcurrentHashMap.newKeySet()).add(session);
        log.info("[WEB SOCKET] plan id: " + planId + " 접속: " + session.getId());

        UserEntity user = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NOT_EXIST_ACCOUNT));
        ProfileEntity profile = profileRepository.findByUserIdAndFileType(userId, ProfileImgType.PROFILE);

        boolean syncProvider = false;
        if (socketUsers.size() == 0) { // 최초 진입자일때
            syncProvider = true;
        }
        // 접속자 데이터 전송
        socketUsers.put(
                session.getId(),
                new SocketUser(userId, user.getNickName(), profile != null ? profile.getPath() : "", syncProvider));

        // 같은 방 사용자들에게 접속자 변경 알림
        sendJoinUser(planId);

        // 기존 참가자가 있다면 yDoc을 받기 위해 pending queue에 담기
        Set<WebSocketSession> sessions = rooms.get(planId);
        if (!sessions.isEmpty()) {
            pendingSyncQueues.computeIfAbsent(planId, key -> new ConcurrentLinkedQueue<>()).offer(session.getId());
        }

    }

    private void sendJoinUser(String planId) throws IOException {
        Set<WebSocketSession> sessions = rooms.get(planId);

        if (sessions == null) {
            return;
        }

        List<SocketUser> users = sessions.stream()
                .map(session -> socketUsers.get(session.getId()))
                .filter(Objects::nonNull)
                .toList();

        byte[] json = objectMapper.writeValueAsBytes(users);

        // type 1byte + JSON
        byte[] message = new byte[json.length + 1];

        message[0] = PRESENCE;

        /**
         * System.arraycopy - src: 복사할 원본 배열srcPos: 원본 배열에서 복사를 시작할 인덱스dest: 붙여넣을 대상(새)
         * 배열destPos: 대상 배열에서 저장을 시작할 인덱스length: 복사할 요소의 개수
         */
        System.arraycopy(json, 0, message, 1, json.length); // json byte 배열을 message 배열에 복사

        // 같은 계획 수정하는 모든 사용자에게 message 전송
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(
                        new BinaryMessage(message));
            }
        }

    }

    @Override
    protected void handleBinaryMessage(
            WebSocketSession session,
            BinaryMessage message) throws Exception {

        String planId = getPlanId(session);
        log.debug("[WEB SOCKET] Handle Binary Message PlanId: {}", planId);

        Set<WebSocketSession> sessions = rooms.get(planId);

        if (session == null) {
            log.debug("[WEB SOCKET] No Exist Session PlanId: {}", planId);
            return;
        }

        ByteBuffer buffer = message.getPayload();
        if (buffer.get(0) == INIT) {
            Queue<String> queue = pendingSyncQueues.get(planId);
            while (!queue.isEmpty()) {
                String queueSessionId = queue.poll();
                // 해당 소켓 세션 찾기
                WebSocketSession socketSession = sessions.stream().filter(i -> i.getId().equals(queueSessionId))
                        .findFirst()
                        .orElse(null);
                if (socketSession != null && socketSession.isOpen()) {
                    socketSession.sendMessage(message);
                    log.debug("[WebSocket] Send Plan Init Message: {}", message);
                }

            }

        } else {
            // 같은 plan에 접속한 사용자에게만 전송
            for (WebSocketSession socketSession : sessions) {

                // 요청 보낸 사람은 제외
                if (socketSession.getId().equals(session.getId())) {
                    continue;
                }
                if (socketSession.isOpen()) {
                    socketSession.sendMessage(message);
                    log.debug("[WebSocket] Send Plan Message: {}", message);
                }
            }
        }

    }

    @Override
    public void afterConnectionClosed(
            WebSocketSession session,
            CloseStatus status) throws Exception {

        String planId = getPlanId(session);

        Set<WebSocketSession> sessions = rooms.get(planId);
        SocketUser closedUser = socketUsers.remove(session.getId());

        if (sessions == null) {
            return;
        }

        // 접속 종료 세션 제거
        sessions.remove(session);

        // 초기 동기화 대기 Queue에서도 제거
        Queue<String> queue = pendingSyncQueues.get(planId);
        if (queue != null) {
            queue.remove(session.getId());
        }

        // 나간 사용자가 syncProvider였다면 재지정
        if (closedUser != null && closedUser.isSyncProvider()) {

            WebSocketSession newProviderSession = sessions.stream()
                    .filter(WebSocketSession::isOpen)
                    .findFirst()
                    .orElse(null);

            if (newProviderSession != null) {
                SocketUser user = socketUsers.get(newProviderSession.getId());

                if (user != null) {
                    socketUsers.put(
                            newProviderSession.getId(),
                            new SocketUser(
                                    user.getUserId(),
                                    user.getNickName(),
                                    user.getProfilePath(),
                                    true));
                }
            }
        }

        // 아무도 없으면 room 관련 데이터 제거
        if (sessions.isEmpty()) {
            rooms.remove(planId);
            pendingSyncQueues.remove(planId);
        }

        // 변경된 접속자 목록 전송
        sendJoinUser(planId);
    }

    // planId만 추출하여 반환
    private String getPlanId(WebSocketSession session) {
        String path = session.getUri().getPath();

        // /ws/plan/2 → 2
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
