package com.travel.community.pages.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardImageEntity;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Long> {

}
