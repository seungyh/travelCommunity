package com.travel.community.pages.account.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.global.utils.FileUtil;
import com.travel.community.global.utils.RandomUtil;
import com.travel.community.pages.account.dto.FollowInfo;
import com.travel.community.pages.account.dto.request.FollowListRequest;
import com.travel.community.pages.account.dto.request.LoginRequest;
import com.travel.community.pages.account.dto.request.ProfileSaveRequest;
import com.travel.community.pages.account.dto.request.SignUpRequest;
import com.travel.community.pages.account.dto.response.FollowListResponse;
import com.travel.community.pages.account.dto.response.LoginResponse;
import com.travel.community.pages.account.dto.response.MyPageResponse;
import com.travel.community.pages.account.dto.response.ProfileResponse;
import com.travel.community.pages.account.entity.ProfileEntity;
import com.travel.community.pages.account.entity.UserEntity;
import com.travel.community.pages.account.enums.FollowTabType;
import com.travel.community.pages.account.enums.ProfileImgType;
import com.travel.community.pages.account.enums.Roles;
import com.travel.community.pages.account.enums.UserResponseMsg;
import com.travel.community.pages.account.exception.enums.AccountErrorCode;
import com.travel.community.pages.account.repository.AccountRepository;
import com.travel.community.pages.account.repository.ProfileRepository;
import com.travel.community.pages.account.repository.mapper.MyPageMapper;
import com.travel.community.pages.board.repository.FollowRepository;
import com.travel.community.pages.common.dto.response.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    @Value("${file.images.base-path}")
    private String BASE_PATH; // BASE 경로

    @Value("${file.images.profile}")
    private String PROFILE; // 프로필 이미지 저장 경로

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    private final MyPageMapper myPageMapper;
    private final JwtManager jwtManager;

    public CommonResponse signUp(SignUpRequest request) {

        // 중복 ID 확인
        accountRepository.findByUserId(request.getUserId())
                .ifPresent(i -> {
                    throw new BusinessException(AccountErrorCode.EXIST_ACCOUNT);
                });

        log.info("회원가입 user id: {}", request.getUserId());
        String encryptPassword = passwordEncoder.encode(request.getPassword());

        String inviteCode = RandomUtil.createInviteCode();

        UserEntity user = UserEntity.builder().userId(request.getUserId()).password(encryptPassword)
                .nickName(request.getNickName()).email(request.getEmail()).role(Roles.USER).inviteCode(inviteCode)
                .build();

        accountRepository.save(user);

        return CommonResponse.builder().result(true).message(UserResponseMsg.SIGN_UP_SUCCESS.getMessage()).build();
    }

    /**
     * 로그인 비즈니스 로직 처리(jwt 토큰 생성)
     * 
     * @param loginInfo
     * @param response
     * @return
     */
    public LoginResponse login(LoginRequest loginInfo, HttpServletResponse response) {
        UserEntity user = accountRepository.findByUserId(loginInfo.getUserId())
                .orElseThrow(() -> new BusinessException(AccountErrorCode.INVALID_ACCOUNT));
        boolean isPwMatch = passwordEncoder.matches(loginInfo.getPassword(), user.getPassword());

        if (!isPwMatch) {
            throw new BusinessException(AccountErrorCode.INVALID_ACCOUNT);
        }
        String token = jwtManager.createJwt(user.getUserId());
        jwtManager.addCookie(response, token);

        ProfileEntity profile = profileRepository.findByUserIdAndFileType(user.getUserId(),
                ProfileImgType.PROFILE);

        return LoginResponse.builder().email(user.getEmail()).nickName(user.getNickName()).userId(user.getUserId())
                .profileImagePath(profile != null ? profile.getPath() : null)
                .build();
    }

    public CommonResponse logout(HttpServletResponse response) {
        jwtManager.getCookieToDelete(response);
        return CommonResponse.builder().result(true).message(UserResponseMsg.LOGOUT_SUCCESS.getMessage()).build();
    }

    public LoginResponse tokenLogin(HttpServletRequest request, HttpServletResponse response) {
        String userId = null;
        try {
            String jwt = jwtManager.getJwt(request);
            userId = jwtManager.getUserId(jwt);
        } catch (BusinessException e) {
            jwtManager.getCookieToDelete(response);
            return null;
        }
        // Users 테이블 조회
        UserEntity userEntity = accountRepository.findByUserId(userId).get();
        String email = userEntity.getEmail();
        String nickName = userEntity.getNickName();
        ProfileEntity profile = profileRepository.findByUserIdAndFileType(userEntity.getUserId(),
                ProfileImgType.PROFILE);
        return LoginResponse.builder().userId(userId).email(email).nickName(nickName)
                .profileImagePath(profile != null ? profile.getPath() : "").build();

    }

    public MyPageResponse getMyInfo(HttpServletRequest request, String userId) {
        return myPageMapper.getMyPageInfo(userId);
    }

    public ProfileResponse getProfile(HttpServletRequest request) {
        String userId = jwtManager.getUserId(request);

        UserEntity user = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NOT_EXIST_ACCOUNT));
        return ProfileResponse.builder().bio(user.getBio()).nickName(user.getNickName()).build();
    }

    @Transactional
    public void saveProfile(HttpServletRequest request, ProfileSaveRequest saveInfo) {
        String userId = jwtManager.getUserId(request);
        UserEntity user = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NOT_EXIST_ACCOUNT));
        user.updateProfileInfo(saveInfo.getNickName(), saveInfo.getBio());
    }

    @Transactional
    public String reCreateInviteCode(HttpServletRequest request) {
        String userId = jwtManager.getUserId(request);

        String newInviteCode = RandomUtil.createInviteCode();

        UserEntity user = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(AccountErrorCode.NOT_EXIST_ACCOUNT));
        user.setInviteCode(newInviteCode);
        return newInviteCode;
    }

    @Transactional
    public void saveProfile(HttpServletRequest request, MultipartFile file, ProfileImgType type) {
        String userId = jwtManager.getUserId(request);
        String saveFileName = UUID.randomUUID().toString();
        String originName = file.getOriginalFilename();
        String fileExt = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String path = PROFILE + FileUtil.getDatePath() + saveFileName + "."
                + fileExt;

        // 파일 저장
        FileUtil.saveMultipartFile(BASE_PATH + path, file);

        // 프로필 경로 변경
        ProfileEntity profile = profileRepository.findByUserIdAndFileType(userId, type);
        if (profile != null) {
            profile.setPath(path);
            profile.setOriginName(originName);
            profile.setFileType(type);
        } else {
            profile = ProfileEntity.create(originName, path, userId, type);
            profileRepository.save(profile);
        }

    }

    public FollowListResponse getFollowList(HttpServletRequest request, FollowListRequest info) {
        List<FollowInfo> followList = myPageMapper.getFollowList(info);
        int followCount = 0;
        if (FollowTabType.FOLLOWER.equals(info.getTabType())) {
            followCount = followRepository.countByFollowingId(info.getUserId());
        } else {
            followCount = followRepository.countByFollowerId(info.getUserId());

        }
        return FollowListResponse.builder().followList(followList).totalCount(followCount).build();
    }

    @Transactional
    public void deleteFollower(HttpServletRequest request, String followerId) {
        String followingId = jwtManager.getUserId(request);
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }

}
