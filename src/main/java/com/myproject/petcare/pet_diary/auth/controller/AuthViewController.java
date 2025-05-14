package com.myproject.petcare.pet_diary.auth.controller;

import com.myproject.petcare.pet_diary.auth.dto.LoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.LoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.SignupReqDto;
import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.EmailDuplicationException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.EmailNotFoundException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.InvalidPasswordException;
import com.myproject.petcare.pet_diary.common.utils.CookieUtils;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthViewController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtils cookieUtils;


    @GetMapping("/auth/signup")
    public String signupPage(Model model) {
        model.addAttribute("signupReqDto", new SignupReqDto());
        return "signup";
    }

    @GetMapping("/auth/login")
    public String loginPage(Model model) {
        model.addAttribute("loginReqDto", new LoginReqDto());
        return "login";
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
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다!");
            return "redirect:/auth/login";
        } catch (EmailDuplicationException e) {
            log.error("이메일 중복 = {}", e.getMessage());
            bindingResult.rejectValue("email", "email.duplicate", "이미 사용 중인 이메일입니다.");
            return "signup";
        } catch (Exception e) {
            // 회원가입 진행 오류 발생 시
            log.error("회원 가입 실패 = {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/auth/signup";
        }
    }

    @PostMapping("/auth/login")
    public String login(@ModelAttribute @Validated LoginReqDto loginReqDto,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {

        // 유효성 검사 실패 시 로그인 페이지로
        if (bindingResult.hasErrors()) {
            log.info("유효성 검사 실패 = {}", bindingResult.getAllErrors());
            return "login";
        }
        try {
            // 로그인 처리 및 토근 생성
            LoginResDto loginResDto = authService.login(loginReqDto);
            // Access Token과 Refresh Token을 쿠키에 저장
            response.addCookie(cookieUtils.createCookie("access", loginResDto.getAccessToken(), jwtUtil.getAccessTokenExpTime().intValue() / 1000));
            response.addCookie(cookieUtils.createCookie("refresh", loginResDto.getRefreshToken(), jwtUtil.getRefreshTokenExpTime().intValue() / 1000));
            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "로그인 성공!");

            // returnUrl 처리
            String returnUrl = request.getParameter("returnUrl");
            String decodedReturnUrl = returnUrl != null ? UriUtils.decode(returnUrl, StandardCharsets.UTF_8) : null;

            // returnUrl 유효성 검증
            if (isValidReturnUrl(decodedReturnUrl)) {
                log.info("로그인 성공, returnUrl로 리다이렉트 : email = {}, returnUrl = {}", loginReqDto.getEmail(), decodedReturnUrl);
                return "redirect:" + decodedReturnUrl;
            } else {
                log.info("로그인 성공, 기본 경로로 리다이렉트 : email = {}, returnUrl = {}", loginReqDto.getEmail(), decodedReturnUrl);
                return "redirect:/user";
            }
        } catch (EmailNotFoundException e) {
            log.error("로그인 실패 = {}", e.getMessage(), e);
            bindingResult.rejectValue("email", "email.notFound", "등록된 이메일이 없습니다.");
            return "login";
        } catch (InvalidPasswordException e) {
            log.error("로그인 실패 = {}", e.getMessage(), e);
            bindingResult.rejectValue("password", "password.mismatch", "비밀번호가 일치하지 않습니다.");
            return "login";
        } catch (Exception e) {
            log.error("로그인 실패 = {}", e.getMessage(), e);
            bindingResult.reject("login.failed", "이메일 또는 비밀번호가 잘못되었습니다.");
            return "login";
        }
    }


    // returnUrl 유효성 검증
    private boolean isValidReturnUrl(String returnUrl) {
        // returnUrl이 없거나 빈 경우 유효하지 않음
        if (returnUrl == null || returnUrl.trim().isEmpty()) {
            return false;
        }
        // 로그인 페이지로의 리다이렉트 방지
        if (returnUrl.startsWith("/auth/login")) {
            return false;
        }
        // 외부 URL 또는 스크립트 포함 방지
        if (returnUrl.contains("://") || returnUrl.contains("<") || returnUrl.contains(">")) {
            return false;
        }
        return true;
    }
}
