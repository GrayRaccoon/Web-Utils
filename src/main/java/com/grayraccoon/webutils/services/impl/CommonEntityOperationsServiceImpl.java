package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import com.grayraccoon.webutils.services.CommonEntityOperationsService;
import com.grayraccoon.webutils.services.CustomValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This service will perform and collect all possible errors on an entity before saving it into db.
 * The purpose of this service is avoid duplicate code on Service Layer classes for different entities.
 *
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@Service
public class CommonEntityOperationsServiceImpl implements CommonEntityOperationsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonEntityOperationsServiceImpl.class);

    @Autowired
    private CustomValidatorService customValidatorService;

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            CrudRepository<T, ?> repository,
            T entity) throws CustomApiException {
        return this.validateAndSaveEntity(repository, entity, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            CrudRepository<T, ?> repository,
            T entity,
            ManualValidatorSupplier<T> manualValidation) throws CustomApiException {
        return this.validateAndSaveEntity(repository, entity, manualValidation, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            CrudRepository<T, ?> repository,
            T entity,
            PostProcessEntity<T> postProcessEntity) throws CustomApiException {
        return this.validateAndSaveEntity(repository, entity, null, postProcessEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Serializable> T validateAndSaveEntity(
            CrudRepository<T, ?> repository,
            T entity,
            ManualValidatorSupplier<T> manualValidation,
            PostProcessEntity<T> postProcessEntity) throws CustomApiException {
        LOGGER.info("validateAndSaveEntity()");
        Objects.requireNonNull(repository);
        Objects.requireNonNull(entity);
        LOGGER.info("Validating class: {}", entity.getClass().getName());


        final Set<ApiValidationError> errors = new HashSet<>();

        // Validations from annotations
        errors.addAll(customValidatorService.validateObject(entity));

        // Custom Validations
        if (manualValidation != null) {
            manualValidation.applyManualValidation(entity, errors);
        }

        LOGGER.info("Errors found while validating entity: {}", errors.size());

        if (errors.isEmpty()) {

            // Apply post processing
            if (postProcessEntity != null) {
                entity = postProcessEntity.postProcess(entity);
            }

            return repository.save(entity);
        }else {
            final ApiValidationError firstError = new ArrayList<>(errors).get(0);
            final ApiError apiError = customValidatorService.getApiErrorFromApiValidationErrors(errors)
                    .toBuilder().message(firstError.getMessage()).build();
            throw new CustomApiException(apiError);
        }
    }

}
