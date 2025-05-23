package com.myproject.petcare.pet_diary.user.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.user.dto.CheckPasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdatePasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdateUserReqDto;
import com.myproject.petcare.pet_diary.user.dto.UserInfoResDto;
import com.myproject.petcare.pet_diary.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PutMapping("/user")
    public ResponseDto<UserInfoResDto> updateUser(
            @RequestBody @Validated UpdateUserReqDto updateUserReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        UserInfoResDto userInfoResDto = userService.updateUser(updateUserReqDto, customUserDetails);
        return new ResponseDto<>(true, "회원 정보 수정 성공", userInfoResDto);

    }

    @GetMapping("/user/password/verify")
    public ResponseDto checkPassword(
            @RequestBody @Validated CheckPasswordReqDto checkPasswordReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        boolean isPasswordEqual = userService.checkPassword(checkPasswordReqDto, customUserDetails);
        return new ResponseDto<>(true, "현재 비밀번호와 동일합니다.", null);

    }

    @GetMapping("/user/verify-auth")
    public ResponseDto verifyAuth(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null) {
            return new ResponseDto<>(false, "인증이 필요합니다.", null);
        }
        return new ResponseDto<>(true, "인증됨", null);
    }



    @PutMapping("/user/password")
    public ResponseDto updatePassword(
            @RequestBody @Validated UpdatePasswordReqDto updatePasswordReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.updatePassword(updatePasswordReqDto, customUserDetails);
        return new ResponseDto<>(true, "비밀번호 수정 성공", null);
    }

    @PutMapping("/user/logout")
    public ResponseDto logout(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletResponse response) {

        userService.logout(customUserDetails);

        Cookie deleteAccessCookie = deleteCookie("access");
        Cookie deleteRefreshCookie = deleteCookie("refresh");
        response.addCookie(deleteAccessCookie);
        response.addCookie(deleteRefreshCookie);
        return new ResponseDto<>(true, "로그아웃 성공", null);
    }

    @DeleteMapping("/user")
    public ResponseDto deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        userService.deleteUser(customUserDetails);
        return new ResponseDto<>(true, "회원 탈퇴 성공", null);
    }

    private Cookie deleteCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
