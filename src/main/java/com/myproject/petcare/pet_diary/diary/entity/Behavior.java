package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.BehaviorIntensity;
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
@DiscriminatorValue("behavior")
@NoArgsConstructor
public class Behavior extends Diary{

    @Column(length = 50, nullable = false)
    @Comment("행동 유형(예: 분리불안, 공격성 등)")
    private String behaviorType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("강도(낮음, 보통, 높음)")
    private BehaviorIntensity behaviorIntensity;

    public Behavior(Pet pet, LocalDate date, String description, String behaviorType, BehaviorIntensity behaviorIntensity) {
        super(pet, date, description);
        this.behaviorType = behaviorType;
        this.behaviorIntensity = behaviorIntensity;
    }
}
