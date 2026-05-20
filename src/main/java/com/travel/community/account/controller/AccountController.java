package com.travel.community.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.community.account.dto.request.LoginRequest;
import com.travel.community.account.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web/api/account")
public class AccountController {

    private final AccountService service;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest loginInfo) {
        boolean res = service.login(loginInfo);
        return ResponseEntity.ok(res);
    }
}
