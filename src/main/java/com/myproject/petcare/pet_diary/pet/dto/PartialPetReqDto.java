package com.myproject.petcare.pet_diary.pet.dto;

import com.myproject.petcare.pet_diary.pet.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PartialPetReqDto {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Size(min = 1, max = 50, message = "이름은 1~50자 사이여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣\\s]+$", message = "이름은 한글, 영문, 공백만 허용됩니다.")
    private String name;

    @NotBlank(message = "품종은 필수 입력 항목입니다.")
    @Size(min = 1, max = 50, message = "품종은 1~50자 사이여야 합니다.")
    private String breed;

    @NotNull(message = "생년월일은 필수 입력 항목입니다.")
    @PastOrPresent(message = "생년월일은 미래 날짜일 수 없습니다.")
    private LocalDate birthDate;

    @NotNull(message = "성별은 필수 입력 항목입니다.")
    private Gender gender;

    @NotNull(message = "몸무게는 필수 입력 항목입니다.")
    @DecimalMin(value = "0.01", message = "몸무게는 0.01kg 이상이어야 합니다.")
    @DecimalMax(value = "999.99", message = "몸무게는 999.99kg 이하여야 합니다.")
    @Digits(integer = 3, fraction = 2, message = "몸무게는 정수 3자리, 소수 2자리까지 허용됩니다.")
    private BigDecimal weight;

    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
}
