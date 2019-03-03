package com.grayraccoon.webutils.services;

import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.errors.ApiValidationError;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface CustomValidatorService {

    public Set<ApiValidationError> validateObject(Object object);

    ApiValidationError getApiValidationErrorFromConstraintViolation(ConstraintViolation<?> violation);

    Set<ApiValidationError> getApiValidationErrorsFromConstraintViolations(Set<ConstraintViolation<?>> violations);

    ApiError getApiErrorFromConstraintViolations(Set<ConstraintViolation<?>> validationErrors);

    ApiError getApiErrorFromApiValidationErrors(Set<ApiValidationError> apiErrors);

}
