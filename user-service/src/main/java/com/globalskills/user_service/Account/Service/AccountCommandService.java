package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.AccountRequest;
import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;


@Service
public class AccountCommandService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountQueryService accountQueryService;

    public AccountResponse save(Account account){
        accountRepo.save(account);
        return modelMapper.map(account, AccountResponse.class);
    }

    public void authorizedRole(String accountRole ,String authRole){
        Set<AccountRole> allowRolesForManager = EnumSet.of(AccountRole.USER,AccountRole.TEACHER);
        if (!Objects.equals(authRole, "ADMIN") && !Objects.equals(authRole, "MANAGER")) {
            throw new AccountException("Unauthorized action", HttpStatus.BAD_REQUEST);
        }
        if (Objects.equals(authRole, "MANAGER") && !allowRolesForManager.contains(AccountRole.valueOf(accountRole))) {
            throw new AccountException("Manager do not have permission to set role: " + accountRole, HttpStatus.BAD_REQUEST);
        }
    }

    public AccountResponse create(AccountRequest request,String accountRole ,String authRole){
        authorizedRole(accountRole, authRole);
        Account account = modelMapper.map(request,Account.class);
        account.setAccountRole(AccountRole.valueOf(accountRole));
        String originPassword = account.getPassword();
        account.setIsActive(true);
        accountRepo.save(account);
        return modelMapper.map(account,AccountResponse.class);
    }

    public AccountResponse update(AccountRequest request, String accountRole, Long accountId, String authRole){
        authorizedRole(accountRole, authRole);
        Account oldAccount = accountQueryService.findAccountById(accountId);
        oldAccount.setAccountRole(AccountRole.valueOf(accountRole));
        String newPassword = request.getPassword();
        accountRepo.save(oldAccount);
        return modelMapper.map(oldAccount, AccountResponse.class);
    }

    public AccountResponse update(AccountRequest request, Long currentAccountId){
        Account oldAccount = accountQueryService.findAccountById(currentAccountId);
        modelMapper.map(request,oldAccount);
        accountRepo.save(oldAccount);
        return modelMapper.map(oldAccount, AccountResponse.class);
    }

    public void delete(Long accountId){
        Account account = accountQueryService.findAccountById(accountId);
        account.setIsActive(false);
        accountRepo.save(account);
    }




}
