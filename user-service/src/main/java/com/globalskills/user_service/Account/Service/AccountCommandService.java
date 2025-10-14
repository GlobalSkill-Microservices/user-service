package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.AccountRequest;
import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Dto.CvListApproved;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AccountCommandService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountQueryService accountQueryService;

    @Autowired
    S3Service s3Service;

    public AccountResponse updateRole(Long id){
        Account updateAccount = accountQueryService.findAccountById(id);
        if(updateAccount.getAccountRole() != AccountRole.USER){
            throw new AccountException("Account is not Role user to update", HttpStatus.BAD_REQUEST);
        }
        updateAccount.setAccountRole(AccountRole.TEACHER);
        accountRepo.save(updateAccount);
        return modelMapper.map(updateAccount, AccountResponse.class);
    }

    public AccountResponse save(Account account){
        accountRepo.save(account);
        return modelMapper.map(account, AccountResponse.class);
    }

    public AccountResponse create(AccountRequest request){
        Account account = modelMapper.map(request,Account.class);
        account.setAccountRole(request.getAccountRole());
        String hashPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10));
        account.setPassword(hashPassword);
        account.setIsActive(true);
        accountRepo.save(account);
        return modelMapper.map(account,AccountResponse.class);
    }

    public AccountResponse updateByUserId(AccountRequest request,Long accountId){
        Account oldAccount = accountQueryService.findAccountById(accountId);
        modelMapper.map(request,oldAccount);
        String hashPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10));
        oldAccount.setPassword(hashPassword);
        accountRepo.save(oldAccount);
        return modelMapper.map(oldAccount, AccountResponse.class);
    }

    public AccountResponse update(AccountRequest request, Long currentAccountId){
        Account oldAccount = accountQueryService.findAccountById(currentAccountId);
        modelMapper.map(request,oldAccount);
        String hashPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10));
        oldAccount.setPassword(hashPassword);
        accountRepo.save(oldAccount);
        return modelMapper.map(oldAccount, AccountResponse.class);
    }

    public void delete(Long accountId){
        Account account = accountQueryService.findAccountById(accountId);
        account.setIsActive(false);
        accountRepo.save(account);
    }

    public void activeAccount(Long accountId){
        Account account = accountQueryService.findAccountById(accountId);
        account.setIsActive(true);
        accountRepo.save(account);
    }

    @Transactional
    public boolean approvedCv(CvListApproved listApproved){
        List<Long> accountIds = listApproved.getId();
        if (accountIds == null || accountIds.isEmpty()) {
            return false;
        }
        List<Account> accountList = accountQueryService.findAllAccountById(accountIds);
        for(Account account : accountList){
            account.setAccountRole(AccountRole.TEACHER);
        }
        accountRepo.saveAll(accountList);
        return true;
    }

    public AccountResponse cvUpload(MultipartFile file, Long accountId)throws IOException {
        Account account = accountQueryService.findAccountById(accountId);
        String profileUrl = s3Service.upLoadCv(file);
        account.setProfileCvUrl(profileUrl);
        accountRepo.save(account);
        return modelMapper.map(account,AccountResponse.class);
    }





}
