package com.travel.community.pages.board.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.travel.community.pages.board.dto.BoardSearchFilter;
import com.travel.community.pages.board.dto.request.BoardWriteRequest;
import com.travel.community.pages.board.dto.response.BoardDraftResponse;
import com.travel.community.pages.board.dto.response.BoardLikeResponse;
import com.travel.community.pages.board.dto.response.BoardSearchResponse;
import com.travel.community.pages.board.enums.BoardFileType;
import com.travel.community.pages.board.service.BoardService;
import com.travel.community.pages.common.dto.request.SearchRequest;
import com.travel.community.pages.common.dto.response.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/web/api/board")
@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 등록
     * 이미지는 임시 저장으로 이미 등록되어 있으므로 필요 없음
     * 
     * @param request
     * @param boardInfo
     * @return
     */
    @PostMapping("/write")
    public ResponseEntity<CommonResponse> boardWrite(
            HttpServletRequest request, @Valid @RequestBody BoardWriteRequest boardData) {
        boardService.save(request, boardData);
        return ResponseEntity.noContent().build();

    }

    /**
     * 게시글 조회
     * 
     * @param request
     * @return
     */
    @PostMapping("/search")
    public ResponseEntity<BoardSearchResponse> boardSearch(HttpServletRequest request,
            @RequestBody SearchRequest<BoardSearchFilter> filterInfo) {
        return ResponseEntity.ok(boardService.search(request, filterInfo));

    }

    /**
     * 게시글 목록 조회시 대표 이미지 Resource로 반환
     * 
     * @param path
     * @return
     */
    @GetMapping("/feature/{boardId}")
    public ResponseEntity<Resource> getFeatureImgResource(HttpServletRequest request, @PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getFeatureImgResource(request, boardId));
    }

    /**
     * 게시글 추가 이미지 또는 content 이미지 Resource로 반환
     * 
     * @param request
     * @param boardId
     * @return
     */
    @GetMapping("/file/{fileId}")
    public ResponseEntity<Resource> getImgResource(HttpServletRequest request, @PathVariable Long fileId) {
        return ResponseEntity.ok(boardService.getImgResource(request, fileId));
    }

    /**
     * 파일 삭제
     * 
     * @param request
     * @return
     */
    @DeleteMapping("/file/{fileId}")
    public ResponseEntity<Void> deleteImage(HttpServletRequest request, @PathVariable Long fileId) {
        boardService.deleteImage(request, fileId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 게시글 임시 저장
     * 임시 저장시에는 파라미터 유효성 검사 안함
     * 
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/draft")
    public ResponseEntity<String> saveDraft(HttpServletRequest request,
            @RequestBody BoardWriteRequest boardData) {

        return ResponseEntity.ok(boardService.save(request, boardData).toString());
    }

    /**
     * 임시 저장 데이터 조회
     * 
     * @param request
     * @return
     */
    @GetMapping("/draft")
    public ResponseEntity<BoardDraftResponse> getDraft(HttpServletRequest request) {
        return ResponseEntity.ok(boardService.getDraft(request));
    }

    /**
     * 파일을 저장하고 파일 id 반환
     * 
     * @param request
     * @param file
     * @param type
     * @return
     */
    @PostMapping("/file/upload")
    public ResponseEntity<Long> fileUpload(HttpServletRequest request,
            @RequestPart(value = "file", required = true) MultipartFile file,
            @RequestPart("type") BoardFileType type) {
        return ResponseEntity.ok(boardService.fileUpload(request, file, type));
    }

    /**
     * 게시글 좋아요
     * 
     * @param boardId
     * @return
     */
    @PatchMapping("/like/{boardId}")
    public ResponseEntity<BoardLikeResponse> updateBoardLike(HttpServletRequest request, @PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.updateBoardLike(request, boardId));
    }
}