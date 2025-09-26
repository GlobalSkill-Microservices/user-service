package com.globalskills.user_service.Authentication.Service;

import com.globalskills.user_service.Account.Entity.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${secret_key}")
    private String KEY_SECRET;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private SecretKey getSignKey() {
        //dung kieu raw text
        byte[] keyBytes = KEY_SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Account account){
        return Jwts.builder()
                .subject(account.getId() + "")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractAccountId(String token) {
        return Long.parseLong(parseToken(token).getSubject().trim());
    }


}
