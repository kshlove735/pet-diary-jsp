package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.BehaviorIntensity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartialBehaviorReqDto {

    @NotNull(message = "행동 유형(예: 분리불안, 공격성 등)은 필수 입력 항목입니다.")
    private String behaviorType;

    @NotNull(message = "강도(낮음, 보통, 높음)는 필수 입력 항목입니다.")
    private BehaviorIntensity behaviorIntensity;

    @NotNull(message = "기록 날짜는 필수 입력 항목입니다.")
    private LocalDate date;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
}
