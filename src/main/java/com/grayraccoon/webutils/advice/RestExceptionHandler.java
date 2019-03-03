package com.grayraccoon.webutils.advice;

import com.grayraccoon.webutils.dto.GenericDto;
import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.grayraccoon.webutils.services.CustomValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.ConstraintViolationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class.getName());

    @Autowired
    private CustomValidatorService customValidatorService;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        ResponseEntity<Object> responseEntity = buildResponseEntity(ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .throwable(ex)
                .message(error)
                .build());
        LOGGER.error("handleHttpMessageNotReadable: {}", responseEntity);
        return responseEntity;
    }

    @ExceptionHandler(CustomApiException.class)
    protected ResponseEntity<Object> handleCustomApiException(
            CustomApiException ex) {

        ApiError apiError = ex.getApiError();

        Throwable cause = ex.getCause();
        if (cause instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) cause;
            apiError = customValidatorService.getApiErrorFromConstraintViolations(
                    violationException.getConstraintViolations()
            );
        }

        ResponseEntity<Object> responseEntity = buildResponseEntity(apiError);
        LOGGER.error("handleCustomApiException: {}", responseEntity);
        return responseEntity;
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        GenericDto dto = GenericDto.builder().error(apiError).build();
        return new ResponseEntity<>(dto, apiError.getStatus());
    }

}
