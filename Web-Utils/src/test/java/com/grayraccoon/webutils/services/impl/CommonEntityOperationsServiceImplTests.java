package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import lombok.Builder;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Heriberto Reyes Esparza
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonEntityOperationsServiceImplTests {

    @InjectMocks
    private CommonEntityOperationsServiceImpl commonEntityOperationsService;

    @Mock
    private CustomValidatorServiceImpl customValidatorService;

    @Mock
    private SomeEntityRepository someEntityRepository;


    @Before
    public void setUp() throws Exception {
        Mockito.when(customValidatorService.validateObject(ArgumentMatchers.any())).thenCallRealMethod();
        Mockito.when(customValidatorService.getApiErrorFromApiValidationErrors(ArgumentMatchers.anySet()))
                .thenCallRealMethod();
        Mockito.when(customValidatorService.getApiValidationErrorFromConstraintViolation(ArgumentMatchers.any()))
                .thenCallRealMethod();
        Mockito.when(someEntityRepository.save(ArgumentMatchers.any(SomeEntity.class))).thenCallRealMethod();
        Mockito.when(customValidatorService.getValidator()).thenCallRealMethod();
    }

    @Test
    public void validateAndSaveEntity_NoCallbacks_Success() {
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity()
        );
        validateBasicSomeEntity(resultedEntity);
    }

    @Test(expected = CustomApiException.class)
    public void validateAndSaveEntity_NoCallbacks_Fail() {
        final SomeEntity invalidEntity = SomeEntity.builder()
                .id(1)
                .build();
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository, invalidEntity);
        validateBasicSomeEntity(resultedEntity);
    }

    @Test
    public void validateAndSaveEntity_WithManualTest_Success() {
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity(),
                this::performSomeManualValidation
        );
        validateBasicSomeEntity(resultedEntity);
    }

    @Test(expected = CustomApiException.class)
    public void validateAndSaveEntity_WithManualTest_Fail() {
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity().toBuilder().id(1).build(),
                this::performSomeManualValidation
        );
        validateBasicSomeEntity(resultedEntity);
    }

    @Test
    public void validateAndSaveEntity_WithPostProcessing_Success() {
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity(),
                this::performSomePostProcessing
        );
        validateBasicSomeEntity(resultedEntity, 10);
    }

    @Test
    public void validateAndSaveEntity_WithManualTestAndPostProcessing_Success() {
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity(),
                this::performSomeManualValidation,
                this::performSomePostProcessing
        );
        validateBasicSomeEntity(resultedEntity, 10);
    }

    @Test(expected = CustomApiException.class)
    public void validateAndSaveEntity_WithManualTestAndPostProcessing_Fail() {
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity().toBuilder().id(1).build(),
                this::performSomeManualValidation,
                this::performSomePostProcessing
        );
        validateBasicSomeEntity(resultedEntity, 10);
    }



    private void performSomeManualValidation(SomeEntity entity, Set<ApiValidationError> errors) {
        if (entity.getId() < 100) {
            // Sample Weird Validation.
            errors.add(new ApiValidationError("id", entity.getId(), "Id Less than 100."));
        }
    }

    private SomeEntity performSomePostProcessing(SomeEntity entity) {
        entity.setIrrelevantField(10);
        return entity;
    }

    private static SomeEntity createBasicSomeEntity() {
        return SomeEntity.builder()
                .id(200)
                .name("some")
                .build();
    }

    private static void validateBasicSomeEntity(SomeEntity resultedEntity) {
        validateBasicSomeEntity(resultedEntity, 0);
    }
    private static void validateBasicSomeEntity(SomeEntity resultedEntity, int irrelevantValue) {
        Assertions.assertThat(resultedEntity).isNotNull();
        Assertions.assertThat(resultedEntity.getId()).isNotNull();
        Assertions.assertThat(resultedEntity.getId()).isEqualTo(200);
        Assertions.assertThat(resultedEntity.getName()).isNotNull();
        Assertions.assertThat(resultedEntity.getName()).isEqualTo("some");
        Assertions.assertThat(resultedEntity.getIrrelevantField()).isNotNull();
        Assertions.assertThat(resultedEntity.getIrrelevantField()).isEqualTo(irrelevantValue);
    }

    @Data
    @Builder(toBuilder = true)
    private static class SomeEntity implements Serializable {
        @Id
        @NotNull
        private Integer id;

        @NotNull
        private String name;

        @Builder.Default private Integer irrelevantField = 0;
    }
    private interface SomeEntityRepository extends CrudRepository<SomeEntity, Integer> {
        @Override
        default <S extends SomeEntity> S save(S s) {
            return s;
        }
    }

}
