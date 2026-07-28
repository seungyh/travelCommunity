package com.travel.community.pages.oauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.oauth.entity.OAuthEntity;

public interface OAuthRepository extends JpaRepository<OAuthEntity, Long> {

    OAuthEntity findByProviderIdAndProvider(String providerId, String provider);

    OAuthEntity findById(String userId);

}
