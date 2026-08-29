package com.travel.community.pages.board.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.travel.community.pages.board.enums.BoardFileType;
import com.travel.community.pages.common.entity.CommonFileEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 이미지
 * BoardImageEntity
 */
@Entity
@Table(name = "board_files")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class BoardImageEntity extends CommonFileEntity {

    @Column(name = "board_id", nullable = false)
    private Long boardId; // board 테이블 id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardFileType type; // 이미지 타입

    public static BoardImageEntity create(String originalName, String path, Long boardId, String fileName,
            BoardFileType type) {
        BoardImageEntity file = new BoardImageEntity();

        file.originalName = originalName;
        file.path = path;
        file.boardId = boardId;
        file.type = type;
        file.fileName = fileName;

        return file;
    }

}
