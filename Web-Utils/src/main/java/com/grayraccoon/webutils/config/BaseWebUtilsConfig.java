package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.config.factory.YmlPropertyLoaderFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

/**
 * @author Heriberto Reyes Esparza
 */
@Import({
        TaskPoolConfiguration.class,
        AsynchronousSpringEventsConfig.class,
        WebConfig.class,
        LanguageConfig.class,
        HystrixThreadLocalConfig.class
})
@SpringBootApplication(
        // Load Advice and Services
        scanBasePackages = {
                "com.grayraccoon.webutils.advice",
                "com.grayraccoon.webutils.config.properties",
                "com.grayraccoon.webutils.services"
        }
)
@PropertySource(value = ResourceUtils.CLASSPATH_URL_PREFIX + "web-utils.yml",
        factory = YmlPropertyLoaderFactory.class)
public class BaseWebUtilsConfig {
}
