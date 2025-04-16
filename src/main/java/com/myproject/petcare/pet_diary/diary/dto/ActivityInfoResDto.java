package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ActivityInfoResDto {

    private Long diaryId;
    private Long petId;
    private LocalDate date;
    private String description;

    private ActivityType activityType;
    private Integer duration;
    private BigDecimal distance;
    private String location;

    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
