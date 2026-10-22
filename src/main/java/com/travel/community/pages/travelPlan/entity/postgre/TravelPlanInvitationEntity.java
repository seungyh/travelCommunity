package com.travel.community.pages.travelPlan.entity.postgre;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "travel_plan_invitation")
public class TravelPlanInvitationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt; // 생성일

    @Column(name = "plan_id", nullable = false)
    private Long planId; // 계획 id

    @Column(name = "invitee_id", nullable = false, length = 20)
    private String inviteeId; // 초대 받은 사용자 id

    @Column(name = "inviter_id", nullable = false, length = 20)
    private String inviterId; // 초대한 사용자 id
}
