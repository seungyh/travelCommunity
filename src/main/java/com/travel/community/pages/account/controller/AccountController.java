package com.travel.community.pages.account.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.travel.community.global.utils.FileUtil;
import com.travel.community.pages.account.dto.request.FollowListRequest;
import com.travel.community.pages.account.dto.request.LoginRequest;
import com.travel.community.pages.account.dto.request.ProfileSaveRequest;
import com.travel.community.pages.account.dto.request.SignUpRequest;
import com.travel.community.pages.account.dto.response.FollowListResponse;
import com.travel.community.pages.account.dto.response.LoginResponse;
import com.travel.community.pages.account.dto.response.MyPageResponse;
import com.travel.community.pages.account.dto.response.ProfileResponse;
import com.travel.community.pages.account.enums.ProfileImgType;
import com.travel.community.pages.account.service.AccountService;
import com.travel.community.pages.account.service.NickNameCreator;
import com.travel.community.pages.common.dto.response.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/api/account")
public class AccountController {

    @Value("${file.images.base-path}")
    private String BASE_PATH; // BASE 경로

    private final AccountService service;
    private final NickNameCreator nickNameCreator;

    /**
     * 로그인
     * 
     * @param response
     * @param loginInfo
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response,
            @Valid @RequestBody LoginRequest loginInfo) {
        return ResponseEntity.ok(service.login(loginInfo, response));
    }

    /**
     * 회원가입
     * 
     * @param info
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody SignUpRequest info) {
        return ResponseEntity.ok(service.signUp(info));
    }

    /**
     * 쿠키를 만료시켜 로그아웃 처리
     * 
     * @param response
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletResponse response) {
        return ResponseEntity.ok(service.logout(response));
    }

    /**
     * jwt 토큰으로 로그인
     * 
     * @param request
     * @return
     */
    @GetMapping("/token/login")
    public ResponseEntity<LoginResponse> tokenLogin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ResponseEntity.ok(service.tokenLogin(request, response));

    }

    /**
     * 로그인 후 csrf 토큰 생성
     * 
     * @param csrfToken
     * @return
     */
    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }

    /**
     * 임의 닉네임 생성하여 반환
     */
    @GetMapping("/nickName")
    public ResponseEntity<String> getRandomNickName() {
        return ResponseEntity.ok(nickNameCreator.getNickName());
    }

    /**
     * profile 이미지 Resource로 반환
     * 
     * @param id
     * @return
     */
    @GetMapping("/profile/image")
    public ResponseEntity<Resource> getProfileImage(@RequestParam String path) {
        return ResponseEntity.ok(FileUtil.getResource(BASE_PATH + path));
    }

    /**
     * 마이페이지 사용자 정보 반환
     */
    @GetMapping("/my-info/{userId}")
    public ResponseEntity<MyPageResponse> getMyPageInfo(HttpServletRequest request, @PathVariable String userId) {
        return ResponseEntity.ok(service.getMyInfo(request, userId));
    }

    /**
     * 프로필 편집 모달 정보 조회
     * 
     * @param request
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(HttpServletRequest request) {
        return ResponseEntity.ok(service.getProfile(request));
    }

    /**
     * 프로필 저장
     * 
     * @param request
     * @return
     */
    @PostMapping("/profile")
    public ResponseEntity<Void> saveProfile(HttpServletRequest request,
            @Valid @RequestBody ProfileSaveRequest saveInfo) {
        service.saveProfile(request, saveInfo);
        return ResponseEntity.noContent().build();
    }

    /**
     * 초대 코드 갱신
     */
    @PatchMapping("/invite-code")
    public ResponseEntity<String> reCreateInviteCode(HttpServletRequest request) {
        return ResponseEntity.ok(service.reCreateInviteCode(request));
    }

    /**
     * 프로필 또는 배경 이미지 저장
     * 
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/profile/image")
    public ResponseEntity<Void> saveProfile(HttpServletRequest request,
            @RequestPart(value = "file", required = true) MultipartFile file,
            @RequestPart("type") ProfileImgType type) {
        service.saveProfile(request, file, type);
        return ResponseEntity.noContent().build();
    }

    /**
     * 팔로워 또는 팔로잉 목록 조회
     * 
     * @param request
     * @param info
     * @return
     */
    @PostMapping("/follow-list")
    public ResponseEntity<FollowListResponse> getFollowList(HttpServletRequest request,
            @Valid @RequestBody FollowListRequest info) {
        return ResponseEntity.ok(service.getFollowList(request, info));
    }

    /**
     * 팔로워 삭제
     * 
     * @param request
     * @param info
     * @return
     */
    @DeleteMapping("/follower/{userId}")
    public ResponseEntity<Void> deleteFollower(HttpServletRequest request,
            @PathVariable String userId) {
        service.deleteFollower(request, userId);
        return ResponseEntity.noContent().build();
    }

}
