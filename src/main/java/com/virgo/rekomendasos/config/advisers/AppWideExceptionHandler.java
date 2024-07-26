package com.virgo.rekomendasos.config.advisers;

import com.virgo.rekomendasos.config.advisers.exception.NotFoundException;
import com.virgo.rekomendasos.config.advisers.exception.ValidateException;
import com.virgo.rekomendasos.utils.response.WebResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import javax.naming.AuthenticationException;
//import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@CrossOrigin
public class AppWideExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return new ResponseEntity<>(new WebResponse<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new WebResponse<>("Not Found", HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<?> handleValidationException(ValidateException e) {
        return new ResponseEntity<>(new WebResponse<>("Request Tidak Sesuai", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
//        return new ResponseEntity<>(new WebResponse<>("Access Denied", HttpStatus.FORBIDDEN, e.getMessage()), HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(new WebResponse<>("Invalid login Credetiantials", HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

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
    public ResponseEntity<String> handleInternalServerError(HttpServerErrorException.InternalServerError ex) {
        return new ResponseEntity<>(
                "500: Unknown Intenal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
