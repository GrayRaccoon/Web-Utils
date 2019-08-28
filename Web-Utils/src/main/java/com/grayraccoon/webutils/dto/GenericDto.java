package com.grayraccoon.webutils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grayraccoon.webutils.errors.ApiError;
import lombok.*;

import java.io.Serializable;

/**
 * @author Heriberto Reyes Esparza
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericDto<T> implements Serializable {

    private ApiError error;

    private T data;

}
