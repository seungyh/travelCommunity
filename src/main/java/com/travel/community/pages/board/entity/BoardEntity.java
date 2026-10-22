package com.travel.community.pages.board.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.travel.community.pages.board.enums.BoardStatus;
import com.travel.community.pages.board.enums.BoardVisibilityType;

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
 * 게시글 entity
 * BoardEntity
 */
@Entity
@Table(name = "board")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title; // 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 내용 text 만

    @Column(columnDefinition = "TEXT")
    private String contentHtml; // 내용 html 전체

    @Builder.Default
    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0; // 좋아요 수

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt; // 생성일 날짜 + 시간

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt; // 수정일 날짜 + 시간

    @Column(length = 100)
    private String place; // 여행 장소

    @Column(name = "travel_start_at")
    private LocalDate travelStartAt; // 여행 시작일

    @Column(name = "travel_end_at")
    private LocalDate travelEndAt; // 여행 마지막일

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false, length = 20)
    private BoardStatus status = BoardStatus.PUBLISHED; // 게시글 상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BoardVisibilityType visibility; // 공개 범위

    // @Builder.Default
    @Column(name = "view_count", nullable = false)
    private int viewCount; // 조회수

    // @Builder.Default
    @Column(name = "shared_count", nullable = false)
    private int sharedCount; // 공유수

    @Column(name = "user_id", nullable = false)
    private String userId; // 작성자 ID

    @Column(nullable = false)
    private String category; // 카테고리

    @Column(name = "comment_count", nullable = false)
    private int commentCount; // 댓글 수

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    // Dirty Checking을 통한 게시글 정보 변경
    public void changeValue(String title, String content, String contentHtml, String place, LocalDate travelStartAt,
            LocalDate travelEndAt, BoardStatus status, BoardVisibilityType visibility, String category,
            OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.contentHtml = contentHtml;
        this.place = place;
        this.travelStartAt = travelStartAt;
        this.travelEndAt = travelEndAt;
        this.status = status;
        this.visibility = visibility;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
