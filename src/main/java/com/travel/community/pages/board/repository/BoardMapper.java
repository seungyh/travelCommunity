package com.travel.community.pages.board.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.travel.community.pages.board.dto.BoardFileAccessInfo;
import com.travel.community.pages.board.dto.BoardInfo;
import com.travel.community.pages.board.dto.BoardSearchFilter;
import com.travel.community.pages.board.dto.response.BoardDraftResponse;
import com.travel.community.pages.common.dto.request.SearchRequest;

@Mapper
public interface BoardMapper {

	/**
	 * 게시글 목록 조회
	 * 
	 * @param request
	 * @return
	 */
	List<BoardInfo> searchBoard(
			@Param("searchFilter") SearchRequest<BoardSearchFilter> searchFilter,
			@Param("userId") String userId);

	/**
	 * 게시글 총 건수 조회
	 * 
	 * @param request
	 * @return
	 */
	int searchBoardTotal(
			@Param("searchFilter") SearchRequest<BoardSearchFilter> searchFilter,
			@Param("userId") String userId);

	/**
	 * 파일 접근 권한을 체크하기 위한 정보 조회
	 * 
	 * @param userId
	 * @param fileId
	 * @return
	 */
	BoardFileAccessInfo selectImgAccessInfo(@Param("fileId") Long fileId);

	/**
	 * 해당 user의 임시 저장 데이터 조회
	 * 
	 * @param userId
	 * @return
	 */
	BoardDraftResponse selectDraftByUserId(String userId);
}
