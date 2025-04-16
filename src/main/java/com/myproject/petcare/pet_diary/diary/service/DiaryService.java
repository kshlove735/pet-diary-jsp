package com.myproject.petcare.pet_diary.diary.service;

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

    public Page<DiaryInfoWithJoinResDto> getDiaryList(Long petId, List<String> dtype, Pageable pageable) {
        Page<Diary> page = diaryRepository.findByPetIdAndDtype(petId, dtype, pageable);

        Page<DiaryInfoWithJoinResDto> diaryInfoWithJoinResDtos = page.map(d -> new DiaryInfoWithJoinResDto(d));

        return diaryInfoWithJoinResDtos;
    }


    @Transactional
    public HealthInfoResDto createHealth(Long petId, PartialHealthReqDto partialHealthReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Health health = new Health(
                pet, partialHealthReqDto.getHealthType(), partialHealthReqDto.getDescription(),
                partialHealthReqDto.getDate(), partialHealthReqDto.getNextDueDate(),
                partialHealthReqDto.getClinic()
        );

        diaryRepository.save(health);

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health);
        return healthInfoResDto;
    }

    @Transactional
    public GroomingInfoResDto createGrooming(Long petId, PartialGroomingReqDto partialHealthReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Grooming grooming = new Grooming(
                pet, partialHealthReqDto.getDate(),
                partialHealthReqDto.getDescription(),
                partialHealthReqDto.getGroomingType()
        );

        diaryRepository.save(grooming);

        GroomingInfoResDto groomingInfoResDto = getGroomingInfoResDto(grooming);
        return groomingInfoResDto;
    }

    @Transactional
    public MealInfoResDto createMeal(Long petId, PartialMealReqDto partialMealReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Meal meal = new Meal(
                pet, partialMealReqDto.getDate(),
                partialMealReqDto.getDescription(), partialMealReqDto.getMealType(),
                partialMealReqDto.getFoodBrand(), partialMealReqDto.getFoodAmount()
        );

        diaryRepository.save(meal);

        MealInfoResDto mealInfoResDto = getMealInfoResDto(meal);
        return mealInfoResDto;
    }

    @Transactional
    public ActivityInfoResDto createActivity(Long petId, PartialActivityReqDto partialActivityReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Activity activity = new Activity(
                pet, partialActivityReqDto.getDate(), partialActivityReqDto.getDescription(),
                partialActivityReqDto.getActivityType(), partialActivityReqDto.getDuration(),
                partialActivityReqDto.getDistance(), partialActivityReqDto.getLocation()
        );

        diaryRepository.save(activity);

        ActivityInfoResDto activityInfoResDto = getActivityInfoResDto(activity);
        return activityInfoResDto;
    }

    @Transactional
    public BehaviorInfoResDto createBehavior(Long petId, PartialBehaviorReqDto partialMealReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        Behavior behavior = new Behavior(
                pet, partialMealReqDto.getDate(), partialMealReqDto.getDescription(),
                partialMealReqDto.getBehaviorType(), partialMealReqDto.getBehaviorIntensity()
        );

        diaryRepository.save(behavior);

        BehaviorInfoResDto behaviorInfoResDto = getBehaviorInfoResDto(behavior);
        return behaviorInfoResDto;
    }

    @Transactional
    public HealthInfoResDto updateHealth(Long diaryId, PartialHealthReqDto partialHealthReqDto) {
        Health health = (Health) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        health.setHealthType(partialHealthReqDto.getHealthType());

        if (StringUtils.hasText(partialHealthReqDto.getDescription())) {
            health.setDescription(partialHealthReqDto.getDescription());
        }

        health.setDate(partialHealthReqDto.getDate());

        if (partialHealthReqDto.getNextDueDate() != null) {
            health.setNextDueDate(partialHealthReqDto.getNextDueDate());
        }

        if (StringUtils.hasText(partialHealthReqDto.getClinic())) {
            health.setClinic(partialHealthReqDto.getClinic());
        }

        HealthInfoResDto healthInfoResDto = getHealthInfoResDto(health);
        return healthInfoResDto;
    }

    @Transactional
    public GroomingInfoResDto updateGrooming(Long diaryId, PartialGroomingReqDto partialHealthReqDto) {
        Grooming grooming = (Grooming) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        grooming.setGroomingType(partialHealthReqDto.getGroomingType());
        grooming.setDate(partialHealthReqDto.getDate());

        if (StringUtils.hasText(partialHealthReqDto.getDescription())) {
            grooming.setDescription(partialHealthReqDto.getDescription());
        }

        GroomingInfoResDto groomingInfoResDto = getGroomingInfoResDto(grooming);
        return groomingInfoResDto;
    }

    @Transactional
    public MealInfoResDto updateMeal(Long diaryId, PartialMealReqDto partialMealReqDto) {
        Meal meal = (Meal) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        meal.setMealType(partialMealReqDto.getMealType());

        if (StringUtils.hasText(partialMealReqDto.getFoodBrand())) {
            meal.setFoodBrand(partialMealReqDto.getFoodBrand());
        }

        if (partialMealReqDto.getFoodAmount() != null) {
            meal.setFoodAmount(partialMealReqDto.getFoodAmount());
        }

        meal.setDate(partialMealReqDto.getDate());

        if (StringUtils.hasText(partialMealReqDto.getDescription())) {
            meal.setDescription(partialMealReqDto.getDescription());
        }

        MealInfoResDto mealInfoResDto = getMealInfoResDto(meal);
        return mealInfoResDto;
    }

    @Transactional
    public ActivityInfoResDto updateActivity(Long diaryId, PartialActivityReqDto partialActivityReqDto) {
        Activity activity = (Activity) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        activity.setActivityType(partialActivityReqDto.getActivityType());

        if (partialActivityReqDto.getDuration() != null) {
            activity.setDuration(partialActivityReqDto.getDuration());

        }

        if (partialActivityReqDto.getDistance() != null) {
            activity.setDistance(partialActivityReqDto.getDistance());
        }

        if (StringUtils.hasText(partialActivityReqDto.getLocation())) {
            activity.setLocation(partialActivityReqDto.getLocation());
        }

        activity.setDate(partialActivityReqDto.getDate());

        if (StringUtils.hasText(partialActivityReqDto.getDescription())) {
            activity.setDescription(partialActivityReqDto.getDescription());
        }

        ActivityInfoResDto activityInfoResDto = getActivityInfoResDto(activity);
        return activityInfoResDto;
    }

    @Transactional
    public BehaviorInfoResDto updateBehavior(Long diaryId, PartialBehaviorReqDto partialBehaviorReqDto) {
        Behavior behavior = (Behavior) diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        if (StringUtils.hasText(partialBehaviorReqDto.getBehaviorType())) {
            behavior.setBehaviorType(partialBehaviorReqDto.getBehaviorType());
        }

        behavior.setBehaviorIntensity(partialBehaviorReqDto.getBehaviorIntensity());

        behavior.setDate(partialBehaviorReqDto.getDate());

        if (StringUtils.hasText(partialBehaviorReqDto.getDescription())) {
            behavior.setDescription(partialBehaviorReqDto.getDescription());
        }

        BehaviorInfoResDto behaviorInfoResDto = getBehaviorInfoResDto(behavior);
        return behaviorInfoResDto;
    }

    @Transactional
    public void deleteDiary(Long diaryId, CustomUserDetails customUserDetails) {
        // 일기 조회
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException("해당하는 일기가 없습니다."));

        // 권한 확인 : 현재 사용자가 해당 Pet, Dairy의 소유자인지 확인
        if(!Objects.equals(diary.getPet().getUser().getId(), Long.valueOf(customUserDetails.getUsername()))){
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
}
