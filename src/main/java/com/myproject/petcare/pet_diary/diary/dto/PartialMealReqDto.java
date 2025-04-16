package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartialMealReqDto {

    @NotNull(message = "식사 유형(아침, 점심, 저녁, 간식)은 필수 입력 항목입니다.")
    private MealType mealType;

    @Size(max = 50, message = "사료량은 50자를 초과할 수 없습니다.")
    private String foodBrand;

    private Integer foodAmount;

    @NotNull(message = "기록 날짜는 필수 입력 항목입니다.")
    private LocalDate date;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
}
