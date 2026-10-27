package com.travel.community.pages.alarm.entity;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.travel.community.pages.alarm.enmus.AlarmType;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 헤더 벨 모양 알림 도큐먼트
 * AlarmDocument
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alarm")
public class AlarmDocument {

    @Id
    private ObjectId id;
    private AlarmType alarmType; // 알림 타입
    private String senderId; // 알림 보낸 사람 userId
    private String senderNickName; // 알림 보낸 사람 닉네임
    private String description; // 설명 - ex (채팅: 이번 주말에 여행가실래요?), (계획: 오사카 3박 4일 여행)
    private Instant createdAt; // 생성일
    private String receiverId; // 받는 사람 userId
    private boolean read; // 읽음 여부

    private Long planId; // alarmType이 PLAN일경우만

}
