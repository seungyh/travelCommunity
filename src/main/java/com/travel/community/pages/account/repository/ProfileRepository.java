package com.travel.community.pages.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.account.entity.ProfileEntity;
import com.travel.community.pages.account.enums.ProfileImgType;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    ProfileEntity findByUserIdAndFileType(String userId, ProfileImgType profile);

}
