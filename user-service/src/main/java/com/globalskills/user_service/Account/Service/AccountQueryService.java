package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Enum.ApplicationStatus;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class AccountQueryService{

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    ModelMapper modelMapper;

    public boolean checkActiveAccount(Long id){
        Account account = findAccountById(id);
        if(account.getIsActive()){
            return true;
        }
        throw new AccountException("Your account need to update gmail",HttpStatus.BAD_REQUEST);
    }

    public List<Account> findAllAccountById(List<Long> list){
        return accountRepo.findAllById(list);
    }

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

    @Transactional(readOnly = true)
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
                ? accountRepo.findAllByAccountRoleNot(pageRequest,AccountRole.ADMIN)
                : accountRepo.findAllByIsActiveAndAccountRoleNot(pageRequest, isActive,AccountRole.ADMIN);
        if (accountPage.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(),page,size,0,0,true);
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

    @Transactional(readOnly = true)
    public PageResponse<AccountResponse> getAccountsByCvApplicationStatus(
            int page,
            int size,
            String sortBy,
            String sortDir,
            ApplicationStatus applicationStatus
    ){
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Account> accountPage = accountRepo.findByProfileCvUrlIsNotNullAndApplicationStatus(pageRequest,applicationStatus);

        if(accountPage.isEmpty()){
            return new PageResponse<>(Collections.emptyList(),page,size,0,0,true);
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

    public boolean isRoleTeacher(Long id){
        Account account = findAccountById(id);
        return account.getAccountRole() == AccountRole.TEACHER;
    }

}
