package com.globalskills.user_service.Account.Entity;

import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Enum.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;

    String password;

    String fullName;

    Date dateOfBirth;

    String phone;

    String email;

    String avatarUrl;

    @Enumerated(EnumType.STRING)
    AccountRole accountRole;

    @Enumerated(EnumType.STRING)
    ApplicationStatus applicationStatus;

    String profileCvUrl;

    Boolean isActive;

    Date lastLogin;

    @ManyToMany
    @JoinTable(
            name = "mentor_languages",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    Set<Language> mentoringLanguages = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mentor_domains",
            joinColumns = @JoinColumn(name = "mentor_id"),
            inverseJoinColumns = @JoinColumn(name = "domain_id")
    )
    Set<Domain> mentoringDomains = new HashSet<>();

}
