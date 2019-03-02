package com.grayraccoon.webutils.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.grayraccoon.webutils.errors.ApiError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericDto<T> implements Serializable {

    private ApiError error;

    private T data;

}
