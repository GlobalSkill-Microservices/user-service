package com.globalskills.user_service.Account.Repository;

import com.globalskills.user_service.Account.Entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepo extends JpaRepository<Language,Long> {
    Optional<Language> findByName(String langName);
}
