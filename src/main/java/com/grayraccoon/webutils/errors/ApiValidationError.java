package com.grayraccoon.webutils.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grayraccoon.webutils.errors.general.ApiSubError;
import lombok.*;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError extends ApiSubError {

    private String field;

    private Object rejectedValue;

    private String message;

    public ApiValidationError(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

}
