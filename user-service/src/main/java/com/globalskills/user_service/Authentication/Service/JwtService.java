package com.globalskills.user_service.Authentication.Service;

import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Autowired
    AccountQueryService accountQueryService;

    public final String KEY_SECRET="c4a7e5bc8d9a53b249f9e7a3eb8d44f4c281cfe6b327f0b2f86f5c9a7e3408d1";
    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(KEY_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //tạo token
    public String generateToken(Account account){
        return Jwts.builder()
                .subject(account.getId() + "")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 1000*60*60*24))
                .signWith(getSignKey())
                .compact();
    }

    //xác thực token
    public Account getUserByToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String idString = claims.getSubject().trim();
        long id = Long.parseLong(idString);
        return accountQueryService.findAccountById(id);
    }

}
