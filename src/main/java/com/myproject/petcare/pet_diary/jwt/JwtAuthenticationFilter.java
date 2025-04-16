package com.myproject.petcare.pet_diary.jwt;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.TokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    // 인가가 필요없는 경로 설정 할 경우
    private final List<String> excludedPatterns = Arrays.asList("/", "/api/v1/auth/**");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    // 인가가 필요없는 경로 설정 할 경우
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return excludedPatterns.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더 찾음
        String authorization = request.getHeader("Authorization");
        log.info("Auth = {}", authorization);
        // Authorization 헤더 검증
        if (!StringUtils.hasText(authorization)|| !authorization.startsWith("Bearer ")) {
            // 인가가 필요없는 경로 설정 할 경우
            throw new TokenNotFoundException("토큰이 없습니다.");

            //filterChain.doFilter(request, response);
            //return;
        }

        // Bearer 부분 제거 후 순수 토근만 획득
        String token = authorization.split(" ")[1];

        // 토큰 유효성 검사
        if (jwtUtil.isExpired(token)) {
            log.info("토큰이 유효하지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 id와 role 획득
        Long id = jwtUtil.getId(token);
        String role = jwtUtil.getRole(token);

        // UserDetails에 회원 정도 객체 담기
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(id.toString());

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 임시 센션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
