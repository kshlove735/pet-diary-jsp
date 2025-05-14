package com.myproject.petcare.pet_diary.common.utils;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${spring.profiles.active:dev}") // 프로파일에 따라 secure 설정 동적 처리
    private String activeProfile;

    public Cookie createCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true); // JS로 접근 불가, 탈취 위험 감소
        cookie.setSecure("prod".equals(activeProfile)); // HTTPS에서만 전송 보장(개발 환경에서는 secure 비활성화)
        return cookie;
    }
}
