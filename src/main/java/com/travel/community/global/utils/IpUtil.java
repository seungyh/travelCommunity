package com.travel.community.global.utils;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {

    /**
     * 사용자 ip 반환
     * 
     * @param request
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip_addr = request.getHeader("X-Forwarded-For");

        if (ip_addr == null) {
            ip_addr = request.getHeader("Proxy-Client-IP");
        }
        if (ip_addr == null) {
            ip_addr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip_addr == null) {
            ip_addr = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip_addr == null) {
            ip_addr = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip_addr == null) {
            ip_addr = request.getRemoteAddr();
        }
        return ip_addr;
    }
}
