package com.travel.community.pages.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardEntity;
import com.travel.community.pages.board.enums.BoardStatus;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    BoardEntity findByUserIdAndStatus(String userId, BoardStatus draft);

    Optional<BoardEntity> findByIdAndUserId(Long id, String userId);

}
