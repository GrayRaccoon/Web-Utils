package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import lombok.Builder;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Id;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(MockitoExtension.class)
public class CommonEntityOperationsServiceImplTests {

    @InjectMocks
    private CommonEntityOperationsServiceImpl commonEntityOperationsService;

    @Mock
    private CustomValidatorServiceImpl customValidatorService;

    @Mock
    private SomeEntityRepository someEntityRepository;


    @BeforeEach
    public void setUp() throws Exception {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        Mockito.when(customValidatorService.validateObject(ArgumentMatchers.any())).thenCallRealMethod();
        Mockito.when(customValidatorService.getValidator()).thenReturn(factory.getValidator());
    }

    @Test
    public void validateAndSaveEntity_NoCallbacks_Success() {
        mockSave();
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity()
        );
        validateBasicSomeEntity(resultedEntity);
    }

    @Test
    public void validateAndSaveEntity_NoCallbacks_Fail() {
        mockApiErrorToConstraint();
        mockApiErrorFromValidations();
        final SomeEntity invalidEntity = SomeEntity.builder()
                .id(1)
                .build();

        org.junit.jupiter.api.Assertions.assertThrows(CustomApiException.class, () ->
                commonEntityOperationsService.validateAndSaveEntity(
                        this.someEntityRepository, invalidEntity));
    }

    @Test
    public void validateAndSaveEntity_WithManualTest_Success() {
        mockSave();
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity(),
                this::performSomeManualValidation
        );
        validateBasicSomeEntity(resultedEntity);
    }

    @Test
    public void validateAndSaveEntity_WithManualTest_Fail() {
        mockApiErrorFromValidations();
        org.junit.jupiter.api.Assertions.assertThrows(CustomApiException.class, () ->
                commonEntityOperationsService.validateAndSaveEntity(
                        this.someEntityRepository,
                        createBasicSomeEntity().toBuilder().id(1).build(),
                        this::performSomeManualValidation
                ));
    }

    @Test
    public void validateAndSaveEntity_WithPostProcessing_Success() {
        mockSave();
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity(),
                this::performSomePostProcessing
        );
        validateBasicSomeEntity(resultedEntity, 10);
    }

    @Test
    public void validateAndSaveEntity_WithManualTestAndPostProcessing_Success() {
        mockSave();
        final SomeEntity resultedEntity = commonEntityOperationsService.validateAndSaveEntity(
                this.someEntityRepository,
                createBasicSomeEntity(),
                this::performSomeManualValidation,
                this::performSomePostProcessing
        );
        validateBasicSomeEntity(resultedEntity, 10);
    }

    @Test
    public void validateAndSaveEntity_WithManualTestAndPostProcessing_Fail() {
        mockApiErrorFromValidations();

        org.junit.jupiter.api.Assertions.assertThrows(CustomApiException.class, () ->
                commonEntityOperationsService.validateAndSaveEntity(
                        this.someEntityRepository,
                        createBasicSomeEntity().toBuilder().id(1).build(),
                        this::performSomeManualValidation,
                        this::performSomePostProcessing
                ));
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


    private void mockSave() {
        Mockito.when(someEntityRepository.saveAndFlush(ArgumentMatchers.any(SomeEntity.class))).thenCallRealMethod();
    }

    private void mockApiErrorToConstraint() {
        Mockito.when(customValidatorService
                .getApiValidationErrorFromConstraintViolation(ArgumentMatchers.any()))
                .thenCallRealMethod();
    }

    private void mockApiErrorFromValidations() {
        Mockito.when(customValidatorService.getApiErrorFromApiValidationErrors(ArgumentMatchers.anySet()))
                .thenCallRealMethod();
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
    private interface SomeEntityRepository extends JpaRepository<SomeEntity, Integer> {
        @Override
        default <S extends SomeEntity> S saveAndFlush(final S s) {
            return s;
        }
    }

}
