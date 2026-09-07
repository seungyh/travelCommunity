package com.travel.community.pages.board.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comment")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID

    @Column(name = "parent_id")
    private Long parentId; // 부모 댓글 ID

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 댓글 내용

    @Column(nullable = false)
    private Integer depth; // 댓글 깊이

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt; // 작성일

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId; // 작성자 ID

    @Column(name = "board_id", nullable = false)
    private Long boardId; // 게시글 ID

    @Column(name = "is_del", nullable = false)
    private boolean isDel; // 삭제 여부

    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }
}
