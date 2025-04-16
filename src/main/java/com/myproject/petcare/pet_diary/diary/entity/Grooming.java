package com.myproject.petcare.pet_diary.diary.entity;

import com.myproject.petcare.pet_diary.diary.enums.GroomingType;
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
@DiscriminatorValue("grooming")
@NoArgsConstructor
public class Grooming extends Diary {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("미용 유형(목욕, 이발, 발톱 손질, 귀 청소, 치아 관리)")
    private GroomingType groomingType;


    public Grooming(Pet pet, LocalDate date, String description, GroomingType groomingType) {
        super(pet, date, description);
        this.groomingType = groomingType;
    }
}