package com.myproject.petcare.pet_diary.diary.service;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.UnauthorizedException;
import com.myproject.petcare.pet_diary.diary.dto.*;
import com.myproject.petcare.pet_diary.diary.entity.*;
import com.myproject.petcare.pet_diary.diary.repository.DiaryRepository;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final PetRepository petRepository;
    private final DiaryRepository diaryRepository;

    public Page<DiaryInfoWithJoinResDto> getDiaryList(List<Long> petIds, List<String> dtypes, Pageable pageable) {
        Page<Diary> page = diaryRepository.findByPetIdAndDtype(petIds, dtypes, pageable);

        Page<DiaryInfoWithJoinResDto> diaryInfoWithJoinResDtos = page.map(d -> new DiaryInfoWithJoinResDto(d));

        return diaryInfoWithJoinResDtos;
    }

    public DiaryInfoWithJoinResDto getDiaryById(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다"));

        return new DiaryInfoWithJoinResDto(diary);
    }

    @Transactional
    public ResponseDto createDiary(Long petId, DiaryReqDto diaryReqDto) {

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        switch (diaryReqDto.getDtype()) {
            case "activity":
                ActivityInfoResDto activityInfoResDto = createActivity(pet, diaryReqDto);
                return new ResponseDto<>(true, "운동 기록 등록 성공", activityInfoResDto);
            case "behavior":
                BehaviorInfoResDto behaviorInfoResDto = createBehavior(pet, diaryReqDto);
                return new ResponseDto<>(true, "행동 기록 등록 성공", behaviorInfoResDto);
            case "grooming":
                GroomingInfoResDto groomingInfoResDto = createGrooming(pet, diaryReqDto);
                return new ResponseDto<>(true, "미용 기록 등록 성공", groomingInfoResDto);
            case "health":
                HealthInfoResDto healthInfoResDto = createHealth(pet, diaryReqDto);
                return new ResponseDto<>(true, "건강 기록 등록 성공", healthInfoResDto);
            case "meal":
                MealInfoResDto mealInfoResDto = createMeal(pet, diaryReqDto);
                return new ResponseDto<>(true, "식사 기록 등록 성공", mealInfoResDto);
            default:
                throw new IllegalArgumentException("지원되지 않는 일기 유형입니다: " + diaryReqDto.getDtype());
        }
    }

    @Transactional
    public ResponseDto updateDiary(Long diaryId, DiaryReqDto diaryReqDto) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        if (!Objects.equals(diary.getDtype(), diaryReqDto.getDtype())) {
            // 다른 타입 수정

            // 기존 일기 삭제
            diaryRepository.delete(diary);
            // 재등록
            Pet pet = diary.getPet();
            switch (diaryReqDto.getDtype()) {
                case "activity":
                    ActivityInfoResDto activityInfoResDto = createActivity(pet, diaryReqDto);
                    return new ResponseDto<>(true, "운동 기록으로 변경 성공", activityInfoResDto);
                case "behavior":
                    BehaviorInfoResDto behaviorInfoResDto = createBehavior(pet, diaryReqDto);
                    return new ResponseDto<>(true, "행동 기록으로 변경 성공", behaviorInfoResDto);
                case "grooming":
                    GroomingInfoResDto groomingInfoResDto = createGrooming(pet, diaryReqDto);
                    return new ResponseDto<>(true, "미용 기록으로 변경 성공", groomingInfoResDto);
                case "health":
                    HealthInfoResDto healthInfoResDto = createHealth(pet, diaryReqDto);
                    return new ResponseDto<>(true, "건강 기록으로 변경 성공", healthInfoResDto);
                case "meal":
                    MealInfoResDto mealInfoResDto = createMeal(pet, diaryReqDto);
                    return new ResponseDto<>(true, "식사 기록으로 변경 성공", mealInfoResDto);
                default:
                    throw new IllegalArgumentException("지원되지 않는 일기 유형입니다: " + diaryReqDto.getDtype());
            }

        } else {
            // 같은 타입 수정
            switch (diaryReqDto.getDtype()) {
                case "activity":
                    ActivityInfoResDto activityInfoResDto = updateActivity(diaryId, diaryReqDto);
                    return new ResponseDto<>(true, "운동 기록 수정 성공", activityInfoResDto);
                case "behavior":
                    BehaviorInfoResDto behaviorInfoResDto = updateBehavior(diaryId, diaryReqDto);
                    return new ResponseDto<>(true, "행동 기록 수정 성공", behaviorInfoResDto);
                case "grooming":
                    GroomingInfoResDto groomingInfoResDto = updateGrooming(diaryId, diaryReqDto);
                    return new ResponseDto<>(true, "미용 기록 수정 성공", groomingInfoResDto);
                case "health":
                    HealthInfoResDto healthInfoResDto = updateHealth(diaryId, diaryReqDto);
                    return new ResponseDto<>(true, "건강 기록 수정 성공", healthInfoResDto);
                case "meal":
                    MealInfoResDto mealInfoResDto = updateMeal(diaryId, diaryReqDto);
                    return new ResponseDto<>(true, "식사 기록 수정 성공", mealInfoResDto);
                default:
                    throw new IllegalArgumentException("지원되지 않는 일기 유형입니다: " + diaryReqDto.getDtype());
            }
        }
    }

    @Transactional
    public void deleteDiary(Long diaryId, CustomUserDetails customUserDetails) {
        // 일기 조회
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        // 권한 확인 : 현재 사용자가 해당 Pet, Dairy의 소유자인지 확인
        if (!Objects.equals(diary.getPet().getUser().getId(), Long.valueOf(customUserDetails.getUsername()))) {
            throw new UnauthorizedException("해당 일기를 삭제할 권한이 없습니다.");
        }

        // 일기 삭제
        diaryRepository.delete(diary);
    }

    private HealthInfoResDto getHealthInfoResDto(Health health) {
        return new HealthInfoResDto(
                health.getId(), health.getPet().getId(),
                health.getDate(), health.getDescription(),
                health.getHealthType(), health.getNextDueDate(),
                health.getClinic(), health.getCreateDate(), health.getUpdatedDate()
        );
    }

    private GroomingInfoResDto getGroomingInfoResDto(Grooming grooming) {
        return new GroomingInfoResDto(
                grooming.getId(), grooming.getPet().getId(),
                grooming.getDate(), grooming.getDescription(),
                grooming.getGroomingType(), grooming.getCreateDate(), grooming.getUpdatedDate()
        );
    }

    private MealInfoResDto getMealInfoResDto(Meal meal) {
        return new MealInfoResDto(
                meal.getId(), meal.getPet().getId(), meal.getDate(),
                meal.getDescription(), meal.getMealType(),
                meal.getFoodBrand(), meal.getFoodAmount(),
                meal.getCreateDate(), meal.getUpdatedDate()
        );
    }

    private ActivityInfoResDto getActivityInfoResDto(Activity activity) {
        return new ActivityInfoResDto(
                activity.getId(), activity.getPet().getId(),
                activity.getDate(), activity.getDescription(),
                activity.getActivityType(), activity.getDuration(),
                activity.getDistance(), activity.getLocation(),
                activity.getCreateDate(), activity.getUpdatedDate()
        );
    }

    private BehaviorInfoResDto getBehaviorInfoResDto(Behavior behavior) {
        return new BehaviorInfoResDto(
                behavior.getId(), behavior.getPet().getId(),
                behavior.getDate(), behavior.getDescription(),
                behavior.getBehaviorType(), behavior.getBehaviorIntensity(),
                behavior.getCreateDate(), behavior.getUpdatedDate()
        );
    }

    private HealthInfoResDto createHealth(Pet pet, DiaryReqDto diaryReqDto) {

        Health health = new Health(
                pet, diaryReqDto.getHealthType(), diaryReqDto.getDescription(),
                diaryReqDto.getDate(), diaryReqDto.getNextDueDate(),
                diaryReqDto.getClinic()
        );

        diaryRepository.save(health);

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health);
        return healthInfoResDto;
    }


    private GroomingInfoResDto createGrooming(Pet pet, DiaryReqDto diaryReqDto) {

        Grooming grooming = new Grooming(
                pet, diaryReqDto.getDate(),
                diaryReqDto.getDescription(),
                diaryReqDto.getGroomingType()
        );

        diaryRepository.save(grooming);

        GroomingInfoResDto groomingInfoResDto = getGroomingInfoResDto(grooming);
        return groomingInfoResDto;
    }


    private MealInfoResDto createMeal(Pet pet, DiaryReqDto diaryReqDto) {

        Meal meal = new Meal(
                pet, diaryReqDto.getDate(),
                diaryReqDto.getDescription(), diaryReqDto.getMealType(),
                diaryReqDto.getFoodBrand(), diaryReqDto.getFoodAmount()
        );

        diaryRepository.save(meal);

        MealInfoResDto mealInfoResDto = getMealInfoResDto(meal);
        return mealInfoResDto;
    }

    private ActivityInfoResDto createActivity(Pet pet, DiaryReqDto diaryReqDto) {

        Activity activity = new Activity(
                pet, diaryReqDto.getDate(), diaryReqDto.getDescription(),
                diaryReqDto.getActivityType(), diaryReqDto.getDuration(),
                diaryReqDto.getDistance(), diaryReqDto.getLocation()
        );

        diaryRepository.save(activity);

        ActivityInfoResDto activityInfoResDto = getActivityInfoResDto(activity);
        return activityInfoResDto;
    }


    private BehaviorInfoResDto createBehavior(Pet pet, DiaryReqDto diaryReqDto) {

        Behavior behavior = new Behavior(
                pet, diaryReqDto.getDate(), diaryReqDto.getDescription(),
                diaryReqDto.getBehaviorType(), diaryReqDto.getBehaviorIntensity()
        );

        diaryRepository.save(behavior);

        BehaviorInfoResDto behaviorInfoResDto = getBehaviorInfoResDto(behavior);
        return behaviorInfoResDto;
    }


    private HealthInfoResDto updateHealth(Long diaryId, DiaryReqDto diaryReqDto) {
        Health health = (Health) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        health.setHealthType(diaryReqDto.getHealthType());

        if (StringUtils.hasText(diaryReqDto.getDescription())) {
            health.setDescription(diaryReqDto.getDescription());
        }

        health.setDate(diaryReqDto.getDate());

        if (diaryReqDto.getNextDueDate() != null) {
            health.setNextDueDate(diaryReqDto.getNextDueDate());
        }

        if (StringUtils.hasText(diaryReqDto.getClinic())) {
            health.setClinic(diaryReqDto.getClinic());
        }

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health);
        return healthInfoResDto;
    }


    private GroomingInfoResDto updateGrooming(Long diaryId, DiaryReqDto diaryReqDto) {
        Grooming grooming = (Grooming) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        grooming.setGroomingType(diaryReqDto.getGroomingType());
        grooming.setDate(diaryReqDto.getDate());

        if (StringUtils.hasText(diaryReqDto.getDescription())) {
            grooming.setDescription(diaryReqDto.getDescription());
        }

        GroomingInfoResDto groomingInfoResDto = getGroomingInfoResDto(grooming);
        return groomingInfoResDto;
    }


    private MealInfoResDto updateMeal(Long diaryId, DiaryReqDto diaryReqDto) {
        Meal meal = (Meal) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        meal.setMealType(diaryReqDto.getMealType());

        if (StringUtils.hasText(diaryReqDto.getFoodBrand())) {
            meal.setFoodBrand(diaryReqDto.getFoodBrand());
        }

        if (diaryReqDto.getFoodAmount() != null) {
            meal.setFoodAmount(diaryReqDto.getFoodAmount());
        }

        meal.setDate(diaryReqDto.getDate());

        if (StringUtils.hasText(diaryReqDto.getDescription())) {
            meal.setDescription(diaryReqDto.getDescription());
        }

        MealInfoResDto mealInfoResDto = getMealInfoResDto(meal);
        return mealInfoResDto;
    }


    private ActivityInfoResDto updateActivity(Long diaryId, DiaryReqDto diaryReqDto) {
        Activity activity = (Activity) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        activity.setActivityType(diaryReqDto.getActivityType());

        if (diaryReqDto.getDuration() != null) {
            activity.setDuration(diaryReqDto.getDuration());

        }

        if (diaryReqDto.getDistance() != null) {
            activity.setDistance(diaryReqDto.getDistance());
        }

        if (StringUtils.hasText(diaryReqDto.getLocation())) {
            activity.setLocation(diaryReqDto.getLocation());
        }

        activity.setDate(diaryReqDto.getDate());

        if (StringUtils.hasText(diaryReqDto.getDescription())) {
            activity.setDescription(diaryReqDto.getDescription());
        }

        ActivityInfoResDto activityInfoResDto = getActivityInfoResDto(activity);
        return activityInfoResDto;
    }


    private BehaviorInfoResDto updateBehavior(Long diaryId, DiaryReqDto diaryReqDto) {
        Behavior behavior = (Behavior) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        if (StringUtils.hasText(diaryReqDto.getBehaviorType())) {
            behavior.setBehaviorType(diaryReqDto.getBehaviorType());
        }

        behavior.setBehaviorIntensity(diaryReqDto.getBehaviorIntensity());

        behavior.setDate(diaryReqDto.getDate());

        if (StringUtils.hasText(diaryReqDto.getDescription())) {
            behavior.setDescription(diaryReqDto.getDescription());
        }

        BehaviorInfoResDto behaviorInfoResDto = getBehaviorInfoResDto(behavior);
        return behaviorInfoResDto;
    }
}
