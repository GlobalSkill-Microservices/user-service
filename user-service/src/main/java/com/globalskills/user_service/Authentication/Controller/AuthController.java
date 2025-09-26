package com.globalskills.user_service.Authentication.Controller;

import com.globalskills.user_service.Authentication.Dto.LoginRequest;
import com.globalskills.user_service.Authentication.Dto.LoginResponse;
import com.globalskills.user_service.Authentication.Dto.RegisterRequest;
import com.globalskills.user_service.Authentication.Service.AuthService;
import com.globalskills.user_service.Common.BaseResponseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    AuthService authService;

    public ResponseEntity<?> login (@RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        BaseResponseAPI<LoginResponse> responseAPI = new BaseResponseAPI<>(true,"Login successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    public ResponseEntity<?> register (@RequestBody RegisterRequest request){
        authService.register(request);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Register successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }
}
