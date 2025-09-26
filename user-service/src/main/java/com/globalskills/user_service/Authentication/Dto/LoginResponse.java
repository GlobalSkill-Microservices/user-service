package com.globalskills.user_service.Authentication.Dto;

import com.globalskills.user_service.Account.Enum.AccountRole;
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
public class LoginResponse {
    Long id;
    String fullName;
    Date dateOfBirth;
    String phone;
    String email;
    String avatarUrl;
    AccountRole accountRole;
    Boolean isActive;
    Date lastLogin;

    String token;
}
