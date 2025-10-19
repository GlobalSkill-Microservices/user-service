package com.globalskills.user_service.Account.Controller;

import com.globalskills.user_service.Account.Dto.AccountRequest;
import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Dto.CvListApproved;
import com.globalskills.user_service.Account.Service.AccountCommandService;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import com.globalskills.user_service.Authentication.Dto.ResetPasswordRequest;
import com.globalskills.user_service.Common.Dto.BaseResponseAPI;
import com.globalskills.user_service.Common.Dto.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "api")
public class AccountController {

    @Autowired
    AccountCommandService accountCommandService;

    @Autowired
    AccountQueryService accountQueryService;

//    public ResponseEntity<?> save(Account account){
//        AccountResponse newAccount = accountCommandService.save(account);
//        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Create account successfully",newAccount,null);
//        return ResponseEntity.ok(responseAPI);
//    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changPassword(@Parameter(hidden = true)
                                           @RequestHeader(value = "X-User-ID",required = false) Long currentAccountId,
                                           @RequestBody ResetPasswordRequest request){
        accountCommandService.changePassword(currentAccountId, request);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Change password successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }


    @PutMapping("/me/role-teacher")
    public ResponseEntity<?> updateRole( @Parameter(hidden = true)
                                         @RequestHeader(value = "X-User-ID",required = false) Long currentAccountId){
        AccountResponse response = accountCommandService.updateRole(currentAccountId);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account role to TEACHER successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody AccountRequest request){
        AccountResponse newAccount = accountCommandService.create(request);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Create account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }


    @PutMapping("/me")
    public ResponseEntity<?> update(@RequestBody AccountRequest request,
                                    @Parameter(hidden = true)
                                    @RequestHeader(value = "X-User-ID",required = false) Long currentAccountId){
        AccountResponse updateAccount = accountCommandService.update(request, currentAccountId);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account successfully",updateAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateByUserId(@RequestBody AccountRequest request,@PathVariable Long id){
        AccountResponse newAccount = accountCommandService.update(request, id);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Update account successfully",newAccount,null);
        return ResponseEntity.ok(responseAPI);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        accountCommandService.delete(id);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Delete account successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> active(@PathVariable Long id){
        accountCommandService.activeAccount(id);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Restore account successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        AccountResponse response = accountQueryService.getAccountById(id);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Get account successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAccount(
            @Parameter(hidden = true)
            @RequestHeader(value = "X-User-ID",required = false)Long id){
        AccountResponse response = accountQueryService.getCurrentUser(id);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Get current account successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping
    public ResponseEntity<?> getListAccount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Boolean isActive
    ){
        PageResponse<AccountResponse> pageResponse = accountQueryService.getListAccount(page, size, sortBy, sortDir, isActive);
        BaseResponseAPI<PageResponse<AccountResponse>> responseAPI = new BaseResponseAPI<>(true,"Get list account successfully",pageResponse,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping("/approvedCv")
    public ResponseEntity<?> approvedCv(@RequestBody CvListApproved listApproved){
        boolean response = accountCommandService.approvedCv(listApproved);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Approved successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping("/rejectCv")
    public ResponseEntity<?> rejectCv(@RequestBody CvListApproved listReject){
        boolean response = accountCommandService.rejectCV(listReject);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Reject successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @PutMapping(value = "/cvUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cvUpload(MultipartFile file,
                                      @Parameter(hidden = true)
                                      @RequestHeader(value = "X-User-ID",required = false)
                                      Long accountId)throws IOException {
        AccountResponse response = accountCommandService.cvUpload(file, accountId);
        BaseResponseAPI<AccountResponse> responseAPI = new BaseResponseAPI<>(true,"Cv upload successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/cv")
    public ResponseEntity<?> getListAccountApproveCV(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ){
        PageResponse<AccountResponse> pageResponse = accountQueryService.getListAccountApproveCV(page, size, sortBy, sortDir);
        BaseResponseAPI<PageResponse<AccountResponse>> responseAPI = new BaseResponseAPI<>(true,"Get list account cv successfully",pageResponse,null);
        return ResponseEntity.ok(responseAPI);
    }

}
