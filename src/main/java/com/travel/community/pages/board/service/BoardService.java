package com.travel.community.pages.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.global.utils.FileUtil;
import com.travel.community.pages.board.dto.BoardFileAccessInfo;
import com.travel.community.pages.board.dto.BoardInfo;
import com.travel.community.pages.board.dto.BoardSearchFilter;
import com.travel.community.pages.board.dto.request.BoardWriteRequest;
import com.travel.community.pages.board.dto.response.BoardDraftResponse;
import com.travel.community.pages.board.dto.response.BoardLikeResponse;
import com.travel.community.pages.board.dto.response.BoardSearchResponse;
import com.travel.community.pages.board.entity.BoardEntity;
import com.travel.community.pages.board.entity.BoardImageEntity;
import com.travel.community.pages.board.entity.BoardLikeEntity;
import com.travel.community.pages.board.entity.BoardTagEntity;
import com.travel.community.pages.board.enums.BoardFileType;
import com.travel.community.pages.board.enums.BoardStatus;
import com.travel.community.pages.board.enums.BoardVisibilityType;
import com.travel.community.pages.board.exception.enums.BoardErrorCode;
import com.travel.community.pages.board.repository.BoardImageRepository;
import com.travel.community.pages.board.repository.BoardLikeRepository;
import com.travel.community.pages.board.repository.BoardMapper;
import com.travel.community.pages.board.repository.BoardRepository;
import com.travel.community.pages.board.repository.BoardTagRepository;
import com.travel.community.pages.board.repository.FollowRepository;
import com.travel.community.pages.common.dto.request.SearchRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	@Value("${file.images.base-path}")
	private String BASE_PATH; // BASE 경로

	@Value("${file.images.board}")
	private String BOARD; // 게시글 이미지 저장 경로

	private final BoardRepository boardRepository;
	private final BoardTagRepository boardTagRepository;
	private final BoardImageRepository boardImageRepository;
	private final BoardLikeRepository boardLikeRepository;
	private final BoardMapper boardMapper;
	private final FollowRepository followRepository;
	private final JwtManager jwtManager;

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 임시 저장 또는 게시글 저장 후 게시글 id 반환
	 * 
	 * @param request
	 * @param boardInfo
	 * @return
	 */
	@Transactional
	public Long save(HttpServletRequest request, BoardWriteRequest boardInfo) {

		String userId = jwtManager.getUserId(request);
		// 임시 저장한 게시글 가져오기
		BoardEntity boardEntity = boardRepository.findByUserIdAndStatus(userId, BoardStatus.DRAFT);

		if (boardEntity == null) {
			return addBoard(request, boardInfo); // 임시저장된게 없으면 저장

		}
		// 임시 저장된게 있으므로 임시 저장 수정 또는 저장
		updateBoard(request, boardInfo, boardEntity);
		return boardEntity.getId();
	}

	public void updateBoard(HttpServletRequest request, BoardWriteRequest boardInfo,
			BoardEntity boardEntity) {

		// 기존에 이미 등록된 게시글이면 게시글 수정
		if (BoardStatus.PUBLISHED.equals(boardEntity.getStatus())) {
			// 게시글 수정
			boardEntity.changeValue(boardInfo.getTitle(), boardInfo.getContentValue().getContent(),
					boardInfo.getContentValue().getContentHtml(), boardInfo.getPlace(), boardInfo.getTravelStartAt(),
					boardInfo.getTravelEndAt(), boardInfo.getStatus(), boardInfo.getVisibility(),
					boardInfo.getCategory(),
					boardEntity.getCreatedAt(), LocalDateTime.now());
		} else {
			// 임시저장 수정
			boardEntity.changeValue(boardInfo.getTitle(), boardInfo.getContentValue().getContent(),
					boardInfo.getContentValue().getContentHtml(), boardInfo.getPlace(), boardInfo.getTravelStartAt(),
					boardInfo.getTravelEndAt(), boardInfo.getStatus(), boardInfo.getVisibility(),
					boardInfo.getCategory(),
					LocalDateTime.now(), null);
		}

		// 기존 태그를 전부 삭제 후 새로운 태그로 교체
		boardTagRepository.deleteByBoardId(boardEntity.getId());
		boardTagRepository.flush(); // 태그 삭제 flush
		boardTagRepository.saveAll(BoardTagEntity.toEntity(boardInfo.getTags(), boardEntity.getId()));

	}

	/**
	 * 게시글 등록
	 * 
	 * @param request
	 * @param boardInfo
	 * @return
	 */
	public Long addBoard(HttpServletRequest request, BoardWriteRequest boardInfo) {
		String userId = jwtManager.getUserId(jwtManager.getJwt(request));

		BoardEntity boardEntity = BoardEntity.builder().title(boardInfo.getTitle())
				.content(boardInfo.getContentValue().getContent())
				.contentHtml(boardInfo.getContentValue().getContentHtml())
				.userId(userId)
				.place(boardInfo.getPlace()).travelStartAt(boardInfo.getTravelStartAt())
				.travelEndAt(boardInfo.getTravelEndAt()).status(boardInfo.getStatus())
				.visibility(boardInfo.getVisibility())
				.category(boardInfo.getCategory()).build();

		boardRepository.save(boardEntity);

		// 태그 저장
		List<BoardTagEntity> tagEntityList = BoardTagEntity.toEntity(boardInfo.getTags(), boardEntity.getId());
		boardTagRepository.saveAll(tagEntityList);

		return boardEntity.getId();
	}

	/**
	 * 이미지 저장 후 파일 id 반환
	 * 
	 * @param file
	 * @param boardId
	 * @param type
	 * @return
	 */
	private Long saveImage(MultipartFile file, Long boardId, BoardFileType type) {

		String saveFileName = UUID.randomUUID().toString();
		String fileExt = file.getOriginalFilename()
				.substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		String saveExtraPath = BOARD + FileUtil.getDatePath() + saveFileName + "."
				+ fileExt;
		FileUtil.saveMultipartFile(BASE_PATH + saveExtraPath, file);

		BoardImageEntity entity = boardImageRepository.save(
				BoardImageEntity.create(saveFileName, saveExtraPath,
						boardId,
						saveFileName + "." + fileExt,
						type));

		return entity.getId();
	}

	/**
	 * 파일을 업로드하고 파일 id 반환
	 * 
	 * @param request
	 * @param file
	 * @param type
	 * @return
	 */
	public Long fileUpload(HttpServletRequest request, MultipartFile file, BoardFileType type) {

		String userId = jwtManager.getUserId(request);
		// 임시 저장한 게시글 가져오기
		BoardEntity boardEntity = boardRepository.findByUserIdAndStatus(userId, BoardStatus.DRAFT);
		Long boardId = null;

		// 임시 저장된게 없으면 게시글 빈 내용으로 임시 저장 데이터 생성
		if (boardEntity == null) {
			boardId = addBoard(request, BoardWriteRequest.builder().build());
		} else {
			boardId = boardEntity.getId();
		}
		// 대표 이미지 파일 저장 및 file id 반환
		return saveImage(file, boardId, type);

	}

	public BoardSearchResponse search(HttpServletRequest request, SearchRequest<BoardSearchFilter> filterInfo) {
		String userId = "";
		try {
			userId = jwtManager.getUserId(request);
		} catch (Exception e) {
		}

		List<BoardInfo> boardList = boardMapper.searchBoard(filterInfo, userId);
		int total = boardMapper.searchBoardTotal(filterInfo, userId);

		return BoardSearchResponse.builder().boardSearchInfo(boardList).totalCount(total).build();
	}

	@Transactional
	public BoardLikeResponse updateBoardLike(HttpServletRequest request, Long boardId) {
		String userId = jwtManager.getUserId(request);

		boolean likeStatus = false;
		BoardEntity board = boardRepository.findById(boardId)
				.orElseThrow(() -> new BusinessException(BoardErrorCode.NOT_EXIST_BOARD));

		BoardLikeEntity likeEntity = boardLikeRepository.findByUserIdAndBoardId(userId, boardId);

		if (likeEntity != null) { // 좋아요 했던 게시글
			board.setLikeCount(board.getLikeCount() - 1); // 좋아요 취소한 카운트
			boardLikeRepository.deleteByUserIdAndBoardId(userId, boardId); // 좋아요 정보 삭제
		} else { // 좋아요 안했던 게시글
			board.setLikeCount(board.getLikeCount() + 1);// 좋아요한 카운트
			boardLikeRepository.save(BoardLikeEntity.builder().boardId(boardId).userId(userId).build()); // 좋아요 정보 생성
			likeStatus = true;
		}

		return BoardLikeResponse.builder().likeCount(board.getLikeCount()).liked(likeStatus).build();
	}

	/**
	 * 대표 이미지 내려주기
	 */
	public Resource getFeatureImgResource(HttpServletRequest request, Long boardId) {
		String userId = "";
		try {
			userId = jwtManager.getUserId(request);
		} catch (Exception e) {
		}
		// 해당 게시글 공개 범위 확인
		BoardEntity boardEntity = boardRepository.findById(boardId)
				.orElseThrow(() -> new BusinessException(BoardErrorCode.NOT_EXIST_BOARD));

		// 자신의 게시글은 검증 없이 접근 가능
		if (!userId.equals(boardEntity.getUserId())) {
			// publish된 게시글 이미지만 접근 가능
			if (!BoardStatus.PUBLISHED.equals(boardEntity.getStatus())) {
				throw new BusinessException(BoardErrorCode.INVALID_ACCESS);
			}
			// 비공개 게시글 이미지는 접근 불가
			if (BoardVisibilityType.PRIVATE.equals(boardEntity.getVisibility())) {
				throw new BusinessException(BoardErrorCode.INVALID_ACCESS);
			}
			// 팔로우를 한 user id만 해당 게시글의 이미지 접근 가능
			if (BoardVisibilityType.FOLLOW.equals(boardEntity.getVisibility())) {
				followRepository.findByFollowerIdAndFollowingId(userId,
						boardEntity.getUserId())
						.orElseThrow(() -> new BusinessException(BoardErrorCode.INVALID_ACCESS));
			}
		}

		// 게시글의 대표 이미지 조회, 대표 이미지는 하나밖에 없으므로 0번째 추출
		List<BoardImageEntity> imgEntity = boardImageRepository.findByBoardIdAndType(boardEntity.getId(),
				BoardFileType.FEATURE);
		if (imgEntity.isEmpty()) {
			throw new BusinessException(BoardErrorCode.INVALID_ACCESS);
		}

		return FileUtil.getResource(BASE_PATH + imgEntity.get(0).getPath());
	}

	public Resource getImgResource(HttpServletRequest request, Long fileId) {
		String userId = "";
		try {
			userId = jwtManager.getUserId(request);
		} catch (Exception e) {
		}

		// 해당 게시글 공개 범위 확인을 위한 정보 조회
		BoardFileAccessInfo accessInfo = boardMapper.selectImgAccessInfo(fileId);

		// 자신의 게시글은 검증 없이 접근 가능
		if (!userId.equals(accessInfo.getUserId())) {

			if (!BoardStatus.PUBLISHED.equals(accessInfo.getStatus())) {
				throw new BusinessException(BoardErrorCode.INVALID_ACCESS);
			}
			// 비공개 게시글 이미지는 접근 불가
			if (BoardVisibilityType.PRIVATE.equals(accessInfo.getVisibility())) {
				throw new BusinessException(BoardErrorCode.INVALID_ACCESS);
			}
			// 팔로우를 한 user id만 해당 게시글의 이미지 접근 가능
			if (BoardVisibilityType.FOLLOW.equals(accessInfo.getVisibility())) {
				followRepository.findByFollowerIdAndFollowingId(userId,
						accessInfo.getUserId())
						.orElseThrow(() -> new BusinessException(BoardErrorCode.INVALID_ACCESS));
			}
		}

		return FileUtil.getResource(BASE_PATH + accessInfo.getPath());

	}

	public void deleteImage(HttpServletRequest request, Long fileId) {
		String userId = jwtManager.getUserId(request);

		// 해당 파일의 게시글 작성자 조회
		BoardFileAccessInfo accessInfo = boardMapper.selectImgAccessInfo(fileId);

		// 자신의 게시글 이미지만 삭제 가능
		if (userId.equals(accessInfo.getUserId())) {
			boardImageRepository.deleteById(fileId);
		}
	}

	public BoardDraftResponse getDraft(HttpServletRequest request) {
		String userId = jwtManager.getUserId(request);
		BoardDraftResponse draftData = boardMapper.selectDraftByUserId(userId); // 임시저장 게시글 조회
		List<BoardTagEntity> tagList = boardTagRepository.findByBoardId(draftData.getBoardId()); // 태그 조회

		// 추가 이미지 조회
		List<BoardImageEntity> extraImgEntityList = boardImageRepository.findByBoardIdAndType(draftData.getBoardId(),
				BoardFileType.EXTRA);
		List<Long> extraImgIds = extraImgEntityList.stream().map(entity -> entity.getId()).toList();
		draftData.setTags(BoardTagEntity.toList(tagList));
		draftData.setExtraImgIds(extraImgIds);

		return draftData;
	}

}
