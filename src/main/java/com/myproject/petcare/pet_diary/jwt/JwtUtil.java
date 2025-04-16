package com.myproject.petcare.pet_diary.jwt;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.ExpiredTokenException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Getter
public class JwtUtil {

    private final SecretKeySpec secretKey;
    private final Long accessTokenExpTime;
    private final Long refreshTokenExpTime;

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey,
                   @Value("${spring.jwt.access-token-expiration-time}") long accessTokenExpTime,
                   @Value("${spring.jwt.refresh-token-expiration-time}") long refreshTokenExpTime
                    ) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public Long getId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("만료된 토큰입니다.");
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않는 토큰입니다.");
        }

    }

    public String createAccessToken(Long id, String role) {
        return Jwts.builder()
                .claim("id", id)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpTime))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long id) {
        return Jwts.builder()
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpTime))
                .signWith(secretKey)
                .compact();
    }
}
