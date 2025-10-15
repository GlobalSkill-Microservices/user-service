package com.globalskills.user_service.Account.Repository;

import com.globalskills.user_service.Account.Entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepo extends JpaRepository<Domain,Long> {
    Optional<Domain> findByName(String domainName);
}
