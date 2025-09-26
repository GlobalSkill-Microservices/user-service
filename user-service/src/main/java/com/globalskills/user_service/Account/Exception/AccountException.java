package com.globalskills.user_service.Account.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountException extends RuntimeException {
  private final HttpStatus status;
    public AccountException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
