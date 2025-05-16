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

        Pet pet1 = new Pet();
        pet1.setName("멍멍이");
        pet1.setBreed("포메라니안");
        pet1.setBirthDate(LocalDate.parse("1993-10-20"));
        pet1.setGender(Gender.FEMALE);
        pet1.setWeight(new BigDecimal("5.23"));
        pet1.setDescription("설명");
        pet1.changeUser(user);
        petRepository.save(pet1);

        Pet pet2 = new Pet();
        pet2.setName("나비");
        pet2.setBreed("포메라니안");
        pet2.setBirthDate(LocalDate.parse("1995-10-20"));
        pet2.setGender(Gender.MALE);
        pet2.setWeight(new BigDecimal("3.3"));
        pet2.changeUser(user);
        petRepository.save(pet2);

        // Pet1 (멍멍이) Diary Entries
        Activity activity1 = new Activity(
                pet1,
                LocalDate.of(2025, 1, 5),
                "공원에서 즐거운 산책",
                ActivityType.WALK,
                30,
                new BigDecimal("500.00"),
                "근처 공원"
        );

        Activity activity2 = new Activity(
                pet1,
                LocalDate.of(2025, 1, 15),
                "집에서 공놀이",
                ActivityType.PLAY,
                20,
                new BigDecimal("0.00"),
                "거실"
        );

        Activity activity3 = new Activity(
                pet1,
                LocalDate.of(2025, 2, 10),
                "기본 명령 훈련",
                ActivityType.TRAINING,
                45,
                new BigDecimal("0.00"),
                "집 마당"
        );

        Activity activity4 = new Activity(
                pet1,
                LocalDate.of(2025, 3, 1),
                "강에서 수영",
                ActivityType.SWIM,
                60,
                new BigDecimal("100.00"),
                "한강"
        );

        Activity activity5 = new Activity(
                pet1,
                LocalDate.of(2025, 3, 20),
                "친구와 함께 산책",
                ActivityType.WALK,
                40,
                new BigDecimal("750.00"),
                "동네 공원"
        );

        Behavior behavior1 = new Behavior(
                pet1,
                LocalDate.of(2025, 1, 3),
                "낮에 혼자 있을 때 짖음",
                "분리불안",
                BehaviorIntensity.MEDIUM
        );

        Behavior behavior2 = new Behavior(
                pet1,
                LocalDate.of(2025, 1, 20),
                "낯선 사람에게 으르렁",
                "공격성",
                BehaviorIntensity.LOW
        );

        Behavior behavior3 = new Behavior(
                pet1,
                LocalDate.of(2025, 2, 5),
                "집에서 물건 씹음",
                "파괴적 행동",
                BehaviorIntensity.HIGH
        );

        Behavior behavior4 = new Behavior(
                pet1,
                LocalDate.of(2025, 2, 25),
                "밤에 계속 돌아다님",
                "불안",
                BehaviorIntensity.MEDIUM
        );

        Behavior behavior5 = new Behavior(
                pet1,
                LocalDate.of(2025, 3, 15),
                "다른 개와 잘 어울림",
                "사회성",
                BehaviorIntensity.LOW
        );

        Grooming grooming1 = new Grooming(
                pet1,
                LocalDate.of(2025, 1, 10),
                "깔끔하게 목욕 완료",
                GroomingType.BATH
        );

        Grooming grooming2 = new Grooming(
                pet1,
                LocalDate.of(2025, 1, 25),
                "털이 너무 길어서 이발",
                GroomingType.HAIRCUT
        );

        Grooming grooming3 = new Grooming(
                pet1,
                LocalDate.of(2025, 2, 15),
                "발톱이 길어서 정리",
                GroomingType.NAIL_TRIM
        );

        Grooming grooming4 = new Grooming(
                pet1,
                LocalDate.of(2025, 3, 5),
                "귀가 더러워서 청소",
                GroomingType.EAR_CLEANING
        );

        Grooming grooming5 = new Grooming(
                pet1,
                LocalDate.of(2025, 3, 25),
                "치석 제거",
                GroomingType.TEETH_CLEANING
        );

        Health health1 = new Health(
                pet1,
                HealthType.VACCINATION,
                "광견병 예방접종 완료",
                LocalDate.of(2025, 1, 7),
                LocalDate.of(2026, 1, 7),
                "행복 동물병원"
        );

        Health health2 = new Health(
                pet1,
                HealthType.CHECKUP,
                "정기 건강검진",
                LocalDate.of(2025, 1, 30),
                null,
                "사랑 동물병원"
        );

        Health health3 = new Health(
                pet1,
                HealthType.SURGERY,
                "중성화 수술",
                LocalDate.of(2025, 2, 20),
                null,
                "희망 동물병원"
        );

        Health health4 = new Health(
                pet1,
                HealthType.MEDICATION,
                "기생충 약 투여",
                LocalDate.of(2025, 3, 10),
                LocalDate.of(2025, 4, 10),
                "건강 동물병원"
        );

        Health health5 = new Health(
                pet1,
                HealthType.VACCINATION,
                "종합 백신 접종",
                LocalDate.of(2025, 3, 28),
                LocalDate.of(2026, 3, 28),
                "행복 동물병원"
        );

        Meal meal1 = new Meal(
                pet1,
                LocalDate.of(2025, 1, 1),
                "아침 식사 잘 먹음",
                MealType.BREAKFAST,
                "로얄캐닌",
                100
        );

        Meal meal2 = new Meal(
                pet1,
                LocalDate.of(2025, 1, 12),
                "점심으로 간식 조금",
                MealType.SNACK,
                "그리니즈",
                20
        );

        Meal meal3 = new Meal(
                pet1,
                LocalDate.of(2025, 2, 1),
                "저녁 식사 완식",
                MealType.DINNER,
                "힐스",
                120
        );

        Meal meal4 = new Meal(
                pet1,
                LocalDate.of(2025, 2, 28),
                "아침 식사 조금 남김",
                MealType.BREAKFAST,
                "로얄캐닌",
                90
        );

        Meal meal5 = new Meal(
                pet1,
                LocalDate.of(2025, 3, 18),
                "저녁에 간식 요청",
                MealType.SNACK,
                "치킨 트릿",
                30
        );

        // Pet2 (나비) Diary Entries
        Activity activity6 = new Activity(
                pet2,
                LocalDate.of(2025, 1, 8),
                "해변에서 산책",
                ActivityType.WALK,
                35,
                new BigDecimal("600.00"),
                "해운대 해변"
        );

        Activity activity7 = new Activity(
                pet2,
                LocalDate.of(2025, 1, 18),
                "장난감으로 놀이",
                ActivityType.PLAY,
                25,
                new BigDecimal("0.00"),
                "집 안"
        );

        Activity activity8 = new Activity(
                pet2,
                LocalDate.of(2025, 2, 12),
                "앉기 훈련",
                ActivityType.TRAINING,
                40,
                new BigDecimal("0.00"),
                "마당"
        );

        Activity activity9 = new Activity(
                pet2,
                LocalDate.of(2025, 3, 3),
                "호수에서 수영",
                ActivityType.SWIM,
                50,
                new BigDecimal("200.00"),
                "일산 호수공원"
        );

        Activity activity10 = new Activity(
                pet2,
                LocalDate.of(2025, 3, 22),
                "이웃 강아지와 산책",
                ActivityType.WALK,
                45,
                new BigDecimal("800.00"),
                "아파트 단지"
        );

        Behavior behavior6 = new Behavior(
                pet2,
                LocalDate.of(2025, 1, 6),
                "혼자 있을 때 낑낑거림",
                "분리불안",
                BehaviorIntensity.LOW
        );

        Behavior behavior7 = new Behavior(
                pet2,
                LocalDate.of(2025, 1, 22),
                "낯선 개에게 짖음",
                "공격성",
                BehaviorIntensity.MEDIUM
        );

        Behavior behavior8 = new Behavior(
                pet2,
                LocalDate.of(2025, 2, 8),
                "신발 물어뜯음",
                "파괴적 행동",
                BehaviorIntensity.MEDIUM
        );

        Behavior behavior9 = new Behavior(
                pet2,
                LocalDate.of(2025, 2, 28),
                "밤에 자꾸 깨어있음",
                "불안",
                BehaviorIntensity.HIGH
        );

        Behavior behavior10 = new Behavior(
                pet2,
                LocalDate.of(2025, 3, 18),
                "사람들과 친화적",
                "사회성",
                BehaviorIntensity.LOW
        );

        Grooming grooming6 = new Grooming(
                pet2,
                LocalDate.of(2025, 1, 12),
                "전문가에게 목욕 맡김",
                GroomingType.BATH
        );

        Grooming grooming7 = new Grooming(
                pet2,
                LocalDate.of(2025, 1, 28),
                "여름 스타일로 이발",
                GroomingType.HAIRCUT
        );

        Grooming grooming8 = new Grooming(
                pet2,
                LocalDate.of(2025, 2, 18),
                "발톱 깎기",
                GroomingType.NAIL_TRIM
        );

        Grooming grooming9 = new Grooming(
                pet2,
                LocalDate.of(2025, 3, 8),
                "귀 청소 완료",
                GroomingType.EAR_CLEANING
        );

        Grooming grooming10 = new Grooming(
                pet2,
                LocalDate.of(2025, 3, 28),
                "치아 세척",
                GroomingType.TEETH_CLEANING
        );

        Health health6 = new Health(
                pet2,
                HealthType.VACCINATION,
                "광견병 백신 접종",
                LocalDate.of(2025, 1, 9),
                LocalDate.of(2026, 1, 9),
                "평화 동물병원"
        );

        Health health7 = new Health(
                pet2,
                HealthType.CHECKUP,
                "연간 건강검진",
                LocalDate.of(2025, 2, 2),
                null,
                "안심 동물병원"
        );

        Health health8 = new Health(
                pet2,
                HealthType.SURGERY,
                "치석 제거 수술",
                LocalDate.of(2025, 2, 22),
                null,
                "미래 동물병원"
        );

        Health health9 = new Health(
                pet2,
                HealthType.MEDICATION,
                "심장사상충 예방약",
                LocalDate.of(2025, 3, 12),
                LocalDate.of(2025, 4, 12),
                "행복 동물병원"
        );

        Health health10 = new Health(
                pet2,
                HealthType.VACCINATION,
                "종합 백신 2차 접종",
                LocalDate.of(2025, 3, 30),
                LocalDate.of(2026, 3, 30),
                "평화 동물병원"
        );

        Meal meal6 = new Meal(
                pet2,
                LocalDate.of(2025, 1, 2),
                "아침 식사 완식",
                MealType.BREAKFAST,
                "오리젠",
                110
        );

        Meal meal7 = new Meal(
                pet2,
                LocalDate.of(2025, 1, 15),
                "점심 간식 잘 먹음",
                MealType.SNACK,
                "덴탈본",
                25
        );

        Meal meal8 = new Meal(
                pet2,
                LocalDate.of(2025, 2, 3),
                "저녁 식사 좋아함",
                MealType.DINNER,
                "아카나",
                130
        );

        Meal meal9 = new Meal(
                pet2,
                LocalDate.of(2025, 2, 26),
                "아침 식사 조금 먹음",
                MealType.BREAKFAST,
                "오리젠",
                95
        );

        Meal meal10 = new Meal(
                pet2,
                LocalDate.of(2025, 3, 20),
                "저녁 간식 달라고 졸라",
                MealType.SNACK,
                "연어 트릿",
                35
        );

        // Save Pet1 Diary Entries
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

        // Save Pet2 Diary Entries
        diaryRepository.save(activity6);
        diaryRepository.save(behavior6);
        diaryRepository.save(grooming6);
        diaryRepository.save(health6);
        diaryRepository.save(meal6);

        diaryRepository.save(activity7);
        diaryRepository.save(behavior7);
        diaryRepository.save(grooming7);
        diaryRepository.save(health7);
        diaryRepository.save(meal7);

        diaryRepository.save(activity8);
        diaryRepository.save(behavior8);
        diaryRepository.save(grooming8);
        diaryRepository.save(health8);
        diaryRepository.save(meal8);

        diaryRepository.save(activity9);
        diaryRepository.save(behavior9);
        diaryRepository.save(grooming9);
        diaryRepository.save(health9);
        diaryRepository.save(meal9);

        diaryRepository.save(activity10);
        diaryRepository.save(behavior10);
        diaryRepository.save(grooming10);
        diaryRepository.save(health10);
        diaryRepository.save(meal10);
    }
}