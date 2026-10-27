package com.travel.community.sse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.sse.sseManager.SseEmitterManager;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmSseService {

    private final SseEmitterManager sseEmitterManager;
    private final JwtManager jwtManager;

    public SseEmitter subscribe(HttpServletRequest request) {
        String userId = jwtManager.getUserId(request);
        log.debug("[SSE] subscribe userId: {}", userId);
        return sseEmitterManager.subscribe(userId);
    }
}
