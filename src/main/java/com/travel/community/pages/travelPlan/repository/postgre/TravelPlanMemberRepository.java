package com.travel.community.pages.travelPlan.repository.postgre;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanMemberEntity;

public interface TravelPlanMemberRepository extends JpaRepository<TravelPlanMemberEntity, Long> {

    boolean existsByPlanIdAndUserId(Long valueOf, String userId);

    int countByPlanId(Long planId);

    Optional<TravelPlanMemberEntity> findByUserId(String userId);

    boolean existsByUserIdAndPlanId(String userId, Long planId);

}
