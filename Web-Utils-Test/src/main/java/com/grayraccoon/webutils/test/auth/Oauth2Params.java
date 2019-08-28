package com.grayraccoon.webutils.test.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Heriberto Reyes Esparza
 */
@Data
public class Oauth2Params implements Serializable {
    private String grant_type = "password";
    private String client_id = "test-client-id";
    private String secret_id = "test-client-secret";
}
