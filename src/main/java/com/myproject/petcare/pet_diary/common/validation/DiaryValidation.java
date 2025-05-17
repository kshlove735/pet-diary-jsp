package com.myproject.petcare.pet_diary.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DiaryValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiaryValidation {
    String message() default "일기 유형에 따라 필수 필드가 누락되었습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
