package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import com.globalskills.user_service.Common.Dto.PageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountQueryService{

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    ModelMapper modelMapper;


    public Account findAccountById(Long id){
        return accountRepo.findById(id).orElseThrow(()->new AccountException("Account not found", HttpStatus.NOT_FOUND));
    }

    public Account findAccountByUsername(String username){
        return accountRepo.findByUsername(username).orElseThrow(()-> new AccountException("Account not found",HttpStatus.NOT_FOUND));
    }

    public boolean findAccountByEmailOrUsername(String email,String username){
        return accountRepo.existsByEmailOrUsername(email, username);
    }

    public Account findAccountByEmail(String email){
        return accountRepo.findByEmail(email).orElseThrow(()-> new AccountException("Account not found",HttpStatus.NOT_FOUND));
    }



    public AccountResponse getCurrentUser(Long id){
        Account account = findAccountById(id);
        return modelMapper.map(account, AccountResponse.class);
    }


    public AccountResponse getAccountById(Long id){
        Account account = findAccountById(id);
        return modelMapper.map(account,AccountResponse.class);
    }

    public PageResponse<AccountResponse> getListAccount(
            int page,
            int size,
            String sortBy,
            String sortDir,
            Boolean isActive
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Account> accountPage = (isActive == null)
                ? accountRepo.findAll(pageRequest)
                : accountRepo.findAllByIsActive(pageRequest, isActive);
        if (accountPage.isEmpty()) {
            return null;
        }

        List<AccountResponse> responses = accountPage
                .stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();

        return new PageResponse<>(
                responses,
                page,
                size,
                accountPage.getTotalElements(),
                accountPage.getTotalPages(),
                accountPage.isLast()
        );
    }
}
