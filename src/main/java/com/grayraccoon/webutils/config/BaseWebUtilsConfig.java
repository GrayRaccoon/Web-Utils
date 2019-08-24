package com.grayraccoon.webutils.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

import com.grayraccoon.webutils.config.factory.YmlPropertyLoaderFactory;

@ComponentScan(basePackages = {
        "com.grayraccoon.webutils.advice",
        "com.grayraccoon.webutils.config.properties",
        "com.grayraccoon.webutils.services"
})    // Load Advice and Services
@Import(TaskPoolConfiguration.class)
@SpringBootApplication
@PropertySource(value = ResourceUtils.CLASSPATH_URL_PREFIX + "web-utils.yml",
        factory = YmlPropertyLoaderFactory.class)
public class BaseWebUtilsConfig {
}
