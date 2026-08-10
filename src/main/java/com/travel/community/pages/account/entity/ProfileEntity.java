package com.travel.community.pages.account.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.travel.community.pages.common.entity.CommonFileEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private Long userId; // users 테이블 id

    public void setPath(String path) {
        this.path = path;
    }

    public static ProfileEntity create(String originFileName, String path, Long userId) {
        ProfileEntity profileEntity = new ProfileEntity();

        profileEntity.originalName = originFileName;
        profileEntity.path = path;
        profileEntity.userId = userId;

        return profileEntity;
    }
}
