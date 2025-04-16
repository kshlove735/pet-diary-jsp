package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.LoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.LoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.SignupReqDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @GetMapping("/auth/signup")
    public String singupPage(Model model){
        return "signup";
    }

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String signup(@ModelAttribute @Validated SignupReqDto signupReqDto) {
        authService.signup(signupReqDto);
        return "userInfo";
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto, HttpServletResponse response) {
        LoginResDto loginResDto = authService.login(loginReqDto);
        response.addCookie(createCookie("access", loginResDto.getAccessToken(), jwtUtil.getAccessTokenExpTime().intValue()/1000));
        response.addCookie(createCookie("refresh", loginResDto.getRefreshToken(), jwtUtil.getRefreshTokenExpTime().intValue()/1000));

        return new ResponseDto<>(true, "로그인 성공", null);
    }

    @PostMapping("/auth/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto refresh(@CookieValue("refresh") String refreshToken, HttpServletResponse response){
        String accessToken = authService.refresh(refreshToken);
        response.addCookie(createCookie("access", accessToken, jwtUtil.getAccessTokenExpTime().intValue()/1000));

        return new ResponseDto<>(true, "access token 재생성 성공", null);
    }

    private Cookie createCookie(String key, String value, int maxAge){
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true); // JS로 접근 불가, 탈취 위험 감소
        cookie.setSecure(true); // HTTPS에서만 전송 보장
        return cookie;
    }
}
