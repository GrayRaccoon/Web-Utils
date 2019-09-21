package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.test.annotations.EnableTestOauth2Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Heriberto Reyes Esparza
 */
@Import(WebUtilsAppContext.class)
@Configuration
@EnableTestOauth2Server
public class WebUtilsAppContextOauth2ResourceServerOnly { }
