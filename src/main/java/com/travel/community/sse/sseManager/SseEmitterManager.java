package com.travel.community.sse.sseManager;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.travel.community.pages.alarm.dto.AlarmInfo;

@Component
public class SseEmitterManager {

    private static final long TIMEOUT = 30 * 60 * 1000L;

    // userId 별로 sseEmitter 관리
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> remove(userId, emitter));
        emitter.onTimeout(() -> remove(userId, emitter));
        emitter.onError(e -> remove(userId, emitter));

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("connect")
                            .data("connected"));
        } catch (IOException e) {
            remove(userId, emitter);
            emitter.completeWithError(e);
        }

        return emitter;

    }

    public void send(String userId, AlarmInfo data) {
        SseEmitter emitter = emitters.get(userId);

        if (emitter == null) {
            return;
        }

        try {
            emitter.send(
                    SseEmitter.event()
                            .name("alarm")
                            .data(data));
        } catch (IOException e) {
            remove(userId, emitter);
            emitter.completeWithError(e);
        }
    }

    private void remove(String userId, SseEmitter emitter) {
        emitters.remove(userId, emitter);
    }

}
