package com.myproject.petcare.pet_diary.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupReqDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank
    private String passwordCheck;

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$",
            message = "이름은 특수문자를 제외한 2~10자리여야 합니다.") // TODO : message 처리
    private String name;

    @NotBlank
    @Pattern(regexp = "^01[0-1,6-9][0-1]\\d{3,4}\\d{4}$|^01[0-1,6-9][0-1]-\\d{3,4}-\\d{4}$",
            message = "유효한 전화번호 형식을 입력해주세요. (예: 010-1234-5678)")
    private String phone;
}
