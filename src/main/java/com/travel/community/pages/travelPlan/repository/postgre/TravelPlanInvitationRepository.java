package com.travel.community.pages.travelPlan.repository.postgre;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.travelPlan.entity.postgre.TravelPlanInvitationEntity;

public interface TravelPlanInvitationRepository extends JpaRepository<TravelPlanInvitationEntity, Long> {

    Optional<TravelPlanInvitationEntity> findByInviteeId(String userId);

    Optional<TravelPlanInvitationEntity> findByInviteeIdAndPlanId(String userId, Long planId);

    void deleteByInviteeIdAndPlanId(String userId, Long planId);

    boolean existsByInviteeIdAndPlanId(String userId, Long planId);

}
