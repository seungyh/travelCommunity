package com.travel.community.pages.account.entity;

import java.time.LocalDateTime;
import java.util.Date;

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
 * 사용자 정보 entity
 * UserEntity
 */
@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // 사용자 id

    @Column(nullable = false)
    private String password; // 사용자 pw

    @Column(name = "email_valid")
    private boolean emailValid; // 사용자 이메일 인증 여부

    @Column(columnDefinition = "TEXT")
    private String bio; // 사용자 자기소개

    @Column(name = "nickname", nullable = false)
    private String nickName; // 닉네임

    @Column(nullable = false, unique = true)
    private String email; // 사용자 이메일

    private String phone; // 사용자 전화번호

    private String role; // 사용자 권한

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 회원가입일

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 자기 정보 변경일
}
