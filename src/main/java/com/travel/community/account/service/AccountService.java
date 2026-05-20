package com.travel.community.account.service;

import org.springframework.stereotype.Service;

import com.travel.community.account.dto.request.LoginRequest;

@Service
public class AccountService {

    public boolean login(LoginRequest loginInfo) {
        return true;
    }

}
