package com.grayraccoon.webutils.abstracts;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Heriberto Reyes Esparza
 */
public interface BaseUserDetails extends Serializable {
    UUID getUserId();
    String getUsername();
    String getEmail();
}
