package com.myproject.petcare.pet_diary;

import com.myproject.petcare.pet_diary.diary.entity.*;
import com.myproject.petcare.pet_diary.diary.enums.*;
import com.myproject.petcare.pet_diary.diary.repository.DiaryRepository;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DiaryRepository diaryRepository;

    @PostConstruct
    public void init() {
        User user = new User();
        user.setEmail("test1@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("TestPassword1!!"));
        user.setName("테스트유저1");
        user.setPhone("010-1234-1234");
        user.setRole(Role.USER);
        userRepository.save(user);

        Pet pet = new Pet();
        pet.setName("멍멍이1");
        pet.setBreed("포메라니안");
        pet.setBirthDate(LocalDate.parse("1993-10-20"));
        pet.setGender(Gender.FEMALE);
        pet.setWeight(new BigDecimal("5.23"));
        pet.setDescription("설명");
        pet.changeUser(user);
        petRepository.save(pet);

        Activity activity1 = new Activity(
                pet,
                LocalDate.of(2025, 1, 5),
                "공원에서 즐거운 산책",
                ActivityType.WALK,
                30,
                new BigDecimal("500.00"),
                "근처 공원"
        );

        Activity activity2 = new Activity(
                pet,
                LocalDate.of(2025, 1, 15),
                "집에서 공놀이",
                ActivityType.PLAY,
                20,
                new BigDecimal("0.00"),
                "거실"
        );

        Activity activity3 = new Activity(
                pet,
                LocalDate.of(2025, 2, 10),
                "기본 명령 훈련",
                ActivityType.TRAINING,
                45,
                new BigDecimal("0.00"),
                "집 마당"
        );

        Activity activity4 = new Activity(
                pet,
                LocalDate.of(2025, 3, 1),
                "강에서 수영",
                ActivityType.SWIM,
                60,
                new BigDecimal("100.00"),
                "한강"
        );

        Activity activity5 = new Activity(
                pet,
                LocalDate.of(2025, 3, 20),
                "친구와 함께 산책",
                ActivityType.WALK,
                40,
                new BigDecimal("750.00"),
                "동네 공원"
        );


        Behavior behavior1 = new Behavior(
                pet,
                LocalDate.of(2025, 1, 3),
                "낮에 혼자 있을 때 짖음",
                "분리불안",
                BehaviorIntensity.MEDIUM
        );

        Behavior behavior2 = new Behavior(
                pet,
                LocalDate.of(2025, 1, 20),
                "낯선 사람에게 으르렁",
                "공격성",
                BehaviorIntensity.LOW
        );

        Behavior behavior3 = new Behavior(
                pet,
                LocalDate.of(2025, 2, 5),
                "집에서 물건 씹음",
                "파괴적 행동",
                BehaviorIntensity.HIGH
        );

        Behavior behavior4 = new Behavior(
                pet,
                LocalDate.of(2025, 2, 25),
                "밤에 계속 돌아다님",
                "불안",
                BehaviorIntensity.MEDIUM
        );

        Behavior behavior5 = new Behavior(
                pet,
                LocalDate.of(2025, 3, 15),
                "다른 개와 잘 어울림",
                "사회성",
                BehaviorIntensity.LOW
        );

        Grooming grooming1 = new Grooming(
                pet,
                LocalDate.of(2025, 1, 10),
                "깔끔하게 목욕 완료",
                GroomingType.BATH
        );

        Grooming grooming2 = new Grooming(
                pet,
                LocalDate.of(2025, 1, 25),
                "털이 너무 길어서 이발",
                GroomingType.HAIRCUT
        );

        Grooming grooming3 = new Grooming(
                pet,
                LocalDate.of(2025, 2, 15),
                "발톱이 길어서 정리",
                GroomingType.NAIL_TRIM
        );

        Grooming grooming4 = new Grooming(
                pet,
                LocalDate.of(2025, 3, 5),
                "귀가 더러워서 청소",
                GroomingType.EAR_CLEANING
        );

        Grooming grooming5 = new Grooming(
                pet,
                LocalDate.of(2025, 3, 25),
                "치석 제거",
                GroomingType.TEETH_CLEANING
        );

        Health health1 = new Health(
                pet,
                HealthType.VACCINATION,
                "광견병 예방접종 완료",
                LocalDate.of(2025, 1, 7),
                LocalDate.of(2026, 1, 7),
                "행복 동물병원"
        );

        Health health2 = new Health(
                pet,
                HealthType.CHECKUP,
                "정기 건강검진",
                LocalDate.of(2025, 1, 30),
                null,
                "사랑 동물병원"
        );

        Health health3 = new Health(
                pet,
                HealthType.SURGERY,
                "중성화 수술",
                LocalDate.of(2025, 2, 20),
                null,
                "희망 동물병원"
        );

        Health health4 = new Health(
                pet,
                HealthType.MEDICATION,
                "기생충 약 투여",
                LocalDate.of(2025, 3, 10),
                LocalDate.of(2025, 4, 10),
                "건강 동물병원"
        );

        Health health5 = new Health(
                pet,
                HealthType.VACCINATION,
                "종합 백신 접종",
                LocalDate.of(2025, 3, 28),
                LocalDate.of(2026, 3, 28),
                "행복 동물병원"
        );

        Meal meal1 = new Meal(
                pet,
                LocalDate.of(2025, 1, 1),
                "아침 식사 잘 먹음",
                MealType.BREAKFAST,
                "로얄캐닌",
                100
        );

        Meal meal2 = new Meal(
                pet,
                LocalDate.of(2025, 1, 12),
                "점심으로 간식 조금",
                MealType.SNACK,
                "그리니즈",
                20
        );

        Meal meal3 = new Meal(
                pet,
                LocalDate.of(2025, 2, 1),
                "저녁 식사 완식",
                MealType.DINNER,
                "힐스",
                120
        );

        Meal meal4 = new Meal(
                pet,
                LocalDate.of(2025, 2, 28),
                "아침 식사 조금 남김",
                MealType.BREAKFAST,
                "로얄캐닌",
                90
        );

        Meal meal5 = new Meal(
                pet,
                LocalDate.of(2025, 3, 18),
                "저녁에 간식 요청",
                MealType.SNACK,
                "치킨 트릿",
                30
        );

        diaryRepository.save(activity1);
        diaryRepository.save(behavior1);
        diaryRepository.save(grooming1);
        diaryRepository.save(health1);
        diaryRepository.save(meal1);

        diaryRepository.save(activity2);
        diaryRepository.save(behavior2);
        diaryRepository.save(grooming2);
        diaryRepository.save(health2);
        diaryRepository.save(meal2);

        diaryRepository.save(activity3);
        diaryRepository.save(behavior3);
        diaryRepository.save(grooming3);
        diaryRepository.save(health3);
        diaryRepository.save(meal3);

        diaryRepository.save(activity4);
        diaryRepository.save(behavior4);
        diaryRepository.save(grooming4);
        diaryRepository.save(health4);
        diaryRepository.save(meal4);

        diaryRepository.save(activity5);
        diaryRepository.save(behavior5);
        diaryRepository.save(grooming5);
        diaryRepository.save(health5);
        diaryRepository.save(meal5);

    }
}
