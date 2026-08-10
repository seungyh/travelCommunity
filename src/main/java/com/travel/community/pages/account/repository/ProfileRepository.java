package com.travel.community.pages.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.account.entity.ProfileEntity;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    ProfileEntity findByUserId(String userId);

}
