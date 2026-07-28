package com.travel.community.pages.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.account.entity.UserEntity;

public interface AccountRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserId(String userId);

}
