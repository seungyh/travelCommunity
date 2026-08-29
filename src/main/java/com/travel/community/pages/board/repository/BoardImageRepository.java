package com.travel.community.pages.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.board.entity.BoardImageEntity;
import com.travel.community.pages.board.enums.BoardFileType;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity, Long> {

    void deleteByBoardIdAndType(Long boardId, BoardFileType feature);

    List<BoardImageEntity> findByBoardIdAndType(Long id, BoardFileType feature);

}
