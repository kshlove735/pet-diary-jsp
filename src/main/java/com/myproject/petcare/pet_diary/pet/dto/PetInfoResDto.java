package com.myproject.petcare.pet_diary.pet.dto;

import com.myproject.petcare.pet_diary.pet.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PetInfoResDto {
    private Long id;
    private String name;
    private String breed;
    private LocalDate birthDate;
    private Gender gender;
    private BigDecimal weight;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

}
