package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.HealthType;
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
@DiscriminatorValue("health")
@NoArgsConstructor
public class Health extends Diary {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("건강 일기 유형(예방접종, 건강검진, 수술, 투약)")
    private HealthType healthType;

    @Comment("다음 예정일(예: 다음 접종일)")
    private LocalDate nextDueDate;

    @Column(length = 50)
    @Comment("병원 이름")
    private String clinic;


    public Health(Pet pet, HealthType healthType, String description, LocalDate date, LocalDate nextDueDate, String clinic) {
        super(pet, date, description);
        this.healthType = healthType;
        this.nextDueDate = nextDueDate;
        this.clinic = clinic;
    }
}
