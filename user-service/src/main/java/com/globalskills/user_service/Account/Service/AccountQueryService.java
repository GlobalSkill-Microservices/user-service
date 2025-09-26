package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import com.globalskills.user_service.Authentication.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountQueryService{

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    JwtService jwtService;

    public Account getAccountByToken(String token){
        Long id = jwtService.extractAccountId(token);
        return findAccountById(id);
    }

    public Account findAccountById(Long id){
        return accountRepo.findById(id).orElseThrow(()->new AccountException("Account not found", HttpStatus.NOT_FOUND));
    }

    public Account getCurrentUser(HttpServletRequest request){
        String userIdHeader = request.getHeader("X-Account-Id");
        if (userIdHeader == null){
            throw new AccountException("Can't found user ID",HttpStatus.NOT_FOUND);
        }
        Long userId = Long.valueOf(userIdHeader);
        return findAccountById(userId);
    }


}
