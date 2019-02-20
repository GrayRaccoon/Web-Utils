package com.grayraccoon.webutils.exceptions;

import com.grayraccoon.webutils.errors.ApiError;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CustomApiException extends RuntimeException {

    private ApiError apiError;

    public CustomApiException(ApiError apiError) {
        super(apiError.getMessage(), apiError.getThrowable());
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("apiError", apiError)
                .toString();
    }
}
