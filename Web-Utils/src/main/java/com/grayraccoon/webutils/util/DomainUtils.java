package com.grayraccoon.webutils.util;

import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DomainUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainUtils.class);

    /**
     * Tries to parse given raw entity id into a valid UUID.
     *
     * @param rawEntityId Raw id to be parsed.
     * @return Parsed Id.
     */
    public static UUID parseEntityId(final String rawEntityId) {
        final UUID entityId;
        try {
            entityId = UUID.fromString(rawEntityId);
        } catch (final IllegalArgumentException ex) {
            LOGGER.error("Error while getting UUID from rawId: {}", rawEntityId, ex);
            throw new CustomApiException(
                    ApiError.builder()
                            .throwable(ex)
                            .status(HttpStatus.BAD_REQUEST)
                            .subError(new ApiValidationError(rawEntityId))
                            .build()
            );
        }
        return entityId;
    }

    /**
     * Validates if given object is null.
     *
     * @param obj Object to validate.
     * @param errorMessage error message if object is null.
     * @param <T> Object type.
     * @return object if not null.
     */
    public static <T> T requireNonNull(final T obj, final String errorMessage) {
        if (obj == null) {
            throw new CustomApiException(
                    ApiError.builder()
                            .message(errorMessage)
                            .status(HttpStatus.BAD_REQUEST)
                            .subError(new ApiValidationError("null"))
                            .build()
            );
        }
        return obj;
    }

}
