package com.myproject.petcare.pet_diary.user.service;

import com.myproject.petcare.pet_diary.auth.service.AuthService;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.InvalidPasswordException;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetailsService;
import com.myproject.petcare.pet_diary.user.dto.CheckPasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdatePasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdateUserReqDto;
import com.myproject.petcare.pet_diary.user.dto.UserInfoResDto;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Slf4j
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User testUser;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void before(){
        testUser = new User();
        testUser.setEmail("test2@gmail.com");
        testUser.setPassword(bCryptPasswordEncoder.encode("TestPassword2!!"));
        testUser.setName("테스트유저1");
        testUser.setPhone("010-1234-1234");
        testUser.setRole(Role.USER);
        userRepository.save(testUser);

        customUserDetails = new CustomUserDetails(testUser);
    }

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getUserSuccess() {
        // given

        // when
        UserInfoResDto userInfoResDto = userService.getUser(customUserDetails);

        // then
        assertThat(userInfoResDto.getName()).isEqualTo(testUser.getName());
        assertThat(userInfoResDto.getPhone()).isEqualTo(testUser.getPhone());
        assertThat(userInfoResDto.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutSuccess() {
        // given

        // when
        userService.logout(customUserDetails);

        // then
        assertThat(testUser).isNotNull();
        assertThat(testUser.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateUserSuccess() {
        // given : 회원 정보 수정 데이터 준비
        UpdateUserReqDto updateUserReqDto = new UpdateUserReqDto();
        updateUserReqDto.setName("김승현");
        updateUserReqDto.setPhone("010-3304-2341");

        // when
        UserInfoResDto userInfoResDto = userService.updateUser(updateUserReqDto, customUserDetails);

        // then
        assertThat(userInfoResDto.getName()).isEqualTo(updateUserReqDto.getName());
        assertThat(userInfoResDto.getPhone()).isEqualTo(updateUserReqDto.getPhone());

        assertThat(userInfoResDto.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("비밀번호 동일 여부 확인 성공")
    void checkPasswordSuccess() {
        // given : 비밀번호 동일 여부 확인 데이터 준비
        CheckPasswordReqDto checkPasswordReqDto = new CheckPasswordReqDto();
        checkPasswordReqDto.setCurrentPassword("TestPassword2!!");

        // when
        boolean isPasswordEqual = userService.checkPassword(checkPasswordReqDto, customUserDetails);

        // then
        assertThat(isPasswordEqual).isTrue();
    }

    @Test
    @DisplayName("비밀번호 동일 여부 확인 실패")
    void checkPasswordFail() {
        // given : 비밀번호 동일 여부 확인 데이터 준비
        CheckPasswordReqDto checkPasswordReqDto = new CheckPasswordReqDto();
        checkPasswordReqDto.setCurrentPassword("wrongPW!!");

        // when && then
        InvalidPasswordException invalidPasswordException = assertThrows(InvalidPasswordException.class, () -> userService.checkPassword(checkPasswordReqDto, customUserDetails));
        assertThat(invalidPasswordException.getMessage()).isEqualTo("비밀번호를 다시 확인해 주세요.");
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void updatePasswordSuccess() {
        // given : 비밀번호 동일 여부 확인 데이터 준비
        UpdatePasswordReqDto updatePasswordReqDto = new UpdatePasswordReqDto();
        updatePasswordReqDto.setCurrentPassword("TestPassword2!!");
        updatePasswordReqDto.setChangedPassword("changedPW1!!");
        updatePasswordReqDto.setChangedPasswordCheck("changedPW1!!");

        // when
        userService.updatePassword(updatePasswordReqDto, customUserDetails);

        // then
        User passwordChangeUser = userRepository.findById(testUser.getId()).orElse(null);

        assertThat(bCryptPasswordEncoder.matches(updatePasswordReqDto.getChangedPassword(), passwordChangeUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("비밀번호 변경 실패")
    void updatePasswordFail() {
        // given : 비밀번호 동일 여부 확인 데이터 준비
        UpdatePasswordReqDto updatePasswordReqDto = new UpdatePasswordReqDto();
        updatePasswordReqDto.setCurrentPassword("TestPassword2!!");
        updatePasswordReqDto.setChangedPassword("changedPW1!!");
        updatePasswordReqDto.setChangedPasswordCheck("differentPW1!!");

        // when && then
        InvalidPasswordException invalidPasswordException = assertThrows(InvalidPasswordException.class, () -> userService.updatePassword(updatePasswordReqDto, customUserDetails));
        assertThat(invalidPasswordException.getMessage()).isEqualTo("변경될 비밀번호가 동일하지 않습니다.");
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteUserSuccess() {
        // given

        // when
        userService.deleteUser(customUserDetails);

        // then
        assertThat(userRepository.findById(testUser.getId()).orElse(null)).isNull();
    }
}