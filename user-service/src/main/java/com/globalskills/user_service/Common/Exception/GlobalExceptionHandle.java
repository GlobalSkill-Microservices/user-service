package com.globalskills.user_service.Common.Exception;

import com.globalskills.user_service.Common.Dto.BaseResponseAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponseAPI<Object>> handleBaseException(BaseException ex) {
        BaseResponseAPI<Object> response = new BaseResponseAPI<>(
                false,
                ex.getMessage(),
                null,
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }
}
