package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
// @DiaryValidation // 커스텀 유효성 검사 어노테이션
public class DiaryReqDto {

    @NotNull(message = "일기 유형은 필수 입력 항목입니다.")
    @Pattern(regexp = "activity|behavior|grooming|health|meal", message = "유효하지 않은 일기 유형입니다.")
    private String dtype;

    @NotNull(message = "기록 날짜는 필수 입력 항목입니다.")
    @PastOrPresent(message = "기록 날짜는 미래일 수 없습니다.")
    private LocalDate date;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;

    // activity
    //@NotNull(message = "활동 유형(산책, 놀이, 훈련, 수영)은 필수 입력 항목입니다.")
    private ActivityType activityType;

    private Integer duration;

    @Digits(integer = 3, fraction = 2, message = "몸무게는 정수 3자리, 소수 2자리까지 허용됩니다.")
    private BigDecimal distance;

    @Size(max = 50, message = "활동 장소는 50자를 초과할 수 없습니다.")
    private String location;

    // behavior
    //@NotNull(message = "행동 유형(예: 분리불안, 공격성 등)은 필수 입력 항목입니다.")
    private String behaviorType;

    //@NotNull(message = "강도(낮음, 보통, 높음)는 필수 입력 항목입니다.")
    private BehaviorIntensity behaviorIntensity;

    // grooming
    //@NotNull(message = "미용 유형(목욕, 이발, 발톱 손질, 귀 청소, 치아 관리)은 필수 입력 항목입니다.")
    private GroomingType groomingType;

    // health
    //@NotNull(message = "기록 유형(예방접종, 건강검진, 수술, 투약)은 필수 입력 항목입니다.")
    private HealthType healthType;

    private LocalDate nextDueDate;

    @Size(max = 50, message = "병원 이름은 50자를 초과할 수 없습니다.")
    private String clinic;

    // meal
    //@NotNull(message = "식사 유형(아침, 점심, 저녁, 간식)은 필수 입력 항목입니다.")
    private MealType mealType;

    @Size(max = 50, message = "사료량은 50자를 초과할 수 없습니다.")
    private String foodBrand;

    @Min(value = 0, message = "급여량은 0 이상이어야 합니다.")
    private Integer foodAmount;
}
