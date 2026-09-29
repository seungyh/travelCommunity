package com.travel.community.global.utils;

import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // I, L, 1 같은 헷갈리는 문자 없이 난수 생성용 텍스트

    /**
     * 여행 계획 등 초대 코드 생성
     * 
     * @return
     */
    public static String createInviteCode() {
        int codeSize = 10;

        StringBuilder code = new StringBuilder(codeSize);

        for (int i = 0; i < codeSize; i++) {
            int idx = random.nextInt(CHARS.length());
            code.append(CHARS.charAt(idx));
        }

        return code.toString();
    }
}
