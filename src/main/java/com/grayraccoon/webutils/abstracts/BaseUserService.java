package com.grayraccoon.webutils.abstracts;

public interface BaseUserService {
    BaseUserDetails findByUsernameOrEmail(String query);
}
