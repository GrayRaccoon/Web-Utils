package com.grayraccoon.webutils.ws;

import com.grayraccoon.webutils.dto.GenericDto;
import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ws")
@DefaultProperties(
        ignoreExceptions = CustomApiException.class,
        defaultFallback = "defaultFallback")
public class BaseWebService {

    private GenericDto<?> defaultFallback() {
        final String message = "Unknown error in Server";
        throw new CustomApiException(ApiError.builder()
                .throwable(new RuntimeException(message))
                .message(message)
                .build());
    }
}
