package com.globalskills.user_service.Account.Dto;

import com.globalskills.user_service.Account.Enum.AccountRole;
import com.globalskills.user_service.Account.Enum.ApplicationStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    Long id;
    String username;
    String password;
    String fullName;
    Date dateOfBirth;
    String phone;
    String email;
    String avatarUrl;
    AccountRole accountRole;
    ApplicationStatus applicationStatus;
    String profileCvUrl;
    Boolean isActive;
    Date lastLogin;
}
