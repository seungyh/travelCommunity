package com.travel.community.pages.travelPlan.repository.postgre;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanEntity;

public interface TravelPlanRepository extends JpaRepository<TravelPlanEntity, Long> {

    boolean existsByIdAndUserId(Long planId, String userId);

    Optional<TravelPlanEntity> findByUserIdAndId(String userId, Long planId);

}
