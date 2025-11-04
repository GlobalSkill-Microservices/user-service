package com.globalskills.user_service.Account.Repository;

import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Entity.Language;
import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Enum.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    Optional<Account> findByUsername(String username);
    boolean existsByEmailOrUsername(String email, String username);
    Optional<Account> findByEmail(String email);

    Page<Account> findAllByAccountRoleNot(PageRequest pageRequest, AccountRole accountRole);

    Page<Account> findAllByIsActiveAndAccountRoleNot(PageRequest pageRequest, Boolean isActive, AccountRole accountRole);

    Page<Account> findByProfileCvUrlIsNotNullAndApplicationStatus(PageRequest pageRequest, ApplicationStatus applicationStatus);

    @Query("SELECT a FROM Account a JOIN a.mentoringLanguages l WHERE l.name = :language")
    Page<Account> findByLanguageName(@Param("language") String language, PageRequest pageRequest);

    @Query("SELECT DISTINCT a FROM Account a JOIN a.mentoringLanguages l")
    Page<Account> findAllWithLanguages(PageRequest pageRequest);


}
