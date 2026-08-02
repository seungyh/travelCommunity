package com.travel.community.pages.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.community.pages.account.dto.request.LoginRequest;
import com.travel.community.pages.account.dto.request.SignUpRequest;
import com.travel.community.pages.account.dto.response.LoginResponse;
import com.travel.community.pages.account.service.AccountService;
import com.travel.community.pages.account.service.NickNameCreator;
import com.travel.community.pages.common.dto.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/api/account")
public class AccountController {

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
    public ResponseEntity<CommonResponse> login(HttpServletResponse response,
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
    public ResponseEntity<LoginResponse> kakaoLogin(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(service.tokenLogin(request));

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
}
