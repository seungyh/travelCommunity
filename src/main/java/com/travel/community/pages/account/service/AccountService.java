package com.travel.community.pages.account.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travel.community.global.exception.BusinessException;
import com.travel.community.global.security.jwt.JwtManager;
import com.travel.community.global.security.jwt.dto.TokenDto;
import com.travel.community.pages.account.dto.request.LoginRequest;
import com.travel.community.pages.account.dto.request.SignUpRequest;
import com.travel.community.pages.account.dto.response.LoginResponse;
import com.travel.community.pages.account.entity.UserEntity;
import com.travel.community.pages.account.enums.Roles;
import com.travel.community.pages.account.enums.UserResponseMsg;
import com.travel.community.pages.account.exception.enums.AccountErrorCode;
import com.travel.community.pages.account.repository.AccountRepository;
import com.travel.community.pages.common.dto.response.CommonResponse;
import com.travel.community.pages.oauth.enums.ProviderType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager jwtManager;

    public CommonResponse signUp(SignUpRequest request) {

        // 중복 ID 확인
        accountRepository.findByUserId(request.getUserId())
                .ifPresent(i -> {
                    throw new BusinessException(AccountErrorCode.EXIST_ACCOUNT);
                });

        log.info("회원가입 user id: {}", request.getUserId());
        String encryptPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = UserEntity.builder().userId(request.getUserId()).password(encryptPassword)
                .nickName(request.getNickName()).email(request.getEmail()).role(Roles.USER)
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
        String token = jwtManager.createJwt(user.getUserId(), ProviderType.LOCAL);
        jwtManager.addCookie(response, token);

        return LoginResponse.builder().email(user.getEmail()).nickName(user.getNickName()).userId(user.getUserId())
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
        return LoginResponse.builder().userId(userId).email(email).nickName(nickName).build();

    }

}
