package com.travel.community.pages.board.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.global.utils.FileUtil;
import com.travel.community.pages.board.dto.request.BoardWriteRequest;
import com.travel.community.pages.board.entity.BoardEntity;
import com.travel.community.pages.board.entity.BoardImageEntity;
import com.travel.community.pages.board.entity.BoardTagEntity;
import com.travel.community.pages.board.enums.BoardFileType;
import com.travel.community.pages.board.repository.BoardImageRepository;
import com.travel.community.pages.board.repository.BoardRepository;
import com.travel.community.pages.board.repository.BoardTagRepository;
import com.travel.community.pages.common.dto.response.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Value("${file.images.base-path}")
    private String BASE_PATH; // BASE 경로
    @Value("${file.images.feature}")
    private String FEATURE_PATH; // 대표 이미지 저장 경로
    @Value("${file.images.extra}")
    private String EXTRA_PATH; // 추가 이미지 저장 경로

    private final BoardRepository boardRepository;
    private final BoardTagRepository boardTagRepository;
    private final BoardImageRepository boardImageRepository;
    private final JwtManager jwtManager;

    @Transactional
    public CommonResponse write(HttpServletRequest request, BoardWriteRequest boardInfo, MultipartFile featureImg,
            List<MultipartFile> extraImgs) {
        String userId = jwtManager.getUserId(jwtManager.getJwt(request));

        BoardEntity boardEntity = BoardEntity.builder().title(boardInfo.getTitle()).content(boardInfo.getContent())
                .userId(userId)
                .place(boardInfo.getPlace()).travelStartAt(boardInfo.getTravelStartAt())
                .travelEndAt(boardInfo.getTravelEndAt()).visibility(boardInfo.getVisibility())
                .category(boardInfo.getCategory()).build();

        boardRepository.save(boardEntity);

        // 태그 저장
        List<BoardTagEntity> tagEntityList = BoardTagEntity.toEntity(boardInfo.getTags(), boardEntity.getId());
        boardTagRepository.saveAll(tagEntityList);

        // 대표 이미지 파일 저장
        String saveFeatureFileName = UUID.randomUUID().toString();
        String featureImgExt = featureImg.getOriginalFilename()
                .substring(featureImg.getOriginalFilename().lastIndexOf(".") + 1);
        String saveFeaturePath = BASE_PATH + FEATURE_PATH + File.separator + saveFeatureFileName + "." + featureImgExt;
        FileUtil.saveMultipartFile(saveFeaturePath, featureImg);

        boardImageRepository.save(
                BoardImageEntity.create(featureImg.getOriginalFilename(), saveFeaturePath, boardEntity.getId(),
                        BoardFileType.FEATURE));

        // 추가 이미지 저장
        if (extraImgs != null && !extraImgs.isEmpty()) {
            List<BoardImageEntity> extraImageEntityList = new ArrayList<>();
            for (MultipartFile img : extraImgs) {
                String saveExtraFileName = UUID.randomUUID().toString();
                String extraImgExt = img.getOriginalFilename()
                        .substring(img.getOriginalFilename().lastIndexOf(".") + 1);
                String saveExtraPath = BASE_PATH + EXTRA_PATH + File.separator + saveExtraFileName + "." + extraImgExt;
                FileUtil.saveMultipartFile(saveExtraPath, img);

                extraImageEntityList.add(BoardImageEntity.create(saveExtraFileName, saveExtraPath, boardEntity.getId(),
                        BoardFileType.EXTRA));
            }
            boardImageRepository.saveAll(extraImageEntityList);

        }

        return CommonResponse.builder().result(true).message("게시글 등록을 성공하였습니다.").build();
    }

}
