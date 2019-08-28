package com.grayraccoon.webutils.annotations;

import com.grayraccoon.webutils.config.Oauth2ServerConfig;
import com.grayraccoon.webutils.config.ResourceServerConfig;
import com.grayraccoon.webutils.config.SecurityConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Heriberto Reyes Esparza
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({Oauth2ServerConfig.class, ResourceServerConfig.class, SecurityConfig.class})
public @interface EnableOauth2Server { }
