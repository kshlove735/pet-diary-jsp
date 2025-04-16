package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.entity.*;
import com.myproject.petcare.pet_diary.diary.enums.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class DiaryInfoWithJoinResDto {

    // diary
    private Long diaryId;
    private Long petId;
    private LocalDate date;
    private String dtype;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

    // activity
    private ActivityType activityType;
    private Integer duration;
    private BigDecimal distance;
    private String location;

    // behavior
    private String behaviorType;
    private BehaviorIntensity behaviorIntensity;

    // grooming
    private GroomingType groomingType;

    // health
    private HealthType healthType;
    private LocalDate nextDueDate;
    private String clinic;

    // meal
    private MealType mealType;
    private String foodBrand;
    private int foodAmount;

    public DiaryInfoWithJoinResDto(Diary diary) {
        // diary
        this.diaryId = diary.getId();
        this.petId = diary.getPet().getId();
        this.date = diary.getDate();
        this.dtype = diary.getDtype();
        this.description = diary.getDescription();
        this.createDate = diary.getCreateDate();
        this.updatedDate = diary.getUpdatedDate();

        if (diary.getDtype().equals("activity")) {
            this.activityType = ((Activity) diary).getActivityType();
            this.duration = ((Activity) diary).getDuration();
            this.distance = ((Activity) diary).getDistance();
            this.location = ((Activity) diary).getLocation();
        } else if (diary.getDtype().equals("behavior")) {
            this.behaviorType = ((Behavior) diary).getBehaviorType();
            this.behaviorIntensity = ((Behavior) diary).getBehaviorIntensity();
        } else if (diary.getDtype().equals("grooming")) {
            this.groomingType = ((Grooming) diary).getGroomingType();
        } else if (diary.getDtype().equals("health")) {
            this.healthType = ((Health) diary).getHealthType();
            this.nextDueDate = ((Health) diary).getNextDueDate();
            this.clinic = ((Health) diary).getClinic();
        } else if (diary.getDtype().equals("meal")) {
            this.mealType = ((Meal) diary).getMealType();
            this.foodBrand = ((Meal) diary).getFoodBrand();
            this.foodAmount = ((Meal) diary).getFoodAmount();
        }
    }
}
