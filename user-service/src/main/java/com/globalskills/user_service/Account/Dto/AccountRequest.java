package com.globalskills.user_service.Account.Dto;

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
public class AccountRequest {
    String username;
    String password;
    String fullName;
    Date dateOfBirth;
    String phone;
    String email;
    String avatarUrl;
    String profileCvUrl;
}
