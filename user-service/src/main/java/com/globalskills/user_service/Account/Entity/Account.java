package com.globalskills.user_service.Account.Entity;

import com.globalskills.user_service.Account.Enum.AccountRole;
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
    String profileCvUrl;
    Boolean isActive;
    Date lastLogin;

    @ManyToMany
    @JoinTable(
            name = "manager_languages",
            joinColumns = @JoinColumn(name = "manager_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    Set<Language> managedLanguage;

}
