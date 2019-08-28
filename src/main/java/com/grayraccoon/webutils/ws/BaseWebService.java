package com.grayraccoon.webutils.ws;

import com.grayraccoon.webutils.dto.GenericDto;
import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ws")
@DefaultProperties(
        ignoreExceptions = CustomApiException.class,
        defaultFallback = "defaultFallback")
public class BaseWebService {

    private GenericDto<?> defaultFallback() {
        final String message = "Service is unavailable.";
        throw new CustomApiException(ApiError.builder()
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .throwable(new RuntimeException(message))
                .message(message)
                .build());
    }
}
