package com.example.mspayment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;

import static com.example.mspayment.model.constant.ExceptionConstants.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleInternal(Exception exception) {
        log.error("Exception : ", exception);
        return new ExceptionResponse(UNEXPECTED_EXCEPTION_CODE, UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handleNotFound(NotFoundException exception) {
        log.error("NotFoundException : ", exception);
        return new ExceptionResponse(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(UndeclaredThrowableException.class) //InvocationTargetException
    @ResponseStatus(SERVICE_UNAVAILABLE)
    public ExceptionResponse handleServiceUnavailable(UndeclaredThrowableException exception) {
        log.error("UndeclaredThrowableException", exception);
        return new ExceptionResponse(SERVICE_UNAVAILABLE.name(), exception.getCause().getCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handleInputParam(MethodArgumentTypeMismatchException exception) {
        log.error("MethodArgumentTypeMismatchException : ", exception);
        String paramName = exception.getParameter().getParameterName();

        return new ExceptionResponse(INPUT_PARAM_CODE, paramName + INPUT_PARAM_MESSAGE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
