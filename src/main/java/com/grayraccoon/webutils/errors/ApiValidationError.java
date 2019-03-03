package com.grayraccoon.webutils.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grayraccoon.webutils.errors.general.ApiSubError;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError extends ApiSubError {

    private String objectType;

    private String field;

    private Object rejectedValue;

    private String message;

    public ApiValidationError(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
    public ApiValidationError(String field, Object rejectedValue, String message) {
        this(null, field, rejectedValue, message);
    }

}
