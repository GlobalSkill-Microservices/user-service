package com.globalskills.user_service.Account.Exception;

import com.globalskills.user_service.Common.Exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmailException extends BaseException {
    public EmailException(String message,HttpStatus status) {
        super(message,status);
    }
}
