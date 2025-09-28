package com.globalskills.user_service.Authentication.Service;

import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Service.AccountCommandService;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import com.globalskills.user_service.Authentication.Dto.LoginRequest;
import com.globalskills.user_service.Authentication.Dto.LoginResponse;
import com.globalskills.user_service.Common.Dto.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountQueryService accountQueryService;

    @Autowired
    AccountCommandService accountCommandService;

    @Autowired
    JwtService jwtService;

    public LoginResponse login (LoginRequest request){
        Account account = accountQueryService.findAccountByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(account);
        LoginResponse response = modelMapper.map(account, LoginResponse.class);
        response.setAccess_token(accessToken);
        return response;
    }

    public LoginResponse register(RegisterRequest request){
        Account account = modelMapper.map(request, Account.class);
        accountCommandService.save(account);
        return modelMapper.map(account, LoginResponse.class);
    }



}
