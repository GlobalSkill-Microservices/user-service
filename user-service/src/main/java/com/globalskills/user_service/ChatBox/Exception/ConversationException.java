package com.globalskills.user_service.ChatBox.Exception;

import com.globalskills.user_service.Common.Exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConversationException extends BaseException {
    public ConversationException(String message, HttpStatus status) {
        super(message,status);
    }
}
