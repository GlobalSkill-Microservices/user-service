package com.globalskills.user_service.Authentication.Service;

import com.globalskills.user_service.Account.Dto.EmailDto;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import com.globalskills.user_service.Account.Service.EmailService;
import com.globalskills.user_service.Authentication.Dto.LoginRequest;
import com.globalskills.user_service.Authentication.Dto.LoginResponse;
import com.globalskills.user_service.Authentication.Dto.RegisterRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    EmailService emailService;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ModelMapper modelMapper;

    public void register (RegisterRequest request){
        Account account = modelMapper.map(request, Account.class);
        if(!Objects.equals(request.getConfirmPassword(), request.getPassword())){
            try{
                String originPassword = account.getPassword();
                account.setPassword(passwordEncoder.encode(originPassword));
                account.setAccountRole(AccountRole.USER);
                accountRepo.save(account);

                EmailDto emailDto = new EmailDto();
                emailDto.setAccount(account);
                emailDto.setSubject("Verify your email");
                emailDto.setLink("#");
                emailDto.setCreatedDate(new Date());
                boolean emailSent = emailService.sendEmailVerify(emailDto);
                if(emailSent){
                    account.setIsActive(true);
                    accountRepo.save(account);
                }
            }catch (Exception e){
                if(e.getMessage().contains(account.getEmail())){
                    throw new AccountException("Duplicate email",HttpStatus.CONFLICT);
                }
            }
        }else {
            throw new AccountException("Password not match", HttpStatus.BAD_REQUEST);
        }
    }

    public LoginResponse login (LoginRequest request){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
            Account account = (Account) authentication.getPrincipal();
            LoginResponse loginResponse = modelMapper.map(account, LoginResponse.class);
            loginResponse.setToken(jwtService.generateToken(account));
            return loginResponse;
        }catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("UserName or password invalid!");
        }
    }

}
