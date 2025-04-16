package com.myproject.petcare.pet_diary.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.petcare.pet_diary.auth.dto.SignupReqDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("정상 회원 가입 요청")
    void signupSuccess() throws Exception {
        // Given : 유효한 회원 가입 데이터 준비
        SignupReqDto signupReqDto = new SignupReqDto();
        signupReqDto.setEmail("test2@gmail.com");
        signupReqDto.setPassword("TestPassword2!!");
        signupReqDto.setName("테스트유저");
        signupReqDto.setPhone("010-1234-1234");

        // When & Then : POST 요청 후 성공 응답 확인
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReqDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원 가입 성공"));
    }

    @Test
    @DisplayName("유효하지 않은 이메일")
    void signupFailDueToInvalidEmail() throws Exception {
        // Given : 유효한 회원 가입 데이터 준비
        SignupReqDto signupReqDto = new SignupReqDto();
        signupReqDto.setEmail("test_gmail.com");
        signupReqDto.setPassword("TestPassword2!!");
        signupReqDto.setName("테스트유저");
        signupReqDto.setPhone("010-1234-1234");

        // When & Then : POST 요청 후 성공 응답 확인
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReqDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DTO 검증 오류"));
    }

    @Test
    @DisplayName("비밀번호 패턴 불일치")
    void signupFailDueToInvalidPassword() throws Exception {
        // Given : 유효한 회원 가입 데이터 준비
        SignupReqDto signupReqDto = new SignupReqDto();
        signupReqDto.setEmail("test_gmail.com");
        signupReqDto.setPassword("Test!");
        signupReqDto.setName("테스트유저");
        signupReqDto.setPhone("010-1234-1234");

        // When & Then : POST 요청 후 성공 응답 확인
        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupReqDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("DTO 검증 오류"));
    }
}