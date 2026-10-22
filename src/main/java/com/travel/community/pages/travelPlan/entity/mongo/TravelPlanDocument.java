package com.travel.community.pages.travelPlan.entity.mongo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.travel.community.pages.travelPlan.dto.PlanDetail;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "travel.plan")
public class TravelPlanDocument {

    // RDB 여행 계획 ID를 mongodb의 _id로 사용
    @Id
    private Long id;

    // 일차별 여행 계획
    @Builder.Default
    private List<PlanDetail> plans = new ArrayList<>();

    // 최종 저장일
    private LocalDateTime updatedAt;

    public void updatePlans(List<PlanDetail> plans) {
        this.plans = plans;
        this.updatedAt = LocalDateTime.now();
    }
}
