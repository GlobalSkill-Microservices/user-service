package com.globalskills.user_service.Account.Controller;

import com.globalskills.user_service.Account.Dto.AccountRequest;
import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Service.AccountCommandService;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import com.globalskills.user_service.Common.Dto.BaseResponseAPI;
import com.globalskills.user_service.Common.Dto.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class AccountController {

    @Autowired
    AccountCommandService accountCommandService;

    @Autowired
    AccountQueryService accountQueryService;

//    @PostMapping("/save")
    public ResponseEntity<?> save(Account account){
        AccountResponse newAccount = accountCommandService.save(account);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Create account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

//    @PutMapping("header = X-User-ID")
    public ResponseEntity<?> update(@RequestBody AccountRequest request,@RequestHeader("X-User-ID") Long currentAccountId){
        AccountResponse updateAccount = accountCommandService.update(request, currentAccountId);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account successfully",updateAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

//    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountRequest request,@RequestParam String accountRole ,@RequestHeader("X-User-Role") String authRole){
        AccountResponse newAccount = accountCommandService.create(request, accountRole, authRole);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Create account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

//    @PutMapping("/authRole/{id}")
    public ResponseEntity<?> update(@RequestBody AccountRequest request,@RequestParam String accountRole,@RequestParam Long accountId,@RequestHeader("X-User-Role") String authRole){
        AccountResponse newAccount = accountCommandService.update(request, accountRole, accountId, authRole);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

//    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> delete(@RequestParam Long accountId){
        accountCommandService.delete(accountId);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Delete account successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        AccountResponse response = accountQueryService.getAccountById(id);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Get account successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAccount(HttpServletRequest request){
        AccountResponse response = accountQueryService.getCurrentUser(request);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Get current account successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping
    public ResponseEntity<?> getListAccount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "null") Boolean isActive
    ){
        PageResponse<AccountResponse> pageResponse = accountQueryService.getListAccount(page, size, sortBy, sortDir, isActive);
        BaseResponseAPI<PageResponse<AccountResponse>> responseAPI = new BaseResponseAPI<>(true,"Get list account successfully",pageResponse,null);
        return ResponseEntity.ok(responseAPI);
    }

}
