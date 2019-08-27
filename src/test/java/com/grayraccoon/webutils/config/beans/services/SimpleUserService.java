package com.grayraccoon.webutils.config.beans.services;

import com.grayraccoon.webutils.abstracts.BaseUserService;
import com.grayraccoon.webutils.config.beans.entities.UsersEntity;

import java.util.List;

public interface SimpleUserService extends BaseUserService {

    List<UsersEntity> findAll();

}
