package com.travel.community.pages.travelPlan.entity.postgre;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.travel.community.pages.travelPlan.enums.TravelPlanStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "travel_plans")
public class TravelPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String title; // 제목

    @Column(name = "start_at")
    private LocalDate startAt; // 시작일

    @Column(name = "end_at")
    private LocalDate endAt; // 종료일

    @Column(length = 30)
    private String place; // 여행 장소

    @Column(length = 200)
    private String memo; // 메모

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TravelPlanStatus status; // 상태 - 완료, 계획중

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt; // 생성일

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId; // 생성자

    public void updateTravelInfo(String title, LocalDate startAt, LocalDate endAt, String place, String memo) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.place = place;
        this.memo = memo;
    }
}
