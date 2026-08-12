package com.travel.community.pages.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardTagEntity;

public interface BoardTagRepository extends JpaRepository<BoardTagEntity, Long> {

}
