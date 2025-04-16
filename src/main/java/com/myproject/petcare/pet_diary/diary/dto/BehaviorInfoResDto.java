package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.BehaviorIntensity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BehaviorInfoResDto {

    private Long diaryId;
    private Long petId;
    private LocalDate date;
    private String description;

    private String behaviorType;
    private BehaviorIntensity behaviorIntensity;

    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
