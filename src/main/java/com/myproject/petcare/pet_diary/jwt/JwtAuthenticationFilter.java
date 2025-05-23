package com.myproject.petcare.pet_diary.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    // 인가가 필요없는 경로 설정 할 경우
    private final List<String> excludedPatterns = Arrays.asList(
            "/",
            "/resources/**",      // 정적 리소스 (CSS, JS, 이미지 등)
            "/static/**",         // 추가 정적 리소스 경로
            "/WEB-INF/**",        // JSP 파일 경로 (직접 접근 방지용)
            "/favicon.ico",
            "/api/v1/auth/**",    // 인증 관련 엔드포인트
            "/auth/**");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    // 인가가 필요없는 경로 설정 할 경우
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        boolean isExcluded = excludedPatterns.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
        log.info("Request URI: {}, Excluded: {}", requestURI, isExcluded);
        return isExcluded;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에서 access 토큰 가져오기
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 토큰 유효성 검사
        if (!StringUtils.hasText(token)) {
            log.info("Access token이 없습니다. 로그인 페이지로 리다이렉트: URI={}", request.getRequestURI());
            // 리다이렉트 URL 생성, returnUrl 인코딩으로 XSS 방지
            String redirectUrl = "/auth/login?error=unauthorized&returnUrl=" +
                    UriUtils.encode(request.getRequestURI(), StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
            return;
        }

        // 토큰 만료 검사
        if (jwtUtil.isExpired(token)) {
            log.info("토큰이 만료되었습니다. 로그인 페이지로 리다이렉트: URI={}", request.getRequestURI());
            // 리다이렉트 URL 생성, returnUrl 인코딩으로 XSS 방지
            String redirectUrl = "/auth/login?error=unauthorized&returnUrl=" +
                    UriUtils.encode(request.getRequestURI(), StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
            return;
        }

        // 토큰에서 id와 role 획득
        Long id = jwtUtil.getId(token);
        String role = jwtUtil.getRole(token);

        // UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(id.toString());

        // 인증 객체 생성 및 SecurityContext에 설정
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
