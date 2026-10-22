package com.travel.community.pages.travelPlan.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InviteMemberRequest {
    private Long planId; // 초대할 계획 id
    private String tag; // 초대할 사용자 tag
}
