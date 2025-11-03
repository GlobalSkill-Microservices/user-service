package com.globalskills.user_service.Account.Controller;

import com.globalskills.user_service.Account.Dto.AccountDto;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.ApplicationStatus;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-client")
@SecurityRequirement(name = "api")
public class AccountClientController {

    @Autowired
    AccountQueryService accountQueryService;

    @Autowired
    ModelMapper modelMapper;


    @PutMapping("/Cv-status/user/{id}")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long id){
        Account account = accountQueryService.findAccountById(id);
        account.setApplicationStatus(ApplicationStatus.PENDING);
        return ResponseEntity.ok("Update Cv status pending successfully");

    }

    @GetMapping("/batch")
    public ResponseEntity<?> getAccountByIds(@RequestParam("ids") List<Long> ids){
        List<Account> accountList = accountQueryService.findAllAccountById(ids);

        List<AccountDto> responses = accountList
                .stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        Account account = accountQueryService.findAccountById(id);
        AccountDto response = modelMapper.map(account, AccountDto.class);
        return ResponseEntity.ok(response);
    }

}
