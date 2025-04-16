package com.myproject.petcare.pet_diary.auth.dto;

import lombok.Getter;

@Getter
public class LoginResDto {
    private String accessToken;
    private String refreshToken;

    public LoginResDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
