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

        // csrf diable
        http.csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/api/v1/auth/**").permitAll()
                .anyRequest().authenticated());

        // 세션 설정 : JWT를 통한 인증/ 인가를 위해서 세션을 STATELESS 상태로 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new FilterExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }
}
