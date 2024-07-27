package com.backend.shopcommu.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final SecretKey jwtSecretKey;

    private SecretKey getSecretKey() {
        return jwtSecretKey;
    }

    // 토큰이 시크릿키로 분해되는 함수인지 확인
    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey()).build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // 토큰으로부터 정보 얻기
    public Map<String, Object> getClaims(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey()).build()
                .parseClaimsJws(token)
                .getBody();

        // Claims를 Map으로 변환
        return claims;
    }

    // 정보와 시크릿 키, 시간을 넣어 압축해 토큰 생성
    public String generateAccessToken(Map<String, Object> claims, long seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }
}
