package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.services.CustomValidatorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@Service
public class CustomValidatorServiceImpl implements CustomValidatorService {

    private Validator validator;

    public CustomValidatorServiceImpl() {
        getValidator();
    }

    private Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    @Override
    public Set<ApiValidationError> validateObject(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations
                = getValidator().validate( object );
        return constraintViolations.stream()
                .map(this::getApiValidationErrorFromConstraintViolation)
                .collect(Collectors.toSet());
    }

    @Override
    public ApiValidationError getApiValidationErrorFromConstraintViolation(ConstraintViolation<?> violation) {
        return new ApiValidationError(
                violation.getPropertyPath().toString(),
                violation.getInvalidValue(),
                violation.getMessage()
        );
    }

    @Override
    public Set<ApiValidationError> getApiValidationErrorsFromConstraintViolations(Set<ConstraintViolation<?>> violations) {
        return violations.stream().map(this::getApiValidationErrorFromConstraintViolation).collect(Collectors.toSet());
    }

    @Override
    public ApiError getApiErrorFromConstraintViolations(Set<ConstraintViolation<?>> validationErrors) {
        return this.getApiErrorFromApiValidationErrors(
                this.getApiValidationErrorsFromConstraintViolations(validationErrors)
        );
    }

    @Override
    public ApiError getApiErrorFromApiValidationErrors(Set<ApiValidationError> apiErrors) {
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .subErrors(apiErrors)
                .build();
    }


}
