package com.travel.community.pages.account.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travel.community.pages.account.entity.NickNameWordEntity;

public interface NickNameRepository extends JpaRepository<NickNameWordEntity, Long> {

    /**
     * 닉네임 테이블에서 삭제안된 단어만 전체 조회
     * 
     * @param b
     * @return
     */
    List<NickNameWordEntity> findByIsDelFalse();

}
