package com.travel.community.pages.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.travel.community.pages.board.dto.request.BoardWriteRequest;
import com.travel.community.pages.board.service.BoardService;
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
     * 
     * @param request
     * @param files
     * @return
     */
    @PostMapping("/write")
    public ResponseEntity<CommonResponse> boardWrite(
            HttpServletRequest request,
            @Valid @RequestPart("boardData") BoardWriteRequest boardInfo,
            @RequestPart(value = "featureImg", required = true) MultipartFile featureImg,
            @RequestPart(value = "extraImgs", required = false) List<MultipartFile> extraImgs) {

        return ResponseEntity.ok(boardService.write(request, boardInfo, featureImg, extraImgs));

    }
}
