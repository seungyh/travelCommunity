package com.travel.community.pages.account.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.travel.community.pages.account.enums.ProfileImgType;
import com.travel.community.pages.common.entity.CommonFileEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 프로필 이미지
 * ProfileEntity
 */
@Entity
@Table(name = "profile_files")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProfileEntity extends CommonFileEntity {

    @Column(name = "user_id", nullable = false)
    private String userId; // userId

    @Column(name = "file_type", nullable = false)
    private ProfileImgType fileType;

    public void setPath(String path) {
        this.path = path;
    }

    public void setOriginName(String name) {
        this.originalName = name;
    }

    public void setFileType(ProfileImgType fileType) {
        this.fileType = fileType;
    }

    public static ProfileEntity create(String originFileName, String path, String userId, ProfileImgType fileType) {
        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.originalName = originFileName;
        profileEntity.path = path;
        profileEntity.userId = userId;
        profileEntity.fileType = fileType;

        return profileEntity;
    }

}
