package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.annotations.EnableOauth2Server;
import com.grayraccoon.webutils.annotations.EnableWebUtils;
import com.grayraccoon.webutils.annotations.EnableWebUtilsDataSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@ComponentScan({
        // Load Test Beans
        "com.grayraccoon.webutils.config.beans"
})
@Configuration
@EnableWebUtils
@EnableOauth2Server
@EnableWebUtilsDataSource
public class WebUtilsAppContext {
}
