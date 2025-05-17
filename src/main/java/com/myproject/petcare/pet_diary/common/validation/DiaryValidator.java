package com.myproject.petcare.pet_diary.common.validation;

import com.myproject.petcare.pet_diary.diary.dto.DiaryReqDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DiaryValidator implements ConstraintValidator<DiaryValidation, DiaryReqDto> {

    @Override
    public boolean isValid(DiaryReqDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getDtype() == null) {
            return false;
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        switch (dto.getDtype()) {
            case "activity":
                if (dto.getActivityType() == null) {
                    addConstraintViolation(context, "활동 유형은 필수 입력 항목입니다.");
                    isValid = false;
                }
                break;
            case "behavior":
                if (dto.getBehaviorType() == null || dto.getBehaviorType().trim().isEmpty()) {
                    addConstraintViolation(context, "행동 유형은 필수 입력 항목입니다.");
                    isValid = false;
                }
                if (dto.getBehaviorIntensity() == null) {
                    addConstraintViolation(context, "행동 강도는 필수 입력 항목입니다.");
                    isValid = false;
                }
                break;
            case "grooming":
                if (dto.getGroomingType() == null) {
                    addConstraintViolation(context, "미용 유형은 필수 입력 항목입니다.");
                    isValid = false;
                }
                break;
            case "health":
                if (dto.getHealthType() == null) {
                    addConstraintViolation(context, "건강 유형은 필수 입력 항목입니다.");
                    isValid = false;
                }
                break;
            case "meal":
                if (dto.getMealType() == null) {
                    addConstraintViolation(context, "식사 유형은 필수 입력 항목입니다.");
                    isValid = false;
                }
                break;
            default:
                addConstraintViolation(context, "유효하지 않은 일기 유형입니다.");
                isValid = false;
        }
        return isValid;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message){
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
