//package com.myproject.petcare.pet_diary.dairy.service;
//
//import com.myproject.petcare.pet_diary.diary.dto.*;
//import com.myproject.petcare.pet_diary.diary.entity.*;
//import com.myproject.petcare.pet_diary.diary.enums.*;
//import com.myproject.petcare.pet_diary.diary.repository.DiaryRepository;
//import com.myproject.petcare.pet_diary.diary.service.DiaryService;
//import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
//import com.myproject.petcare.pet_diary.pet.entity.Pet;
//import com.myproject.petcare.pet_diary.pet.enums.Gender;
//import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
//import com.myproject.petcare.pet_diary.user.entity.User;
//import com.myproject.petcare.pet_diary.user.enums.Role;
//import com.myproject.petcare.pet_diary.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringBootTest
//@RequiredArgsConstructor
//@Transactional
//class DairyServiceTest {
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PetRepository petRepository;
//
//    @Autowired
//    private DiaryService diaryService;
//
//    @Autowired
//    private DiaryRepository diaryRepository;
//
//
//    private User testUser;
//    private Pet testPet;
//    private Health testHealth;
//    private Grooming testGrooming;
//    private Meal testMeal;
//    private Activity testActivity;
//    private Behavior testBehavior;
//    private CustomUserDetails customUserDetails;
//
//    @BeforeEach
//    void before() {
//        testUser = new User();
//        testUser.setEmail("test2@gmail.com");
//        testUser.setPassword(bCryptPasswordEncoder.encode("TestPassword2!!"));
//        testUser.setName("테스트유저1");
//        testUser.setPhone("010-1234-1234");
//        testUser.setRole(Role.USER);
//        userRepository.save(testUser);
//
//        customUserDetails = new CustomUserDetails(testUser);
//
//        testPet = new Pet();
//        testPet.setName("멍멍이1");
//        testPet.setBreed("포메라니안");
//        testPet.setBirthDate(LocalDate.parse("1993-10-20"));
//        testPet.setGender(Gender.FEMALE);
//        testPet.setWeight(new BigDecimal("5.23"));
//        testPet.setDescription(null);
//        testPet.changeUser(testUser);
//        petRepository.save(testPet);
//
//        testHealth = new Health();
//        testHealth.setPet(testPet);
//        testHealth.setHealthType(HealthType.VACCINATION);
//        testHealth.setDescription("광견병 예방접종 완료");
//        testHealth.setDate(LocalDate.parse("2025-03-25"));
//        testHealth.setNextDueDate(LocalDate.parse("2026-03-25"));
//        testHealth.setClinic("행복 동물병원");
//        diaryRepository.save(testHealth);
//
//        testGrooming = new Grooming();
//        testGrooming.setPet(testPet);
//        testGrooming.setDescription("이발");
//        testGrooming.setDate(LocalDate.parse("2025-03-25"));
//        testGrooming.setGroomingType(GroomingType.HAIRCUT);
//        diaryRepository.save(testGrooming);
//
//        testMeal = new Meal();
//        testMeal.setPet(testPet);
//        testMeal.setDescription("아침에 평소보다 식욕이 좋았습니다. 사료를 거의 다 먹었고 물도 많이 마셨습니다.");
//        testMeal.setDate(LocalDate.parse("2025-03-25"));
//        testMeal.setMealType(MealType.BREAKFAST);
//        testMeal.setFoodBrand("로얄캐닌 미니 어덜트");
//        testMeal.setFoodAmount(100);
//        diaryRepository.save(testMeal);
//
//        testActivity = new Activity();
//        testActivity.setPet(testPet);
//        testActivity.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        testActivity.setDate(LocalDate.parse("2025-03-25"));
//        testActivity.setActivityType(ActivityType.WALK);
//        testActivity.setDuration(45);
//        testActivity.setDistance(new BigDecimal(2));
//        testActivity.setLocation("한강공원");
//        diaryRepository.save(testActivity);
//
//        testBehavior = new Behavior();
//        testBehavior.setPet(testPet);
//        testBehavior.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        testBehavior.setDate(LocalDate.parse("2025-03-25"));
//        testBehavior.setBehaviorType("분리불안");
//        testBehavior.setBehaviorIntensity(BehaviorIntensity.HIGH);
//        diaryRepository.save(testBehavior);
//    }
//
//    @Test
//    @DisplayName("건강 기록 등록 성공")
//    void createHealthSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialHealthReqDto partialHealthReqDto1 = new PartialHealthReqDto();
//        partialHealthReqDto1.setHealthType(HealthType.VACCINATION);
//        partialHealthReqDto1.setDescription("광견병 예방접종 완료");
//        partialHealthReqDto1.setDate(LocalDate.parse("2025-03-25"));
//        partialHealthReqDto1.setNextDueDate(LocalDate.parse("2026-03-25"));
//        partialHealthReqDto1.setClinic("행복 동물병원");
//
//        // when
//        HealthInfoResDto healthInfoResDto1 = diaryService.createHealth(testPet.getId(), partialHealthReqDto1);
//
//        // then
//        assertThat(healthInfoResDto1.getHealthType()).isEqualTo(partialHealthReqDto1.getHealthType());
//        assertThat(healthInfoResDto1.getDescription()).isEqualTo(partialHealthReqDto1.getDescription());
//        assertThat(healthInfoResDto1.getDate()).isEqualTo(partialHealthReqDto1.getDate());
//        assertThat(healthInfoResDto1.getNextDueDate()).isEqualTo(partialHealthReqDto1.getNextDueDate());
//        assertThat(healthInfoResDto1.getClinic()).isEqualTo(partialHealthReqDto1.getClinic());
//
//
//        // given : 선택 속성 값 없을 때
//        PartialHealthReqDto partialHealthReqDto2 = new PartialHealthReqDto();
//        partialHealthReqDto2.setHealthType(HealthType.VACCINATION);
//        partialHealthReqDto2.setDate(LocalDate.parse("2025-03-25"));
//
//        // when
//        HealthInfoResDto healthInfoResDto2 = diaryService.createHealth(testPet.getId(), partialHealthReqDto2);
//
//
//        // then
//        assertThat(healthInfoResDto2.getHealthType()).isEqualTo(partialHealthReqDto2.getHealthType());
//        assertThat(healthInfoResDto2.getDescription()).isEqualTo(partialHealthReqDto2.getDescription());
//        assertThat(healthInfoResDto2.getDate()).isEqualTo(partialHealthReqDto2.getDate());
//        assertThat(healthInfoResDto2.getNextDueDate()).isEqualTo(partialHealthReqDto2.getNextDueDate());
//        assertThat(healthInfoResDto2.getClinic()).isEqualTo(partialHealthReqDto2.getClinic());
//    }
//
//    @Test
//    @DisplayName("건강 기록 등록 실패 - 필수 속성 값 없음")
//    void createHealthFail() {
//
//        // given
//        PartialHealthReqDto partialHealthReqDto1 = new PartialHealthReqDto();
//        partialHealthReqDto1.setDescription("광견병 예방접종 완료");
//        partialHealthReqDto1.setDate(LocalDate.parse("2025-03-25"));
//        partialHealthReqDto1.setNextDueDate(LocalDate.parse("2026-03-25"));
//        partialHealthReqDto1.setClinic("행복 동물병원");
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createHealth(testPet.getId(), partialHealthReqDto1));
//
//        // given
//        PartialHealthReqDto partialHealthReqDto2 = new PartialHealthReqDto();
//        partialHealthReqDto2.setHealthType(HealthType.VACCINATION);
//        partialHealthReqDto2.setDescription("광견병 예방접종 완료");
//        partialHealthReqDto2.setNextDueDate(LocalDate.parse("2026-03-25"));
//        partialHealthReqDto2.setClinic("행복 동물병원");
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createHealth(testPet.getId(), partialHealthReqDto2));
//    }
//
//    @Test
//    @DisplayName("건강 기록 수정 성공")
//    void updateHealthSuccess() {
//        // given : 모든 속성 값 있을 때
//        Long diaryId = testHealth.getId();
//
//        PartialHealthReqDto partialHealthReqDto1 = new PartialHealthReqDto();
//        partialHealthReqDto1.setHealthType(HealthType.CHECKUP);
//        partialHealthReqDto1.setDescription("건강 검진 완료");
//        partialHealthReqDto1.setDate(LocalDate.parse("2025-03-30"));
//        partialHealthReqDto1.setNextDueDate(LocalDate.parse("2026-03-30"));
//        partialHealthReqDto1.setClinic("우리 동물병원");
//
//        // then
//        HealthInfoResDto healthInfoResDto1 = diaryService.updateHealth(diaryId, partialHealthReqDto1);
//
//        // when
//        assertThat(healthInfoResDto1.getHealthType()).isEqualTo(partialHealthReqDto1.getHealthType());
//        assertThat(healthInfoResDto1.getDescription()).isEqualTo(partialHealthReqDto1.getDescription());
//        assertThat(healthInfoResDto1.getDate()).isEqualTo(partialHealthReqDto1.getDate());
//        assertThat(healthInfoResDto1.getNextDueDate()).isEqualTo(partialHealthReqDto1.getNextDueDate());
//        assertThat(healthInfoResDto1.getClinic()).isEqualTo(partialHealthReqDto1.getClinic());
//
//
//        // given : 선택 속성 값 없을 때
//        PartialHealthReqDto partialHealthReqDto2 = new PartialHealthReqDto();
//        partialHealthReqDto2.setHealthType(HealthType.VACCINATION);
//        partialHealthReqDto2.setDate(LocalDate.parse("2025-03-25"));
//
//        // when
//        HealthInfoResDto healthInfoResDto2 = diaryService.createHealth(testPet.getId(), partialHealthReqDto2);
//
//
//        // then
//        assertThat(healthInfoResDto2.getHealthType()).isEqualTo(partialHealthReqDto2.getHealthType());
//        assertThat(healthInfoResDto2.getDescription()).isEqualTo(partialHealthReqDto2.getDescription());
//        assertThat(healthInfoResDto2.getDate()).isEqualTo(partialHealthReqDto2.getDate());
//        assertThat(healthInfoResDto2.getNextDueDate()).isEqualTo(partialHealthReqDto2.getNextDueDate());
//        assertThat(healthInfoResDto2.getClinic()).isEqualTo(partialHealthReqDto2.getClinic());
//    }
//
//
//    @Test
//    @DisplayName("미용 기록 등록 성공")
//    void createGroomingSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialGroomingReqDto partialGroomingReqDto1 = new PartialGroomingReqDto();
//        partialGroomingReqDto1.setDescription("이발");
//        partialGroomingReqDto1.setDate(LocalDate.parse("2025-03-25"));
//        partialGroomingReqDto1.setGroomingType(GroomingType.HAIRCUT);
//        // when
//        GroomingInfoResDto groomingInfoResDto1 = diaryService.createGrooming(testPet.getId(), partialGroomingReqDto1);
//
//        // then
//        assertThat(groomingInfoResDto1.getDescription()).isEqualTo(partialGroomingReqDto1.getDescription());
//        assertThat(groomingInfoResDto1.getDate()).isEqualTo(partialGroomingReqDto1.getDate());
//        assertThat(groomingInfoResDto1.getGroomingType()).isEqualTo(partialGroomingReqDto1.getGroomingType());
//    }
//
//    @Test
//    @DisplayName("미용 기록 등록 실패 - 필수 속성 값 없음")
//    void createGroomingFail() {
//
//        // given : 필수 속성 값이 없을 때
//        PartialGroomingReqDto partialGroomingReqDto1 = new PartialGroomingReqDto();
//        partialGroomingReqDto1.setDescription("이발");
//        partialGroomingReqDto1.setDate(LocalDate.parse("2025-03-25"));
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createGrooming(testPet.getId(), partialGroomingReqDto1));
//
//
//        // given : 필수 속성 값이 없을 때
//        PartialGroomingReqDto partialGroomingReqDto2 = new PartialGroomingReqDto();
//        partialGroomingReqDto2.setDescription("이발");
//        partialGroomingReqDto2.setGroomingType(GroomingType.HAIRCUT);
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createGrooming(testPet.getId(), partialGroomingReqDto2));
//    }
//
//    @Test
//    @DisplayName("미용 기록 수정 성공")
//    void updateGroomingSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialGroomingReqDto partialGroomingReqDto1 = new PartialGroomingReqDto();
//        partialGroomingReqDto1.setDescription("목욕");
//        partialGroomingReqDto1.setDate(LocalDate.parse("2025-03-30"));
//        partialGroomingReqDto1.setGroomingType(GroomingType.BATH);
//        // when
//        GroomingInfoResDto groomingInfoResDto1 = diaryService.updateGrooming(testGrooming.getId(), partialGroomingReqDto1);
//
//        // then
//        assertThat(groomingInfoResDto1.getDescription()).isEqualTo(partialGroomingReqDto1.getDescription());
//        assertThat(groomingInfoResDto1.getDate()).isEqualTo(partialGroomingReqDto1.getDate());
//        assertThat(groomingInfoResDto1.getGroomingType()).isEqualTo(partialGroomingReqDto1.getGroomingType());
//
//        // given : 속성 값이 몇개만 있을때
//        PartialGroomingReqDto partialGroomingReqDto2 = new PartialGroomingReqDto();
//        partialGroomingReqDto2.setDate(LocalDate.parse("2025-03-30"));
//        partialGroomingReqDto2.setGroomingType(GroomingType.BATH);
//        // when
//        GroomingInfoResDto groomingInfoResDto2 = diaryService.updateGrooming(testGrooming.getId(), partialGroomingReqDto2);
//
//        // then
//        assertThat(groomingInfoResDto2.getDescription()).isEqualTo(partialGroomingReqDto1.getDescription());
//        assertThat(groomingInfoResDto2.getDate()).isEqualTo(partialGroomingReqDto1.getDate());
//        assertThat(groomingInfoResDto2.getGroomingType()).isEqualTo(partialGroomingReqDto1.getGroomingType());
//    }
//
//    @Test
//    @DisplayName("식사 기록 등록 성공")
//    void createMealSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialMealReqDto partialMealReqDto = new PartialMealReqDto();
//        partialMealReqDto.setDescription("아침에 평소보다 식욕이 좋았습니다. 사료를 거의 다 먹었고 물도 많이 마셨습니다.");
//        partialMealReqDto.setDate(LocalDate.parse("2025-03-25"));
//        partialMealReqDto.setMealType(MealType.BREAKFAST);
//        partialMealReqDto.setFoodBrand("로얄캐닌 미니 어덜트");
//        partialMealReqDto.setFoodAmount(100);
//
//        // when
//        MealInfoResDto mealInfoResDto = diaryService.createMeal(testPet.getId(), partialMealReqDto);
//
//        // then
//        assertThat(mealInfoResDto.getDescription()).isEqualTo(partialMealReqDto.getDescription());
//        assertThat(mealInfoResDto.getDate()).isEqualTo(partialMealReqDto.getDate());
//        assertThat(mealInfoResDto.getMealType()).isEqualTo(partialMealReqDto.getMealType());
//        assertThat(mealInfoResDto.getFoodBrand()).isEqualTo(partialMealReqDto.getFoodBrand());
//        assertThat(mealInfoResDto.getFoodAmount()).isEqualTo(partialMealReqDto.getFoodAmount());
//    }
//
//    @Test
//    @DisplayName("식사 기록 등록 실패 - 필수 속성 값 없음")
//    void createMealFail() {
//
//        // given : 필수 속성 값이 없을 때
//        PartialMealReqDto partialMealReqDto1 = new PartialMealReqDto();
//        partialMealReqDto1.setDescription("아침에 평소보다 식욕이 좋았습니다. 사료를 거의 다 먹었고 물도 많이 마셨습니다.");
//        partialMealReqDto1.setDate(LocalDate.parse("2025-03-25"));
//        partialMealReqDto1.setFoodBrand("로얄캐닌 미니 어덜트");
//        partialMealReqDto1.setFoodAmount(100);
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createMeal(testPet.getId(), partialMealReqDto1));
//
//
//        // given : 필수 속성 값이 없을 때
//        PartialMealReqDto partialMealReqDto2 = new PartialMealReqDto();
//        partialMealReqDto2.setDescription("아침에 평소보다 식욕이 좋았습니다. 사료를 거의 다 먹었고 물도 많이 마셨습니다.");
//        partialMealReqDto2.setMealType(MealType.BREAKFAST);
//        partialMealReqDto2.setFoodBrand("로얄캐닌 미니 어덜트");
//        partialMealReqDto2.setFoodAmount(100);
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createMeal(testPet.getId(), partialMealReqDto2));
//    }
//
//    @Test
//    @DisplayName("식사 기록 수정 성공")
//    void updateMealSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialMealReqDto partialMealReqDto1 = new PartialMealReqDto();
//        partialMealReqDto1.setDescription("저녁 식사 후 약간의 소화 불량 증상이 있었습니다. 내일부터는 급여량을 조금 줄여볼 예정입니다.");
//        partialMealReqDto1.setDate(LocalDate.parse("2025-03-30"));
//        partialMealReqDto1.setMealType(MealType.DINNER);
//        partialMealReqDto1.setFoodBrand("내추럴발란스 오리지널 울트라");
//        partialMealReqDto1.setFoodAmount(120);
//        // when
//        MealInfoResDto mealInfoResDto1 = diaryService.updateMeal(testMeal.getId(), partialMealReqDto1);
//
//        // then
//        assertThat(mealInfoResDto1.getDescription()).isEqualTo(partialMealReqDto1.getDescription());
//        assertThat(mealInfoResDto1.getDate()).isEqualTo(partialMealReqDto1.getDate());
//        assertThat(mealInfoResDto1.getMealType()).isEqualTo(partialMealReqDto1.getMealType());
//        assertThat(mealInfoResDto1.getFoodBrand()).isEqualTo(partialMealReqDto1.getFoodBrand());
//        assertThat(mealInfoResDto1.getFoodAmount()).isEqualTo(partialMealReqDto1.getFoodAmount());
//    }
//
//    @Test
//    @DisplayName("운동 기록 등록 성공")
//    void createActivitySuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialActivityReqDto partialActivityReqDto = new PartialActivityReqDto();
//        partialActivityReqDto.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialActivityReqDto.setDate(LocalDate.parse("2025-03-25"));
//        partialActivityReqDto.setActivityType(ActivityType.WALK);
//        partialActivityReqDto.setDuration(45);
//        partialActivityReqDto.setDistance(new BigDecimal(2));
//        partialActivityReqDto.setLocation("한강공원");
//
//
//        // when
//        ActivityInfoResDto activityInfoResDto = diaryService.createActivity(testPet.getId(), partialActivityReqDto);
//
//        // then
//        assertThat(activityInfoResDto.getDescription()).isEqualTo(partialActivityReqDto.getDescription());
//        assertThat(activityInfoResDto.getDate()).isEqualTo(partialActivityReqDto.getDate());
//        assertThat(activityInfoResDto.getActivityType()).isEqualTo(partialActivityReqDto.getActivityType());
//        assertThat(activityInfoResDto.getDuration()).isEqualTo(partialActivityReqDto.getDuration());
//        assertThat(activityInfoResDto.getDistance()).isEqualTo(partialActivityReqDto.getDistance());
//        assertThat(activityInfoResDto.getLocation()).isEqualTo(partialActivityReqDto.getLocation());
//    }
//
//    @Test
//    @DisplayName("운동 기록 등록 실패 - 필수 속성 값 없음")
//    void createActivityFail() {
//
//        // given : 필수 속성 값이 없을 때
//        PartialActivityReqDto partialActivityReqDto1 = new PartialActivityReqDto();
//        partialActivityReqDto1.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialActivityReqDto1.setDate(LocalDate.parse("2025-03-25"));
//        partialActivityReqDto1.setDuration(45);
//        partialActivityReqDto1.setDistance(new BigDecimal(2));
//        partialActivityReqDto1.setLocation("한강공원");
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createActivity(testPet.getId(), partialActivityReqDto1));
//
//
//        // given : 필수 속성 값이 없을 때
//        PartialActivityReqDto partialActivityReqDto2 = new PartialActivityReqDto();
//        partialActivityReqDto2.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialActivityReqDto2.setActivityType(ActivityType.WALK);
//        partialActivityReqDto2.setDuration(45);
//        partialActivityReqDto2.setDistance(new BigDecimal(2));
//        partialActivityReqDto2.setLocation("한강공원");
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createActivity(testPet.getId(), partialActivityReqDto2));
//    }
//
//    @Test
//    @DisplayName("운동 기록 수정 성공")
//    void updateActivitySuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialActivityReqDto partialActivityReqDto = new PartialActivityReqDto();
//        partialActivityReqDto.setDescription("기본 복종 훈련을 진행했습니다. '앉아', '기다려' 명령에 잘 반응하기 시작했습니다. 트레이너가 일주일에 2번 정도 훈련을 추천했습니다.");
//        partialActivityReqDto.setDate(LocalDate.parse("2025-03-28"));
//        partialActivityReqDto.setActivityType(ActivityType.TRAINING);
//        partialActivityReqDto.setDuration(30);
//        partialActivityReqDto.setDistance(new BigDecimal(0));
//        partialActivityReqDto.setLocation("펫 트레이닝 센터");
//
//        // when
//        ActivityInfoResDto activityInfoResDto = diaryService.updateActivity(testActivity.getId(), partialActivityReqDto);
//
//        // then
//        assertThat(activityInfoResDto.getDescription()).isEqualTo(partialActivityReqDto.getDescription());
//        assertThat(activityInfoResDto.getDate()).isEqualTo(partialActivityReqDto.getDate());
//        assertThat(activityInfoResDto.getActivityType()).isEqualTo(partialActivityReqDto.getActivityType());
//        assertThat(activityInfoResDto.getDuration()).isEqualTo(partialActivityReqDto.getDuration());
//        assertThat(activityInfoResDto.getDistance()).isEqualTo(partialActivityReqDto.getDistance());
//        assertThat(activityInfoResDto.getLocation()).isEqualTo(partialActivityReqDto.getLocation());
//    }
//
//    @Test
//    @DisplayName("행동 기록 등록 성공")
//    void createBehaviorSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialBehaviorReqDto partialBehaviorReqDto = new PartialBehaviorReqDto();
//        partialBehaviorReqDto.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialBehaviorReqDto.setDate(LocalDate.parse("2025-03-25"));
//        partialBehaviorReqDto.setBehaviorType("분리불안");
//        partialBehaviorReqDto.setBehaviorIntensity(BehaviorIntensity.HIGH);
//
//        // when
//        BehaviorInfoResDto behaviorInfoResDto = diaryService.createBehavior(testPet.getId(), partialBehaviorReqDto);
//
//        // then
//        assertThat(behaviorInfoResDto.getDescription()).isEqualTo(partialBehaviorReqDto.getDescription());
//        assertThat(behaviorInfoResDto.getDate()).isEqualTo(partialBehaviorReqDto.getDate());
//        assertThat(behaviorInfoResDto.getBehaviorType()).isEqualTo(partialBehaviorReqDto.getBehaviorType());
//        assertThat(behaviorInfoResDto.getBehaviorIntensity()).isEqualTo(partialBehaviorReqDto.getBehaviorIntensity());
//    }
//
//    @Test
//    @DisplayName("행동 기록 등록 실패 - 필수 속성 값 없음")
//    void createBehaviorFail() {
//
//        // given : 필수 속성 값이 없을 때
//        PartialBehaviorReqDto partialBehaviorReqDto1 = new PartialBehaviorReqDto();
//        partialBehaviorReqDto1.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialBehaviorReqDto1.setDate(LocalDate.parse("2025-03-25"));
//        partialBehaviorReqDto1.setBehaviorIntensity(BehaviorIntensity.HIGH);
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createBehavior(testPet.getId(), partialBehaviorReqDto1));
//
//
//        // given : 필수 속성 값이 없을 때
//        PartialBehaviorReqDto partialBehaviorReqDto2 = new PartialBehaviorReqDto();
//        partialBehaviorReqDto2.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialBehaviorReqDto2.setDate(LocalDate.parse("2025-03-25"));
//        partialBehaviorReqDto2.setBehaviorType("분리불안");
//
//        // when & then
//        assertThrows(DataIntegrityViolationException.class, () -> diaryService.createBehavior(testPet.getId(), partialBehaviorReqDto2));
//    }
//
//    @Test
//    @DisplayName("행동 기록 수정 성공")
//    void updateBehaviorSuccess() {
//
//        // given : 모든 속성 값 있을 때
//        PartialBehaviorReqDto partialBehaviorReqDto = new PartialBehaviorReqDto();
//        partialBehaviorReqDto.setDescription("오늘 첫 한강 산책을 했습니다. 다른 강아지들과 잘 어울리고 활기차게 놀았습니다. 날씨가 좋아서 평소보다 더 오래 산책했습니다.");
//        partialBehaviorReqDto.setDate(LocalDate.parse("2025-03-25"));
//        partialBehaviorReqDto.setBehaviorType("공격성");
//        partialBehaviorReqDto.setBehaviorIntensity(BehaviorIntensity.MEDIUM);
//
//        // when
//        BehaviorInfoResDto behaviorInfoResDto = diaryService.updateBehavior(testBehavior.getId(), partialBehaviorReqDto);
//
//        // then
//        assertThat(behaviorInfoResDto.getDescription()).isEqualTo(partialBehaviorReqDto.getDescription());
//        assertThat(behaviorInfoResDto.getDate()).isEqualTo(partialBehaviorReqDto.getDate());
//        assertThat(behaviorInfoResDto.getBehaviorType()).isEqualTo(partialBehaviorReqDto.getBehaviorType());
//        assertThat(behaviorInfoResDto.getBehaviorIntensity()).isEqualTo(partialBehaviorReqDto.getBehaviorIntensity());
//    }
//
//    /**
//     * TODO : 테스트코드 작성
//     */
//    @Test
//    @DisplayName("일기 리스트 조회(페이징) 성공")
//    void getDiaryListSuccess() {}
//
//    @Test
//    @DisplayName("일기 삭제 성공")
//    void deleteDiarySuccess() {}
//
//}