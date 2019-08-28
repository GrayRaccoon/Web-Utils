package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.annotations.EnableOauth2Server;
import com.grayraccoon.webutils.annotations.EnableWebUtils;
import com.grayraccoon.webutils.annotations.EnableWebUtilsDataSource;
import com.grayraccoon.webutils.config.factory.YmlPropertyLoaderFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@ComponentScan({
        // Load Test Beans
        "com.grayraccoon.webutils.config.beans"
})
@PropertySource(
        value = ResourceUtils.CLASSPATH_URL_PREFIX + "missing-web-utils.yml",
        ignoreResourceNotFound = true,
        factory = YmlPropertyLoaderFactory.class)
@Configuration
@EnableWebUtils
@EnableOauth2Server
@EnableWebUtilsDataSource
public class WebUtilsAppContext {
}
