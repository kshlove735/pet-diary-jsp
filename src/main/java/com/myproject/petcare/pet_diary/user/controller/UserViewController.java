package com.myproject.petcare.pet_diary.user.controller;

import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.user.dto.UpdatePasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UserInfoResDto;
import com.myproject.petcare.pet_diary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/user")
    public String getUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            Model model) {
        // 유저 정보 조회
        UserInfoResDto userInfoResDto = userService.getUser(customUserDetails);
        // JSP에 렌더링할 데이터 추가
        model.addAttribute("userInfo", userInfoResDto);
        return "userInfo";
    }

    @GetMapping("/user/password")
    public String updatePasswordPage(Model model){
        model.addAttribute("updatePasswordReqDto", new UpdatePasswordReqDto());
        return "passwordChange";
    }
}
