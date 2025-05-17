package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.diary.dto.*;
import com.myproject.petcare.pet_diary.diary.service.DiaryService;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/diary")
    public ResponseDto<Page<DiaryInfoWithJoinResDto>> getDiaryList(
            @RequestParam(value = "petId", required = false) List<Long> petIds,
            @RequestParam(value = "dtype", required = false) List<String> dtypes,
            Pageable pageable
    ) {

        Page<DiaryInfoWithJoinResDto> diaryInfoResDtos = diaryService.getDiaryList(petIds, dtypes, pageable);
        return new ResponseDto<>(true, "일기 리스트 조회 성공", diaryInfoResDtos);
    }

    @PostMapping("/diary/{petId}")
    public ResponseDto createDiary(
            @PathVariable("petId") @Positive(message = "반려견 ID는 양수여야 합니다.") Long petId,
            @RequestBody @Validated DiaryReqDto diaryReqDto
    ) {

        switch (diaryReqDto.getDtype()) {
            case "activity":
                ActivityInfoResDto activityRes = diaryService.createActivity(petId, toActivityReqDto(diaryReqDto));
                return new ResponseDto<>(true, "운동 기록 등록 성공", activityRes);
            case "behavior":
                BehaviorInfoResDto behaviorRes = diaryService.createBehavior(petId, toBehaviorReqDto(diaryReqDto));
                return new ResponseDto<>(true, "행동 기록 등록 성공", behaviorRes);
            case "grooming":
                GroomingInfoResDto groomingRes = diaryService.createGrooming(petId, toGroomingReqDto(diaryReqDto));
                return new ResponseDto<>(true, "미용 기록 등록 성공", groomingRes);
            case "health":
                HealthInfoResDto healthRes = diaryService.createHealth(petId, toHealthReqDto(diaryReqDto));
                return new ResponseDto<>(true, "건강 기록 등록 성공", healthRes);
            case "meal":
                MealInfoResDto mealRes = diaryService.createMeal(petId, toMealReqDto(diaryReqDto));
                return new ResponseDto<>(true, "식사 기록 등록 성공", mealRes);
            default:
                throw new IllegalArgumentException("지원되지 않는 일기 유형입니다: " + diaryReqDto.getDtype());
        }
    }


    // @PostMapping("/diary/{petId}/health")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<HealthInfoResDto> createHealth(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialHealthReqDto partialHealthReqDto
    ) {
        HealthInfoResDto healthInfoResDto = diaryService.createHealth(petId, partialHealthReqDto);
        return new ResponseDto<>(true, "건강 기록 등록 성공", healthInfoResDto);
    }

    // @PostMapping("/diary/{petId}/grooming")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<GroomingInfoResDto> createGrooming(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialGroomingReqDto partialHealthReqDto
    ) {
        GroomingInfoResDto groomingInfoResDto = diaryService.createGrooming(petId, partialHealthReqDto);
        return new ResponseDto<>(true, "미용 기록 등록 성공", groomingInfoResDto);
    }

    // @PostMapping("/diary/{petId}/meal")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MealInfoResDto> createMeal(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialMealReqDto partialMealReqDto
    ) {
        MealInfoResDto mealInfoResDto = diaryService.createMeal(petId, partialMealReqDto);
        return new ResponseDto<>(true, "식사 기록 등록 성공", mealInfoResDto);
    }

    // @PostMapping("/diary/{petId}/activity")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ActivityInfoResDto> createActivity(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialActivityReqDto partialActivityReqDto
    ) {
        ActivityInfoResDto activityInfoResDto = diaryService.createActivity(petId, partialActivityReqDto);
        return new ResponseDto<>(true, "운동 기록 등록 성공", activityInfoResDto);
    }

    // @PostMapping("/diary/{petId}/behavior")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<BehaviorInfoResDto> createBehavior(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialBehaviorReqDto partialMealReqDto
    ) {
        BehaviorInfoResDto behaviorInfoResDto = diaryService.createBehavior(petId, partialMealReqDto);
        return new ResponseDto<>(true, "행동 기록 등록 성공", behaviorInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/health")
    public ResponseDto<HealthInfoResDto> updateHealth(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialHealthReqDto partialHealthReqDto
    ) {
        HealthInfoResDto healthInfoResDto = diaryService.updateHealth(diaryId, partialHealthReqDto);
        return new ResponseDto<>(true, "건강 기록 수정 성공", healthInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/grooming")
    public ResponseDto<GroomingInfoResDto> updateGrooming(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialGroomingReqDto partialHealthReqDto
    ) {
        GroomingInfoResDto groomingInfoResDto = diaryService.updateGrooming(diaryId, partialHealthReqDto);
        return new ResponseDto<>(true, "미용 기록 수정 성공", groomingInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/meal")
    public ResponseDto<MealInfoResDto> updateMeal(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialMealReqDto partialMealReqDto
    ) {
        MealInfoResDto mealInfoResDto = diaryService.updateMeal(diaryId, partialMealReqDto);
        return new ResponseDto<>(true, "식사 기록 수정 성공", mealInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/activity")
    public ResponseDto<ActivityInfoResDto> updateActivity(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialActivityReqDto partialActivityReqDto
    ) {
        ActivityInfoResDto activityInfoResDto = diaryService.updateActivity(diaryId, partialActivityReqDto);
        return new ResponseDto<>(true, "운동 기록 수정 성공", activityInfoResDto);
    }

    @PutMapping("/diary/{diaryId}/behavior")
    public ResponseDto<BehaviorInfoResDto> updateBehavior(
            @PathVariable("diaryId") Long diaryId,
            @RequestBody @Validated PartialBehaviorReqDto partialBehaviorReqDto
    ) {
        BehaviorInfoResDto behaviorInfoResDto = diaryService.updateBehavior(diaryId, partialBehaviorReqDto);
        return new ResponseDto<>(true, "행동 기록 수정 성공", behaviorInfoResDto);
    }

    @DeleteMapping("/diary/{diaryId}")
    public ResponseDto deleteDiary(
            @PathVariable("diaryId") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        diaryService.deleteDiary(diaryId, customUserDetails);
        return new ResponseDto<>(true, "일기 삭제 성공", null);
    }

    // DTO 변환 메서드
    private PartialActivityReqDto toActivityReqDto(DiaryReqDto dto) {
        PartialActivityReqDto activityDto = new PartialActivityReqDto();
        activityDto.setActivityType(dto.getActivityType());
        activityDto.setDuration(dto.getDuration());
        activityDto.setDistance(dto.getDistance());
        activityDto.setLocation(dto.getLocation());
        activityDto.setDate(dto.getDate());
        activityDto.setDescription(dto.getDescription());
        return activityDto;
    }

    private PartialBehaviorReqDto toBehaviorReqDto(DiaryReqDto dto) {
        PartialBehaviorReqDto behaviorDto = new PartialBehaviorReqDto();
        behaviorDto.setBehaviorType(dto.getBehaviorType());
        behaviorDto.setBehaviorIntensity(dto.getBehaviorIntensity());
        behaviorDto.setDate(dto.getDate());
        behaviorDto.setDescription(dto.getDescription());
        return behaviorDto;
    }

    private PartialGroomingReqDto toGroomingReqDto(DiaryReqDto dto) {
        PartialGroomingReqDto groomingDto = new PartialGroomingReqDto();
        groomingDto.setGroomingType(dto.getGroomingType());
        groomingDto.setDate(dto.getDate());
        groomingDto.setDescription(dto.getDescription());
        return groomingDto;
    }

    private PartialHealthReqDto toHealthReqDto(DiaryReqDto dto) {
        PartialHealthReqDto healthDto = new PartialHealthReqDto();
        healthDto.setHealthType(dto.getHealthType());
        healthDto.setNextDueDate(dto.getNextDueDate());
        healthDto.setClinic(dto.getClinic());
        healthDto.setDate(dto.getDate());
        healthDto.setDescription(dto.getDescription());
        return healthDto;
    }

    private PartialMealReqDto toMealReqDto(DiaryReqDto dto) {
        PartialMealReqDto mealDto = new PartialMealReqDto();
        mealDto.setMealType(dto.getMealType());
        mealDto.setFoodBrand(dto.getFoodBrand());
        mealDto.setFoodAmount(dto.getFoodAmount());
        mealDto.setDate(dto.getDate());
        mealDto.setDescription(dto.getDescription());
        return mealDto;
    }
}
