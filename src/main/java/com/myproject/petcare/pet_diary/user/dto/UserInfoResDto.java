package com.myproject.petcare.pet_diary.user.dto;

import lombok.Getter;

@Getter
public class UserInfoResDto {

    private String email;
    private String name;
    private String phone;

    public UserInfoResDto(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }
}
