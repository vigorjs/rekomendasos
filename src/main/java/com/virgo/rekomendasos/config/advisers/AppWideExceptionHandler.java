package com.virgo.rekomendasos.config.advisers;

import com.virgo.rekomendasos.config.advisers.exception.ValidateException;
import com.virgo.rekomendasos.utils.responseWrapper.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.webjars.NotFoundException;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@CrossOrigin
public class AppWideExceptionHandler {

    /*
    * Authentication Handler Exception
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Error: Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<?> handleValidationException(ValidateException e) {
        return new ResponseEntity<>(new WebResponse<>("Request Tidak Sesuai", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(new WebResponse<>("Invalid login Credetiantials", HttpStatus.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return new ResponseEntity<>(new WebResponse<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new WebResponse<>("Not Found", HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    // Bad Input Handler
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlePropertyValueException(Exception e){
        String errorMessage = "BAD REQUEST : " + e.getMessage();
        if (e.getMessage().contains("\"email\"")){
            errorMessage = "Email Field Cannot be Empty";
        }
        if (e.getMessage().contains("rawPassword")) {
            errorMessage = "Password Field Cannot be Empty";
        }
        if (e.getMessage().contains("\"first_name\"")) {
            errorMessage = "FirstName Field Cannot be Empty";
        }
        if (e.getMessage().contains("\"last_name\"")) {
            errorMessage = "LastName Field Cannot be Empty";
        }
        if (e.getMessage().contains("\"users_email_key\"")){
            errorMessage = "Email Has Been Registered On Our System, Please Login Instead";
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Account Locked Exception
    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<String> handleAccountStatusException(AccountStatusException ex) {
        return  new ResponseEntity<>("Error: The Account is Locked", HttpStatus.FORBIDDEN);
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
