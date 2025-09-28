package com.globalskills.user_service.Authentication.Controller;

import com.globalskills.user_service.Authentication.Dto.LoginRequest;
import com.globalskills.user_service.Authentication.Dto.LoginResponse;
import com.globalskills.user_service.Authentication.Service.AuthenticationService;
import com.globalskills.user_service.Common.Dto.BaseResponseAPI;
import com.globalskills.user_service.Common.Dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request){
        LoginResponse response = authenticationService.login(request);
        BaseResponseAPI<LoginResponse> responseAPI = new BaseResponseAPI<>(true,"Login successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest request){
        LoginResponse response = authenticationService.register(request);
        BaseResponseAPI<LoginResponse> responseAPI = new BaseResponseAPI<>(true,"Register successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }
}
