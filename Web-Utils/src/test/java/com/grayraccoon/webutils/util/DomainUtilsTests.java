package com.grayraccoon.webutils.util;

import com.grayraccoon.webutils.exceptions.CustomApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
public class DomainUtilsTests {

    @Test
    public void test_parseEntityId_validRawId_Success() {
        final String rawEntityId = UUID.randomUUID().toString();

        final UUID entityId = DomainUtils.parseEntityId(rawEntityId);

        Assertions.assertThat(entityId).isNotNull();
        Assertions.assertThat(entityId.toString()).isEqualTo(rawEntityId);
    }

    @Test
    public void test_parseEntityId_invalidRawId_Fail() {
        final String rawEntityId = "abc-1234";

        org.junit.jupiter.api.Assertions.assertThrows(CustomApiException.class,
                () -> DomainUtils.parseEntityId(rawEntityId));
    }

    @Test
    public void test_requireNonNull_nonNullObject_Success() {
        final Object obj = "Nice Non Null Contents.";

        DomainUtils.requireNonNull(obj, "Obj cannot be null.");
    }

    @Test
    public void test_requireNonNull_nullObject_Success() {
        final Object obj = null;

        org.junit.jupiter.api.Assertions.assertThrows(CustomApiException.class,
                () -> DomainUtils.requireNonNull(obj, "Obj cannot be null."));
    }

}
