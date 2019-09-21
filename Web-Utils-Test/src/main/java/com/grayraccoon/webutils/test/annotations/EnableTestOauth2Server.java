package com.grayraccoon.webutils.test.annotations;

import com.grayraccoon.webutils.test.config.TestAuthorizationServerConfig;
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
@Import({TestAuthorizationServerConfig.class})
public @interface EnableTestOauth2Server { }
