package com.grayraccoon.webutils.config.beans.services;

import com.grayraccoon.webutils.abstracts.BaseUserService;
import com.grayraccoon.webutils.config.beans.entities.UsersEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
public interface SimpleUserService extends BaseUserService {

    List<UsersEntity> findAll();
    UsersEntity saveUser(UsersEntity user);
    UsersEntity findUserById(UUID userId);

}
