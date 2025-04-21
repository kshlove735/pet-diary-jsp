package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.LoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.LoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.SignupReqDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.EmailDuplicationException;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Value("${spring.profiles.active:dev}") // 프로파일에 따라 secure 설정 동적 처리
    private String activeProfile;

    @PostMapping("/auth/check-email")
    @ResponseBody
    public Map<String, Object> checkEmail(@RequestParam String email) {

        // 입력 유효성 검사
        Map<String, Object> response = new HashMap<>();
        if (!StringUtils.hasText(email)) {
            response.put("available", false);
            response.put("message", "이메일을 입력해주세요.");
            return response;
        }
        boolean isAvailable = !authService.isEmailTaken(email);
        response.put("available", isAvailable);
        response.put("message", isAvailable ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.");
        return response;
    }

    @GetMapping("/auth/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupReqDto", new SignupReqDto());
        log.info("Model attributes = {}", model.asMap());
        return "signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute @Validated SignupReqDto signupReqDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         @RequestParam(defaultValue = "false") boolean emailChecked) {

        // 이메일 중복 검사 여부 확인
        if (!emailChecked) {
            bindingResult.rejectValue("email", "email.notChecked", "이메일 중복 검사를 진행해주세요.");
        }

        // 비밀번호 동일 유효성 검사
        if (!signupReqDto.getPassword().equals(signupReqDto.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "password.mismatch", "비밀번호가 일치하지 않습니다.");
        }

        // 유효성 검사 실패 시 signup.jsp로
        if (bindingResult.hasErrors()) {
            log.info("유효성 검사 실패 = {}", bindingResult.getAllErrors());
            return "signup";
        }

        // 회원가입 진행
        try {
            authService.signup(signupReqDto);
            redirectAttributes.addAttribute("status", true);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다!");
            return "redirect:/api/v1/auth/login";
        } catch (EmailDuplicationException e) {
            log.error("이메일 중복 = {}", e.getMessage());
            bindingResult.rejectValue("email", "email.duplicate", "이미 사용 중인 이메일입니다.");
            return "signup";
        } catch (Exception e) {
            // 회원가입 진행 오류 발생 시
            log.error("회원 가입 실패 = {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/api/v1/auth/signup";
        }
    }

    @GetMapping("/auth/login")
    public String loginPage(Model model) {
        model.addAttribute("loginReqDto", new LoginReqDto());
        return "login";
    }

    @PostMapping("/auth/login")
    public ResponseDto<LoginResDto> login(@RequestBody @Validated LoginReqDto loginReqDto, HttpServletResponse response) {
        LoginResDto loginResDto = authService.login(loginReqDto);
        response.addCookie(createCookie("access", loginResDto.getAccessToken(), jwtUtil.getAccessTokenExpTime().intValue() / 1000));
        response.addCookie(createCookie("refresh", loginResDto.getRefreshToken(), jwtUtil.getRefreshTokenExpTime().intValue() / 1000));

        return new ResponseDto<>(true, "로그인 성공", null);
    }

    @PostMapping("/auth/refresh")
    public ResponseDto refresh(@CookieValue("refresh") String refreshToken, HttpServletResponse response) {
        String accessToken = authService.refresh(refreshToken);
        response.addCookie(createCookie("access", accessToken, jwtUtil.getAccessTokenExpTime().intValue() / 1000));

        return new ResponseDto<>(true, "Access token 재생성 성공", null);
    }

    private Cookie createCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true); // JS로 접근 불가, 탈취 위험 감소
        cookie.setSecure("prod".equals(activeProfile)); // HTTPS에서만 전송 보장(개발 환경에서는 secure 비활성화)
        return cookie;
    }
}
