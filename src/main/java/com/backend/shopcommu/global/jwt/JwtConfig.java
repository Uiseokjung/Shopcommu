package com.backend.shopcommu.global.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.secretKey}")
    private String secretKeyPlain;

    @Bean
    public SecretKey jwtSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyPlain);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
