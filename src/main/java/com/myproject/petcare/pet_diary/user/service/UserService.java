package com.myproject.petcare.pet_diary.user.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.InvalidPasswordException;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.user.dto.CheckPasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdatePasswordReqDto;
import com.myproject.petcare.pet_diary.user.dto.UpdateUserReqDto;
import com.myproject.petcare.pet_diary.user.dto.UserInfoResDto;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserInfoResDto getUser(CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);

        // 강아지 정보 포함하여 리턴
        List<Pet> pets = user.getPets();
        return new UserInfoResDto(user.getEmail(), user.getName(), user.getPhone(), pets);
    }

    @Transactional
    public void logout(CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);
        user.setRefreshToken(null);
    }

    @Transactional
    public UserInfoResDto updateUser(UpdateUserReqDto updateUserReqDto, CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);

        if (StringUtils.hasText(updateUserReqDto.getName())) {
            user.setName(updateUserReqDto.getName());
        }

        if (StringUtils.hasText(updateUserReqDto.getPhone())) {
            user.setPhone(updateUserReqDto.getPhone());
        }

        return new UserInfoResDto(user.getEmail(), user.getName(), user.getPhone());
    }

    public boolean checkPassword(CheckPasswordReqDto checkPasswordReqDto, CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);

        // 현재 비밀번호가 DB에 저장된 비밀번호와 같은지 확인
        if (!bCryptPasswordEncoder.matches(checkPasswordReqDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호를 다시 확인해 주세요.");
        }

        return true;
    }

    @Transactional
    public void updatePassword(UpdatePasswordReqDto updatePasswordReqDto, CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);

        // 현재 비밀번호가 DB에 저장된 비밀번호와 같은지 확인
        if (!bCryptPasswordEncoder.matches(updatePasswordReqDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호를 다시 확인해 주세요.");
        }

        // 바뀔 비밀번호의 동일 입력 여부 확인
        if (!updatePasswordReqDto.getChangedPassword().equals(updatePasswordReqDto.getChangedPasswordCheck())) {
            throw new InvalidPasswordException("변경될 비밀번호가 동일하지 않습니다.");
        }

        user.setPassword(bCryptPasswordEncoder.encode(updatePasswordReqDto.getChangedPassword()));
    }

    @Transactional
    public void deleteUser(CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);
        userRepository.delete(user);
    }

    private User getUserFromUserDetails(CustomUserDetails customUserDetails) {
        Long id = Long.valueOf(customUserDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
}
