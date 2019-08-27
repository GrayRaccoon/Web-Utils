package com.grayraccoon.webutils.config.beans.services.impl;

import com.grayraccoon.webutils.config.beans.entities.UsersEntity;
import com.grayraccoon.webutils.config.beans.repository.UsersRepository;
import com.grayraccoon.webutils.config.beans.services.SimpleUserService;
import com.grayraccoon.webutils.errors.ApiError;
import com.grayraccoon.webutils.errors.ApiValidationError;
import com.grayraccoon.webutils.exceptions.CustomApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SimpleUserServiceImpl implements SimpleUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUserServiceImpl.class);

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public List<UsersEntity> findAll() {
        return usersRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public UsersEntity findByUsernameOrEmail(String query) {
        LOGGER.info("findUserByUsernameOrEmail: {}", query);
        UsersEntity u = usersRepository.findFirstByEmailOrUsername(query, query);
        if (u == null) {
            throw new CustomApiException(
                    ApiError.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .subError(new ApiValidationError(query))
                            .build()
            );
        }
        return u;
    }
}
