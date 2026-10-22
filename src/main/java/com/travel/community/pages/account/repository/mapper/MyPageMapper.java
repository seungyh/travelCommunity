package com.travel.community.pages.account.repository.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.travel.community.pages.account.dto.FollowInfo;
import com.travel.community.pages.account.dto.request.FollowListRequest;
import com.travel.community.pages.account.dto.response.MyPageResponse;

@Mapper
public interface MyPageMapper {

    /**
     * 마이페이지 정보 조회
     * 
     * @param userId
     * @return
     */
    MyPageResponse getMyPageInfo(String userId);

    List<FollowInfo> getFollowList(FollowListRequest info);
}
