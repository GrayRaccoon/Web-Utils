package com.grayraccoon.webutils.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grayraccoon.webutils.errors.general.ApiSubError;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiValidationError extends ApiSubError {

    private String objectType;

    private String field;

    @NonNull
    private Object rejectedValue;

    private String message;

}
