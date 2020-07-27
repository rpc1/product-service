package com.app.productservice.controllers;

import com.app.productservice.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerErrorHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ObjectNotFoundException.class})
    protected ResponseEntity<Object> handleObjectNotFoundException(Exception e, WebRequest webRequest) {
        return handleExceptionInternal(e, null,
                new HttpHeaders(), HttpStatus.NOT_FOUND, webRequest);

    }

    @ExceptionHandler(value = {HttpMessageConversionException.class})
    protected ResponseEntity<Object> handleRequestParseException(Exception e, WebRequest webRequest) {
        return handleExceptionInternal(e, null,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);
    }
}