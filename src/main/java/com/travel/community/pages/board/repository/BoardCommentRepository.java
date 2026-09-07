package com.travel.community.pages.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardCommentEntity;

public interface BoardCommentRepository extends JpaRepository<BoardCommentEntity, Long> {

    int countByBoardId(Long boardId);

    Optional<BoardCommentEntity> findByUserIdAndId(String userId, Long commentId);

}
