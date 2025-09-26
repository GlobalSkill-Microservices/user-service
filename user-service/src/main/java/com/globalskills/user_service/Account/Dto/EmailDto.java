package com.globalskills.user_service.Account.Dto;

import com.globalskills.user_service.Account.Entity.Account;
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
public class EmailDto {
    Account account;
    String subject;
    String link;
    Date createdDate;
}
