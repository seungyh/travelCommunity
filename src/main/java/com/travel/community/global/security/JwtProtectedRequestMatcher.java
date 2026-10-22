package com.travel.community.global.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

/** JWT 필터와 SecurityConfig가 동일한 메서드/경로 규칙을 공유합니다. */
@Component
public class JwtProtectedRequestMatcher implements RequestMatcher {

    @Value("${filter.get-mapping:}")
    private List<String> GET_MAPPING;

    @Value("${filter.post-mapping:}")
    private List<String> POST_MAPPING;

    @Value("${filter.put-mapping:}")
    private List<String> PUT_MAPPING;

    @Value("${filter.patch-mapping:}")
    private List<String> PATCH_MAPPING;

    @Value("${filter.delete-mapping:}")
    private List<String> DELETE_MAPPING;

    private List<RequestMatcher> matchers = List.of();

    // @Value 필드 주입이 완료된 후 매칭 규칙을 생성합니다.
    @PostConstruct
    public void init() {
        List<RequestMatcher> configured = new ArrayList<>();
        addMappings(configured, HttpMethod.GET, GET_MAPPING);
        // Spring MVC의 GET 핸들러는 HEAD도 처리하므로 동일하게 보호합니다.
        addMappings(configured, HttpMethod.HEAD, GET_MAPPING);
        addMappings(configured, HttpMethod.POST, POST_MAPPING);
        addMappings(configured, HttpMethod.PUT, PUT_MAPPING);
        addMappings(configured, HttpMethod.PATCH, PATCH_MAPPING);
        addMappings(configured, HttpMethod.DELETE, DELETE_MAPPING);
        this.matchers = List.copyOf(configured);
    }

    private static void addMappings(List<RequestMatcher> matchers, HttpMethod method, List<String> patterns) {
        for (String pattern : patterns) {
            if (StringUtils.hasText(pattern)) {
                matchers.add(PathPatternRequestMatcher.withDefaults().matcher(method, pattern.trim()));
            }
        }
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return matchers.stream().anyMatch(matcher -> matcher.matches(request));
    }
}