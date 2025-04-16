package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.GroomingType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartialGroomingReqDto {

    @NotNull(message = "미용 유형(목욕, 이발, 발톱 손질, 귀 청소, 치아 관리)은 필수 입력 항목입니다.")
    private GroomingType groomingType;

    @NotNull(message = "기록 날짜는 필수 입력 항목입니다.")
    private LocalDate date;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
}
