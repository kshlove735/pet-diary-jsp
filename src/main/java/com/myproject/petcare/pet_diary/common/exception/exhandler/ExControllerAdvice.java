package com.myproject.petcare.pet_diary.common.exception.exhandler;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Map<String, String>>> handleValidException(MethodArgumentNotValidException e) {
        log.error("[exceptionHandler] MethodArgumentNotValidException", e);

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String field = ((FieldError) error).getField();
                    String defaultMessage = error.getDefaultMessage();
                    errors.put(field, defaultMessage);
                });

        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>(false, "DTO 검증 오류", errors);


        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    // @RequestParam validation 오류
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e) {

        Map<String, String> errors = new HashMap<>();

        e.getConstraintViolations().forEach((error) -> {
            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);
            List<Path.Node> list = stream.collect(Collectors.toList());

            String field = list.get(list.size()-1).getName();
            String defaultMessage = error.getMessage();
            errors.put(field, defaultMessage);
        });

        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>(false, "DTO 검증 오류", errors);

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ResponseDto> handleMissingRequestCookieException(MissingRequestCookieException e) {
        log.error("[exceptionHandler] MissingRequestCookieException", e);
        return new ResponseEntity<>(new ResponseDto(false, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedException e) {
        log.error("[exceptionHandler] UnauthorizedException", e);
        return new ResponseEntity<>(new ResponseDto(false, e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleRuntimeException(RuntimeException e) {
        log.error("[exceptionHandler] RuntimeException", e);
        return new ResponseEntity<>(new ResponseDto(false, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseDto handleException(Exception e) {
        log.error("[exceptionHandler] Exception", e);
        return new ResponseDto(false, e.getMessage());
    }
}