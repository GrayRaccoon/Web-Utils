package com.grayraccoon.webutils.exceptions;

import com.grayraccoon.webutils.errors.ApiError;

public class CustomApiException extends RuntimeException {

    private ApiError apiError;

    public CustomApiException(ApiError apiError) {
        super(apiError.getMessage(), apiError.getThrowable());
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }

}
