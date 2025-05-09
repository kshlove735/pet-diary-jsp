package com.myproject.petcare.pet_diary.config;


import com.myproject.petcare.pet_diary.common.exception.filter.FilterExceptionFilter;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetailsService;
import com.myproject.petcare.pet_diary.jwt.JwtAuthenticationFilter;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // TODO : CORS 설정

        // CSRF 비활성화: JWT 기반 인증 및 stateless 세션 사용으로 CSRF 보호 불필요
        // TODO : 회원가입(/api/v1/auth/signup)은 CAPTCHA 또는 요청 제한으로 봇 공격 방지 권장
        http.csrf((auth) -> auth.disable());

        // Form 로그인 비활성화: JWT 기반 인증 사용
        http.formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 비활성화: JWT 사용
        http.httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers(
                        "/",
                        "/resources/**",      // 정적 리소스 (CSS, JS, 이미지 등)
                        "/static/**",         // 추가 정적 리소스 경로
                        "/WEB-INF/**",        // JSP 파일 경로 (직접 접근 방지용)
                        "/favicon.ico",
                        "/api/v1/auth/**"     // 인증 관련 엔드포인트
                ).permitAll()
                .anyRequest().authenticated());

        // 세션 설정: JWT 기반 인증을 위해 stateless 세션 정책 사용
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 필터 추가: JWT 인증 및 예외 처리
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new FilterExceptionFilter(), JwtAuthenticationFilter.class);

        // 인증 실패 시 로그인 페이지로 리다이렉트
        http.exceptionHandling((exception) -> exception
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/api/v1/auth/login")));

        return http.build();
    }
}
