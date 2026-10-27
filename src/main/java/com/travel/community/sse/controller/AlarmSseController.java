package com.travel.community.sse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.travel.community.sse.service.AlarmSseService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequestMapping("/web/api/alarm")
@RequiredArgsConstructor
@RestController
public class AlarmSseController {

    private final AlarmSseService alarmSseService;

    @GetMapping("/sse/subscribe")
    public SseEmitter subscribe(HttpServletRequest request) {
        return alarmSseService.subscribe(request);
    }
}
