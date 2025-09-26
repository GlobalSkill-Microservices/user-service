package com.globalskills.user_service.Account.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailException extends RuntimeException {
    private final HttpStatus status;
    public EmailException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
