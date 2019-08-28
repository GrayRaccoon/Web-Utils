package com.grayraccoon.webutils.exceptions;

import com.grayraccoon.webutils.errors.ApiError;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Heriberto Reyes Esparza
 */
@ToString
public class CustomApiException extends RuntimeException {

    @Getter
    private ApiError apiError;

    public CustomApiException(ApiError apiError) {
        super(apiError.getMessage(), apiError.getThrowable());
        this.apiError = apiError;
    }
}
