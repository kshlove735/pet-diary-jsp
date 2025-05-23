package com.myproject.petcare.pet_diary.auth.service;

import com.myproject.petcare.pet_diary.auth.dto.LoginReqDto;
import com.myproject.petcare.pet_diary.auth.dto.LoginResDto;
import com.myproject.petcare.pet_diary.auth.dto.SignupReqDto;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.*;
import com.myproject.petcare.pet_diary.jwt.JwtUtil;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.enums.Role;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public boolean isEmailTaken(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void signup(SignupReqDto signupReqDto) {

        // 이메일 중복 검증
        if (isEmailTaken(signupReqDto.getEmail())) {
            throw new EmailDuplicationException("이미 사용 중인 이메일입니다.");
        }

        User user = new User();
        user.setEmail(signupReqDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(signupReqDto.getPassword()));
        user.setName(signupReqDto.getName());
        user.setPhone(signupReqDto.getPhone());
        user.setRole(Role.USER); // TODO : 유저, 관리자 권한에 따른 동적 회원 가입

        // 회원 정보 DB 저장
        userRepository.save(user);
    }


    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) {
        User findUser = userRepository.findByEmail(loginReqDto.getEmail()).orElse(null);

        // DB에 저장된 회원인지 여부 검증
        if (findUser == null) {
            throw new EmailNotFoundException("등록된 이메일이 없습니다.");
        }

        // password 검증
        if (!bCryptPasswordEncoder.matches(loginReqDto.getPassword(), findUser.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 발급
        String accessToken = jwtUtil.createAccessToken(findUser.getId(), findUser.getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(findUser.getId());

        // refresh token DB에 저장
        findUser.setRefreshToken(refreshToken);

        // return JWT 토큰
        return new LoginResDto(accessToken, refreshToken);
    }

    @Transactional
    public String refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new TokenNotFoundException("Refresh token이 없습니다.");
        }

        if (jwtUtil.isExpired(refreshToken)) {
            throw new ExpiredTokenException("Refresh token이 만료되었습니다.");
        }

        Long id = jwtUtil.getId(refreshToken);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new NotFoundException("유효하지 않은 Refresh token입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(id, String.valueOf(user.getRole()));
        user.setRefreshToken(accessToken);

        return accessToken;
    }
}
