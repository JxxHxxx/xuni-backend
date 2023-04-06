package com.jxx.xuni.exception;

import com.jxx.xuni.auth.config.UnauthenticatedException;
import com.jxx.xuni.common.exception.NotPermissionException;
import com.jxx.xuni.group.domain.exception.CapacityOutOfBoundException;
import com.jxx.xuni.group.domain.exception.GroupJoinException;
import com.jxx.xuni.group.domain.exception.NotAppropriateGroupStatusException;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CapacityOutOfBoundException.class, NotAppropriateGroupStatusException.class,
                       GroupJoinException.class})
    public ResponseEntity<ExceptionResponse> groupCreateExceptionsHandler(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(400, exception.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({UnauthenticatedException.class})
    public ResponseEntity<ExceptionResponse> unAuthenticatedExceptionHandler(RuntimeException exception) {
        ExceptionResponse response = ExceptionResponse.of(401, exception.getMessage());
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler({SecurityException.class})
    public ResponseEntity<ExceptionResponse> securityExceptionHandler(SecurityException exception) {
        ExceptionResponse response = ExceptionResponse.of(400, exception.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler({NotPermissionException.class})
    public ResponseEntity<ExceptionResponse> notPermissionExceptionHandler(SecurityException exception) {
        ExceptionResponse response = ExceptionResponse.of(403, exception.getMessage());
        return new ResponseEntity<>(response, FORBIDDEN);
    }

}
