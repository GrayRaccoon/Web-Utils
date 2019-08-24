package com.grayraccoon.webutils.annotations;

import com.grayraccoon.webutils.config.BaseWebUtilsConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(BaseWebUtilsConfig.class)
public @interface EnableWebUtils { }
