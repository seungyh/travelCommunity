package com.travel.community.pages.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByFollowerIdAndFollowingId(String userId, String userId2);

}
