package com.globalskills.user_service.Common.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    public BaseException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}