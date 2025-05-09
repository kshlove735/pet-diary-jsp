package com.myproject.petcare.pet_diary.common.exception.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FilterExceptionFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            // JwtAuthenticationFilter에서 처리하지 않은 예외만 로그인 페이지로 리다이렉트
            log.error("예외 발생, 로그인 페이지로 리다이렉트: URI={}, message={}", request.getRequestURI(), e.getMessage(), e);
            // 리다이렉트 URL 생성, returnUrl 인코딩
            String redirectUrl = "/api/v1/auth/login?error=unauthorized&returnUrl=" +
                    UriUtils.encode(request.getRequestURI(), StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
        }
    }
}
