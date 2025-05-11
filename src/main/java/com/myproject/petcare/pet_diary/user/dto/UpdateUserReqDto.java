package com.myproject.petcare.pet_diary.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserReqDto {

    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$",
            message = "이름은 특수문자를 제외한 2~10자리여야 합니다.") // TODO : message 처리
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^01[0-1,6-9]-?\\d{3,4}-?\\d{4}$",
            message = "유효한 전화번호 형식을 입력해주세요. (예: 010-1234-5678 또는 01012345678)")
    private String phone;
}
