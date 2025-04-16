package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.PartialPetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor
@Transactional
class PetServiceTest {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    private User testUser;
    private Pet testPet1;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void before() {
        testUser = new User();
        testUser.setEmail("test2@gmail.com");
        testUser.setPassword(bCryptPasswordEncoder.encode("TestPassword2!!"));
        testUser.setName("테스트유저1");
        testUser.setPhone("010-1234-1234");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        customUserDetails = new CustomUserDetails(testUser);

        testPet1 = new Pet();
        testPet1.setName("멍멍이1");
        testPet1.setBreed("포메라니안");
        testPet1.setBirthDate(LocalDate.parse("1993-10-20"));
        testPet1.setGender(Gender.FEMALE);
        testPet1.setWeight(new BigDecimal("5.23"));
        testPet1.setDescription(null);
        testPet1.changeUser(testUser);
        petRepository.save(testPet1);

        Pet testPet2 = new Pet();
        testPet2.setName("멍멍이2");
        testPet2.setBreed("포메라니안");
        testPet2.setBirthDate(LocalDate.parse("1993-11-10"));
        testPet2.setGender(Gender.MALE);
        testPet2.setWeight(new BigDecimal("3.23"));
        testPet2.setDescription(null);
        testPet2.changeUser(testUser);
        petRepository.save(testPet2);
    }

    @Test
    @DisplayName("반려견 등록 성공")
    void createPetSuccess() {
        // given
        PartialPetReqDto partialPetReqDto = new PartialPetReqDto();
        partialPetReqDto.setName("나비");
        partialPetReqDto.setBreed("푸들");
        partialPetReqDto.setBirthDate(LocalDate.parse("1989-09-14"));
        partialPetReqDto.setGender(Gender.MALE);
        partialPetReqDto.setWeight(new BigDecimal("10.23"));
        partialPetReqDto.setDescription("예민함");

        // when
        PetInfoResDto petInfoResDto = petService.createPet(partialPetReqDto, customUserDetails);

        // then
        assertThat(petInfoResDto.getName()).isEqualTo(partialPetReqDto.getName());
        assertThat(petInfoResDto.getBreed()).isEqualTo(partialPetReqDto.getBreed());
        assertThat(petInfoResDto.getBirthDate()).isEqualTo(partialPetReqDto.getBirthDate());
        assertThat(petInfoResDto.getGender()).isEqualTo(partialPetReqDto.getGender());
        assertThat(petInfoResDto.getWeight()).isEqualTo(partialPetReqDto.getWeight());
        assertThat(petInfoResDto.getDescription()).isEqualTo(partialPetReqDto.getDescription());
    }

    @Test
    @DisplayName("반려견 단일 조회 성공")
    void getPetSuccess() {
        // given

        // when
        PetInfoResDto petInfoResDto = petService.getPet(testPet1.getId());

        //then
        assertThat(petInfoResDto.getName()).isEqualTo(testPet1.getName());
        assertThat(petInfoResDto.getBreed()).isEqualTo(testPet1.getBreed());
        assertThat(petInfoResDto.getBirthDate()).isEqualTo(testPet1.getBirthDate());
        assertThat(petInfoResDto.getGender()).isEqualTo(testPet1.getGender());
        assertThat(petInfoResDto.getWeight()).isEqualTo(testPet1.getWeight());
        assertThat(petInfoResDto.getDescription()).isEqualTo(testPet1.getDescription());
    }

    @Test
    @DisplayName("반려견 단일 조회 실패")
    void getPetFail() {
        // given

        // when & then
        assertThrows(NotFoundException.class, () -> petService.getPet(13L));
    }

    @Test
    @DisplayName("반려견 복수 조회 성공")
    void getPetsSuccess() {
        // given

        // when
        List<PetInfoResDto> pets = petService.getPets(customUserDetails);

        //then
        PetInfoResDto petInfoResDto = pets.get(0);

        assertThat(pets.size()).isEqualTo(2);
        assertThat(petInfoResDto.getName()).isEqualTo(testPet1.getName());
        assertThat(petInfoResDto.getBreed()).isEqualTo(testPet1.getBreed());
        assertThat(petInfoResDto.getBirthDate()).isEqualTo(testPet1.getBirthDate());
        assertThat(petInfoResDto.getGender()).isEqualTo(testPet1.getGender());
        assertThat(petInfoResDto.getWeight()).isEqualTo(testPet1.getWeight());
        assertThat(petInfoResDto.getDescription()).isEqualTo(testPet1.getDescription());
    }

    @Test
    @DisplayName("반려견 정보 수정 성공")
    void updatePetSuccess() {
        // given
        PartialPetReqDto partialPetReqDto = new PartialPetReqDto();
        partialPetReqDto.setName("나비");
        partialPetReqDto.setBreed("푸들");
        partialPetReqDto.setBirthDate(LocalDate.parse("1989-09-14"));
        partialPetReqDto.setGender(Gender.MALE);
        partialPetReqDto.setWeight(new BigDecimal("10.23"));
        partialPetReqDto.setDescription("예민함");

        // when
        PetInfoResDto petInfoResDto = petService.updatePet(testPet1.getId(), partialPetReqDto);

        // then
        assertThat(petInfoResDto.getName()).isEqualTo(partialPetReqDto.getName());
        assertThat(petInfoResDto.getBreed()).isEqualTo(partialPetReqDto.getBreed());
        assertThat(petInfoResDto.getBirthDate()).isEqualTo(partialPetReqDto.getBirthDate());
        assertThat(petInfoResDto.getGender()).isEqualTo(partialPetReqDto.getGender());
        assertThat(petInfoResDto.getWeight()).isEqualTo(partialPetReqDto.getWeight());
        assertThat(petInfoResDto.getDescription()).isEqualTo(partialPetReqDto.getDescription());
    }

    @Test
    @DisplayName("반려견 정보 삭제 성공")
    void deletePetSuccess() {
        // given

        // when
        petService.deletePet(testPet1.getId(), customUserDetails);

        //then
        assertThrows(NotFoundException.class, () -> petService.getPet(testPet1.getId()));
    }

}