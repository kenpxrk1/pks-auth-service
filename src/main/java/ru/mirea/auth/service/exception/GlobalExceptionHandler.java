package ru.mirea.auth.service.exception;

import ru.mirea.auth.service.dto.response.ErrorResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<List<ErrorResponseDto>> handleException(MethodArgumentNotValidException e) {
        List<ErrorResponseDto> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponseDto(error.getDefaultMessage()))
                .toList();
        log.error("Validation exception", e);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    private ResponseEntity<ErrorResponseDto> handleException(EntityAlreadyExistsException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ErrorResponseDto> handleException(EntityNotFoundException e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        ErrorResponseDto response = new ErrorResponseDto(e.getMessage());
        log.error("Exception was thrown", e);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
