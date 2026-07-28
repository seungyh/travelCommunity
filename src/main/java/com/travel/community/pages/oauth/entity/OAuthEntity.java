package com.travel.community.pages.oauth.entity;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * oauth 로그인 시 저장 entity
 * OAuthEntity
 */
@Getter
@Builder
@Entity
@Table(name = "oauth")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", nullable = false)
    private String providerId; // 제공 id

    @Column(nullable = false)
    private String provider; // 플랫폼

    @Column(name = "nickname", nullable = false)
    private String nickName; // 처음엔 임의로 설정

    @Column(nullable = false)
    private String email; // 이메일

    // @Column(name = "linked_at", updatable = false)
    // private LocalDateTime linkedAt; // 계정 연동일

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 등록일
}
