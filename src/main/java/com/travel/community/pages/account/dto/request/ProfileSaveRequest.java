package com.travel.community.pages.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileSaveRequest {
    private String bio; // 자기소개
    private String location; // 위치
    @NotBlank
    private String nickName; // 닉네임
}
