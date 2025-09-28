package com.globalskills.user_service.Account.Controller;

import com.globalskills.user_service.Account.Dto.AccountRequest;
import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Service.AccountCommandService;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import com.globalskills.user_service.Common.Dto.BaseResponseAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    AccountCommandService accountCommandService;

    @Autowired
    AccountQueryService accountQueryService;

    public ResponseEntity<?> save(Account account){
        AccountResponse newAccount = accountCommandService.save(account);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Create account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

    public ResponseEntity<?> update(@RequestBody AccountRequest request,@RequestHeader("X-User-ID") Long currentAccountId){
        AccountResponse updateAccount = accountCommandService.update(request, currentAccountId);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account successfully",updateAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

    public ResponseEntity<?> create(@RequestBody AccountRequest request,@RequestParam String accountRole ,@RequestHeader("X-User-Role") String authRole){
        AccountResponse newAccount = accountCommandService.create(request, accountRole, authRole);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Create account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

    public ResponseEntity<?> update(@RequestBody AccountRequest request,@RequestParam String accountRole,@RequestParam Long accountId,@RequestHeader("X-User-Role") String authRole){
        AccountResponse newAccount = accountCommandService.update(request, accountRole, accountId, authRole);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

    public ResponseEntity<?> delete(@RequestParam Long accountId){
        accountCommandService.delete(accountId);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Delete account successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

}
