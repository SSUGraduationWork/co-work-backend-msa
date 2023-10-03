package com.example.userservice.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {

    private Environment env;

    /**
     * JWT 생성
     * @param userId
     * @return String
     */
    public String createJwt(Long userId, String role){
        Date now = new Date();
        String jwt =  Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))  //signature 부분
                .compact();
        return jwt;
    }
}
