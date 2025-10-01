package com.globalskills.user_service.Authentication.Service;

import com.globalskills.user_service.Account.Dto.EmailDto;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Service.AccountCommandService;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import com.globalskills.user_service.Account.Service.EmailService;
import com.globalskills.user_service.Authentication.Dto.LoginRequest;
import com.globalskills.user_service.Authentication.Dto.LoginResponse;
import com.globalskills.user_service.Common.Dto.RegisterRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Autowired
    EmailService emailService;

    public LoginResponse login (LoginRequest request){
        Account account = accountQueryService.findAccountByUsername(request.getUsername());
        boolean checkPassword = BCrypt.checkpw(request.getPassword(),account.getPassword());
        if(checkPassword){
            String accessToken = jwtService.generateToken(account);
            LoginResponse response = modelMapper.map(account, LoginResponse.class);
            response.setAccess_token(accessToken);
            return response;
        }else{
            throw new AccountException("Login fail",HttpStatus.NOT_FOUND);
        }

    }

    public LoginResponse register(RegisterRequest request){
        boolean existAccount = accountQueryService.findAccountByEmailOrUsername(request.getUsername(), request.getEmail());
        if(!existAccount){
            Account newAccount = modelMapper.map(request, Account.class);
            String hashPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10));
            newAccount.setPassword(hashPassword);
            newAccount.setAccountRole(AccountRole.USER);
            accountCommandService.save(newAccount);
            EmailDto emailDto = new EmailDto();
            emailDto.setAccount(newAccount);
            emailDto.setSubject("Verify your email");
            emailDto.setLink("#");
            emailDto.setCreatedDate(new Date());
            boolean emailSend = emailService.sendEmailVerify(emailDto);
            if(emailSend){
                newAccount.setIsActive(true);
            }
            return modelMapper.map(newAccount, LoginResponse.class);

        }else{
            throw new AccountException("Account has been already exist", HttpStatus.BAD_REQUEST);
        }
    }

    public void forgotPassword(String email){
        Account account = accountQueryService.findAccountByEmail(email);
        String token = jwtService.generateToken(account);
        EmailDto emailDto = new EmailDto();
        emailDto.setAccount(account);
        emailDto.setSubject("Reset password");
        emailDto.setLink("http://localhost:5173/verify-otp/"+ token);
        emailDto.setCreatedDate(new Date());
        emailService.sendEmailResetPassword(emailDto);
    }

    public void resetPassword(Long accountId, String password){
        Account account = accountQueryService.findAccountById(accountId);
        account.setPassword(BCrypt.hashpw(password,BCrypt.gensalt(10)));
        try{
            accountCommandService.save(account);
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
    }



}
