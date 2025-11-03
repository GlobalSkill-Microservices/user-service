package com.globalskills.user_service.Account.Service;

import com.globalskills.user_service.Account.Dto.AccountRequest;
import com.globalskills.user_service.Account.Dto.AccountResponse;
import com.globalskills.user_service.Account.Dto.CvListApproved;
import com.globalskills.user_service.Account.Dto.EmailDto;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Entity.Domain;
import com.globalskills.user_service.Account.Entity.Language;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Enum.ApplicationStatus;
import com.globalskills.user_service.Account.Exception.AccountException;
import com.globalskills.user_service.Account.Repository.AccountRepo;
import com.globalskills.user_service.Account.Repository.DomainRepo;
import com.globalskills.user_service.Account.Repository.LanguageRepo;
import com.globalskills.user_service.Authentication.Dto.ResetPasswordRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AccountCommandService {

    @Autowired
    DomainRepo domainRepo;

    @Autowired
    LanguageRepo languageRepo;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    AccountQueryService accountQueryService;

    @Autowired
    S3Service s3Service;

    @Autowired
    EmailService emailService;

    public void save(Account account){
        accountRepo.save(account);
        modelMapper.map(account, AccountResponse.class);
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

    @Transactional
    public AccountResponse update(AccountRequest request, Long currentAccountId){
        Account oldAccount = accountQueryService.findAccountById(currentAccountId);
        modelMapper.map(request,oldAccount);
        String hashPassword = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10));
        oldAccount.setPassword(hashPassword);
        updateUserSkills(oldAccount, request.getLanguageNames(), request.getDomainNames());
        accountRepo.save(oldAccount);
        return modelMapper.map(oldAccount, AccountResponse.class);
    }

    private void updateUserSkills(Account account, Set<String> languageNames, Set<String> domainNames) {
        if (languageNames == null || domainNames == null) {
            return;
        }
        Set<Language> mentoringLanguages = new HashSet<>();
        for (String langName : languageNames) {
            Language language = languageRepo.findByName(langName)
                    .orElseGet(() -> {
                        Language newLanguage = new Language();
                        newLanguage.setName(langName);
                        return languageRepo.save(newLanguage);
                    });
            mentoringLanguages.add(language);
        }
        account.setMentoringLanguages(mentoringLanguages);

        Set<Domain> mentoringDomains = new HashSet<>();
        for (String domainName : domainNames) {
            Domain domain = domainRepo.findByName(domainName)
                    .orElseGet(() -> {
                        Domain newDomain = new Domain();
                        newDomain.setName(domainName);
                        return domainRepo.save(newDomain);
                    });
            mentoringDomains.add(domain);
        }
        account.setMentoringDomains(mentoringDomains);
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
            account.setApplicationStatus(ApplicationStatus.APPROVED);

            EmailDto emailDto = new EmailDto();
            emailDto.setAccount(account);
            emailDto.setSubject("Approve your CV");
            emailDto.setLink("#");
            emailDto.setCreatedDate(new Date());
            emailService.sendEmailApproveCV(emailDto);

        }
        accountRepo.saveAll(accountList);
        return true;
    }

    @Transactional
    public boolean rejectCV(CvListApproved listApproved){
        List<Long> accountIds = listApproved.getId();
        if (accountIds == null || accountIds.isEmpty()) {
            return false;
        }
        List<Account> accountList = accountQueryService.findAllAccountById(accountIds);
        for(Account account : accountList){
            account.setApplicationStatus(ApplicationStatus.REJECT);

            EmailDto emailDto = new EmailDto();
            emailDto.setAccount(account);
            emailDto.setSubject("Approve your CV");
            emailDto.setLink("#");
            emailDto.setCreatedDate(new Date());
            emailService.sendEmailApproveCV(emailDto);

        }
        return true;
    }

    public AccountResponse cvUpload(MultipartFile file, Long accountId)throws IOException {
        Account account = accountQueryService.findAccountById(accountId);
        String profileUrl = s3Service.upLoadCv(file);
        account.setProfileCvUrl(profileUrl);
        if(account.getAccountRole() == AccountRole.TEACHER || account.getApplicationStatus()==ApplicationStatus.REJECT){
            account.setApplicationStatus(ApplicationStatus.RE_SUBMIT);
            accountRepo.save(account);
        }
        accountRepo.save(account);
        return modelMapper.map(account,AccountResponse.class);
    }

    public void changePassword(Long accountId, ResetPasswordRequest request){
        Account account = accountQueryService.findAccountById(accountId);
        account.setPassword(BCrypt.hashpw(request.getPassword(),BCrypt.gensalt(10)));
        try{
            save(account);
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
    }

}
