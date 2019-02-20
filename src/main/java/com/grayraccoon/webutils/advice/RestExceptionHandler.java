package com.grayraccoon.webutils.advice;

import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.grayraccoon.webutils.errors.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class.getName());

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        ResponseEntity<Object> responseEntity = buildResponseEntity(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .ex(ex)
                .message(error)
                .build());
        LOGGER.error("handleHttpMessageNotReadable: {}", responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(CustomApiException.class)
    protected ResponseEntity<Object> handleCustomApiException(
            CustomApiException ex) {
        ResponseEntity<Object> responseEntity = buildResponseEntity(ex.getApiError());
        LOGGER.error("handleCustomApiException: {}", responseEntity);
        return responseEntity;
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
