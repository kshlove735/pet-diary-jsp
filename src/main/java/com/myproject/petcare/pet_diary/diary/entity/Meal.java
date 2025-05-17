package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.MealType;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@DiscriminatorValue("meal")
@NoArgsConstructor
public class Meal extends Diary {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("식사 유형(아침, 점심, 저녁, 간식)")
    private MealType mealType;

    @Column(length = 50)
    @Comment("사료 브랜드")
    private String foodBrand;

    @Comment("급여량(g)")
    private Integer foodAmount;

    public Meal(Pet pet, LocalDate date, String description, MealType mealType, String foodBrand, Integer foodAmount) {
        super(pet, date, description);
        this.mealType = mealType;
        this.foodBrand = foodBrand;
        this.foodAmount = foodAmount;
    }
}
