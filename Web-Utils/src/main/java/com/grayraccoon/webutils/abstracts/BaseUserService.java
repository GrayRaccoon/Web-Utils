package com.grayraccoon.webutils.abstracts;

/**
 * @author Heriberto Reyes Esparza
 */
public interface BaseUserService {
    BaseUserDetails findByUsernameOrEmail(String query);
}
