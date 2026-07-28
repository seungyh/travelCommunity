package com.travel.community.pages.account.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.travel.community.pages.account.enums.NickNamePart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 닉네임 생성 정보 entity
 * NickNameWordEntity
 */
@Entity
@Table(name = "nickname_word")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class NickNameWordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // enum으로 받기 위해
    @Column(name = "part", nullable = false)
    private NickNamePart part; // 닉네임 순서

    @Column(nullable = false)
    private String word; // 닉네임 단어

    @Column(name = "is_del")
    private Boolean isDel; // 삭제 여부

}
