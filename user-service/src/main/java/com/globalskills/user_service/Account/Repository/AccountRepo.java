package com.globalskills.user_service.Account.Repository;

import com.globalskills.user_service.Account.Entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    Optional<Account> findByUsername(String username);
    boolean existsByEmailOrUsername(String email, String username);
    Optional<Account> findByEmail(String email);

    Page<Account> findAllByIsActive(Pageable pageable,boolean isActive);
}
