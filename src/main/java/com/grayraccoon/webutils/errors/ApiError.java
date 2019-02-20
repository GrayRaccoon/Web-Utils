package com.grayraccoon.webutils.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grayraccoon.webutils.errors.general.ApiSubError;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
public class ApiError {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Setter(AccessLevel.NONE) private LocalDateTime timestamp;

    private String message;

    private String debugMessage;

    @JsonIgnore
    private Throwable throwable;

    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
        status = HttpStatus.BAD_REQUEST;
        message = status.getReasonPhrase();
    }

    @Builder()
    public ApiError(HttpStatus status, String message, String debugMessage, Throwable ex, @Singular List<ApiSubError> subErrors) {
        this();
        this.status = status != null ? status: ex != null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.BAD_REQUEST;
        this.message = StringUtils.isNotBlank(message) ? message : this.status.getReasonPhrase();
        this.debugMessage = StringUtils.isNotBlank(debugMessage) ? debugMessage : ex != null ? ex.getLocalizedMessage() : "";
        this.throwable = ex;
        this.subErrors = subErrors;
    }

}
