package com.travel.community.pages.board.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.community.pages.board.dto.BoardCommentInfo;

@Mapper
public interface BoardCommentMapper {

    /**
     * 게시글의 댓글 조회
     * 
     * @param boardId
     * @return
     */
    List<BoardCommentInfo> getComments(Long boardId);
}
