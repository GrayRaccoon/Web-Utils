package com.grayraccoon.webutils.test.config;

import com.grayraccoon.webutils.test.annotations.EnableTestOauth2Server;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Heriberto Reyes Esparza
 */
@ComponentScan({
        // Load Test Beans
        "com.grayraccoon.webutils.test.config",
        "com.grayraccoon.webutils.test.ws",
})
@SpringBootApplication
@EnableTestOauth2Server
public class WebUtilsTestsAppContext {
}
