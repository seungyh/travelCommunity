package com.travel.community.pages.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

}
