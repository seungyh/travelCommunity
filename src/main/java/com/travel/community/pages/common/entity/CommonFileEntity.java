package com.travel.community.pages.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 entity 공통 필드 정의
 * 외부에서 직접 생성 불가하게 설정
 * entity 필드만 상속하여 사용 사능
 * CommonFileEntity
 */
@Getter
@MappedSuperclass // jpa 매핑 필드만 자식 entity에 상속
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 생성자 호출 막음
// abstract 하여 객체 생성 막음
public abstract class CommonFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "origin_name")
    protected String originalName; // 원본 파일명

    @Column(nullable = false)
    protected String path; // 저장 경로
}
