package com.grayraccoon.webutils.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grayraccoon.webutils.errors.general.ApiSubError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Heriberto Reyes Esparza
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError extends ApiSubError {

    private String field;

    private Object rejectedValue;

    private String message;

    public ApiValidationError(final Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
}
