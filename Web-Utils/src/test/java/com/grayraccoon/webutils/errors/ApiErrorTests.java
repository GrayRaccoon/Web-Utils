package com.grayraccoon.webutils.errors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/**
 * @author Heriberto Reyes Esparza
 */
public class ApiErrorTests {

    // Constants

    private static final HttpStatus CUSTOM_HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final String CUSTOM_CODE = "nice-code";
    private static final String CUSTOM_MSG = "Some error.";
    private static final String CUSTOM_DETAIL_MSG = "Some null pointer in the new feature.";
    private static final String RANDOM_ERROR_STR = "Some crazy error cause.";

    @Test
    public void build_allArguments_Success() {
        final ApiError apiError = ApiError.builder()
                .status(CUSTOM_HTTP_STATUS)
                .code(CUSTOM_CODE)
                .message(CUSTOM_MSG)
                .debugMessage(CUSTOM_DETAIL_MSG)
                .throwable(new NullPointerException(RANDOM_ERROR_STR))
                .build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(CUSTOM_HTTP_STATUS);

        Assertions.assertThat(apiError.getCode()).isNotNull();
        Assertions.assertThat(apiError.getCode()).isEqualTo(CUSTOM_CODE);

        Assertions.assertThat(apiError.getMessage()).isNotNull();
        Assertions.assertThat(apiError.getMessage()).isEqualTo(CUSTOM_MSG);

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEqualTo(CUSTOM_DETAIL_MSG);
    }

    @Test
    public void build_missingStatusWithThrowable_Success() {
        final ApiError apiError = ApiError.builder()
                .code(CUSTOM_CODE)
                .message(CUSTOM_MSG)
                .debugMessage(CUSTOM_DETAIL_MSG)
                .throwable(new NullPointerException(RANDOM_ERROR_STR))
                .build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        Assertions.assertThat(apiError.getCode()).isNotNull();
        Assertions.assertThat(apiError.getCode()).isEqualTo(CUSTOM_CODE);

        Assertions.assertThat(apiError.getMessage()).isNotNull();
        Assertions.assertThat(apiError.getMessage()).isEqualTo(CUSTOM_MSG);

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEqualTo(CUSTOM_DETAIL_MSG);
    }

    @Test
    public void build_missingStatusNoThrowable_Success() {
        final ApiError apiError = ApiError.builder()
                .code(CUSTOM_CODE)
                .message(CUSTOM_MSG)
                .debugMessage(CUSTOM_DETAIL_MSG)
                .build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(apiError.getCode()).isNotNull();
        Assertions.assertThat(apiError.getCode()).isEqualTo(CUSTOM_CODE);

        Assertions.assertThat(apiError.getMessage()).isNotNull();
        Assertions.assertThat(apiError.getMessage()).isEqualTo(CUSTOM_MSG);

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEqualTo(CUSTOM_DETAIL_MSG);
    }

    @Test
    public void build_missingCode_Success() {
        final ApiError apiError = ApiError.builder()
                .message(CUSTOM_MSG)
                .debugMessage(CUSTOM_DETAIL_MSG)
                .build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(apiError.getCode()).isNotNull();
        Assertions.assertThat(apiError.getCode()).isEqualTo(String.valueOf(400));

        Assertions.assertThat(apiError.getMessage()).isNotNull();
        Assertions.assertThat(apiError.getMessage()).isEqualTo(CUSTOM_MSG);

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEqualTo(CUSTOM_DETAIL_MSG);
    }

    @Test
    public void build_missingMessage_Success() {
        final ApiError apiError = ApiError.builder()
                .debugMessage(CUSTOM_DETAIL_MSG)
                .build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(apiError.getCode()).isNotNull();
        Assertions.assertThat(apiError.getCode()).isEqualTo(String.valueOf(400));

        Assertions.assertThat(apiError.getMessage()).isNotNull();
        Assertions.assertThat(apiError.getMessage()).isEqualTo("Bad Request");

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEqualTo(CUSTOM_DETAIL_MSG);
    }

    @Test
    public void build_missingDebugMessageWithThrowable_Success() {
        final ApiError apiError = ApiError.builder()
                .throwable(new NullPointerException(RANDOM_ERROR_STR))
                .build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEqualTo(RANDOM_ERROR_STR);
    }

    @Test
    public void build_missingDebugMessageNoThrowable_Success() {
        final ApiError apiError = ApiError.builder().build();

        Assertions.assertThat(apiError).isNotNull();

        Assertions.assertThat(apiError.getStatus()).isNotNull();
        Assertions.assertThat(apiError.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        Assertions.assertThat(apiError.getCode()).isNotNull();
        Assertions.assertThat(apiError.getCode()).isEqualTo(String.valueOf(400));

        Assertions.assertThat(apiError.getMessage()).isNotNull();
        Assertions.assertThat(apiError.getMessage()).isEqualTo("Bad Request");

        Assertions.assertThat(apiError.getDebugMessage()).isNotNull();
        Assertions.assertThat(apiError.getDebugMessage()).isEmpty();
    }

}
