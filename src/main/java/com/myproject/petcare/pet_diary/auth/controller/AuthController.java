package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.common.utils.CookieUtils;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtils;

    @GetMapping("/auth/check-email")
    public ResponseDto checkEmail(
            @RequestParam
            @Email(message = "유효한 이메일 형식이 아닙니다.")
            @NotBlank(message = "이메일을 입력해주세요.")
            String email) {

        boolean isAvailable = !authService.isEmailTaken(email);
        String message = isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.";
        return new ResponseDto<>(isAvailable, message, null);
    }

    @PostMapping("/auth/refresh")
    public ResponseDto refresh(@CookieValue("refresh") String refreshToken, HttpServletResponse response) {
        String accessToken = authService.refresh(refreshToken);
        response.addCookie(cookieUtils.createCookie("access", accessToken, jwtUtil.getAccessTokenExpTime().intValue() / 1000));

        return new ResponseDto<>(true, "Access token 재생성 성공", null);
    }
}
