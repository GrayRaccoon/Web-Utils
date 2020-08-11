package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.grayraccoon.webutils.services.CommonEntityOperationsService;
import com.grayraccoon.webutils.services.CustomValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This service will perform and collect all possible errors on an entity before saving it into db.
 * The purpose of this service is avoid duplicate code on Service Layer classes for different entities.
 *
 * @author Heriberto Reyes Esparza
 */
@Service
@ConditionalOnClass(JpaRepository.class)
public class CommonEntityOperationsServiceImpl implements CommonEntityOperationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonEntityOperationsServiceImpl.class);

    private final CustomValidatorService customValidatorService;

    @Autowired
    public CommonEntityOperationsServiceImpl(
            final CustomValidatorService customValidatorService) {
        this.customValidatorService = customValidatorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            JpaRepository<T, ?> repository,
            T entity) throws CustomApiException {
        return this.validateAndSaveEntity(repository, entity, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            JpaRepository<T, ?> repository,
            T entity,
            ManualValidatorSupplier<T> manualValidation) throws CustomApiException {
        return this.validateAndSaveEntity(repository, entity, manualValidation, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            JpaRepository<T, ?> repository,
            T entity,
            PostProcessEntity<T> postProcessEntity) throws CustomApiException {
        return this.validateAndSaveEntity(repository, entity, null, postProcessEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            JpaRepository<T, ?> repository,
            T entity,
            ManualValidatorSupplier<T> manualValidation,
            PostProcessEntity<T> postProcessEntity) throws CustomApiException {
        LOGGER.trace("Performing Validation previous to save()");
        Objects.requireNonNull(repository);
        Objects.requireNonNull(entity);
        LOGGER.trace("Validating: {}", entity);

        final Set<ApiValidationError> errors = new HashSet<>();

        // Validations from annotations
        errors.addAll(customValidatorService.validateObject(entity));

        // Custom Validations
        if (manualValidation != null) {
            manualValidation.applyManualValidation(entity, errors);
        }

        if (errors.isEmpty()) {
            LOGGER.info("No errors found validating entity.");

            // Apply post processing
            if (postProcessEntity != null) {
                entity = postProcessEntity.postProcess(entity);
            }

            return repository.saveAndFlush(entity);
        }else {
            LOGGER.warn("Errors found while validating entity: {}", errors.size());

            final ApiError apiError = customValidatorService.getApiErrorFromApiValidationErrors(errors);
            throw new CustomApiException(apiError);
        }
    }

}
