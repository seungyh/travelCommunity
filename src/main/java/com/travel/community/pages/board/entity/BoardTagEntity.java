package com.travel.community.pages.board.entity;

import java.util.ArrayList;
import java.util.List;

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
 * 게시글 해시 태그 entity
 * BoardTagEntity
 */
@Entity
@Table(name = "board_tags")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BoardTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Long boardId; // 게시글 id

    @Column(nullable = false, length = 30)
    private String tag; // 태그 값

    public void setTag(String tag) {
        this.tag = tag;
    }

    // tags 값 리스트를 tag entity 리스트로 반환
    public static List<BoardTagEntity> toEntity(List<String> tags, Long boardId) {
        List<BoardTagEntity> tagEntityList = new ArrayList<>();
        for (String tag : tags) {
            tagEntityList.add(BoardTagEntity.builder().boardId(boardId).tag(tag).build());
        }
        return tagEntityList;
    }

    // entity list를 tag 만 뽑아서 list 로 반환
    public static List<String> toList(List<BoardTagEntity> tagList) {
        return tagList.stream().map(entity -> entity.getTag()).toList();
    }
}
