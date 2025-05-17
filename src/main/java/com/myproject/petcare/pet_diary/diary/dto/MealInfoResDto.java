package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MealInfoResDto {

    private Long diaryId;
    private Long petId;
    private LocalDate date;
    private String description;

    private MealType mealType;
    private String foodBrand;
    private Integer foodAmount;

    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
