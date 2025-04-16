package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.HealthType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartialHealthReqDto {

    @NotNull(message = "기록 유형(예방접종, 건강검진, 수술, 투약)은 필수 입력 항목입니다.")
    private HealthType healthType;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;

    @NotNull(message = "기록 날짜는 필수 입력 항목입니다.")
    private LocalDate date;

    private LocalDate nextDueDate;

    @Size(max = 50, message = "병원 이름은 50자를 초과할 수 없습니다.")
    private String clinic;
}
