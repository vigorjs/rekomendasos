package com.virgo.rekomendasos.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Bad Input Handler
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Authentication Error Handler
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Error: Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<String> handleAccountStatusException(AccountStatusException ex) {
        return  new ResponseEntity<>("Error: The Account is Locked", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex){
        return new ResponseEntity<>("Error: You are not authorized to access this resource", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex){
        return new ResponseEntity<>(
                "Error: Your Session has expired, Please Login Again",
                HttpStatus.FORBIDDEN
        );
    }

    // Method Argument Error Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // Internal Server Error Handler
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<String> handleInternalServerError(HttpServerErrorException.InternalServerError ex){
        return new ResponseEntity<>(
                "500: Unknown Intenal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
