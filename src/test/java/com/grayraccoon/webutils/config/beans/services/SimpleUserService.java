package com.grayraccoon.webutils.config.beans.services;

import com.grayraccoon.webutils.abstracts.BaseUserService;
import com.grayraccoon.webutils.config.beans.entities.UsersEntity;

import java.util.List;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
public interface SimpleUserService extends BaseUserService {

    List<UsersEntity> findAll();
    UsersEntity saveUser(UsersEntity user);

}
