package com.travel.community.pages.alarm.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.travel.community.pages.alarm.enmus.AlarmType;
import com.travel.community.pages.alarm.entity.AlarmDocument;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmInfo {

    private String id; // object id
    private String nickName; // 닉네임
    private String description; // 채팅 또는 계획 제목
    private Instant createdAt; // 생성일
    private AlarmType alarmType; // 알람 타입
    private String senderId; // 보낸 사람 userId
    private boolean read; // 읽음 여부
    private Long planId; // 계획 id - alarmType이 PLAN일 때만

    public static List<AlarmInfo> from(List<AlarmDocument> docs) {
        List<AlarmInfo> resList = new ArrayList<>();

        for (AlarmDocument doc : docs) {
            resList.add(AlarmInfo.builder().id(doc.getId().toHexString()).nickName(doc.getSenderNickName())
                    .description(doc.getDescription())
                    .createdAt(doc.getCreatedAt())
                    .alarmType(doc.getAlarmType())
                    .senderId(doc.getSenderId())
                    .planId(doc.getPlanId())
                    .read(doc.isRead())
                    .build());
        }
        return resList;
    }

    public static AlarmInfo from(AlarmDocument doc) {

        return AlarmInfo.builder().id(doc.getId().toHexString()).nickName(doc.getSenderNickName())
                .description(doc.getDescription())
                .createdAt(doc.getCreatedAt())
                .alarmType(doc.getAlarmType())
                .senderId(doc.getSenderId())
                .planId(doc.getPlanId())
                .read(doc.isRead())
                .build();
    }
}
