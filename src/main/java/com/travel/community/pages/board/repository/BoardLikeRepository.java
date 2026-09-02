package com.travel.community.pages.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardLikeEntity;

public interface BoardLikeRepository extends JpaRepository<BoardLikeEntity, Long> {

    BoardLikeEntity findByUserIdAndBoardId(String userId, Long boardId);

    void deleteByUserIdAndBoardId(String userId, Long boardId);

}
