package com.globalskills.user_service.Account.Exception;

import com.globalskills.user_service.Common.Exception.BaseException;
import org.springframework.http.HttpStatus;

public class AccountException extends BaseException {
    public AccountException(String message,HttpStatus status) {
        super(message,status);
    }
}
