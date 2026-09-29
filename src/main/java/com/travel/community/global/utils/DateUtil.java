package com.travel.community.global.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {

    /**
     * 자정까지의 시간을 second로 반환
     * 
     * @return
     */
    public static long getSecondsUntilMidnight() {

        // 1. 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 2. 오늘 날짜의 가장 마지막 시간(23:59:59.999999999) 구하기
        LocalDateTime endOfToday = now.with(LocalTime.MAX);

        // 3. 현재 시간부터 오늘 마감 시간까지의 차이(초) 계산 + 내일 정각으로 넘어가기 위한 1초 추가
        return Duration.between(now, endOfToday).getSeconds() + 1;
    }
}
