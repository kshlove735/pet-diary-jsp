package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.ActivityType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PartialActivityReqDto {

    @NotNull(message = "활동 유형(산책, 놀이, 훈련, 수영)은 필수 입력 항목입니다.")
    private ActivityType activityType;

    private Integer duration;

    @Digits(integer = 3, fraction = 2, message = "몸무게는 정수 3자리, 소수 2자리까지 허용됩니다.")
    private BigDecimal distance;

    @Size(max = 50, message = "활동 장소는 50자를 초과할 수 없습니다.")
    private String location;

    @NotNull(message = "기록 날짜는 필수 입력 항목입니다.")
    private LocalDate date;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
}
