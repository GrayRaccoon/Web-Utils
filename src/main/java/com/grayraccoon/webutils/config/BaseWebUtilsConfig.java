package com.grayraccoon.webutils.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

import com.grayraccoon.webutils.config.factory.YmlPropertyLoaderFactory;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@Import({
        TaskPoolConfiguration.class,
        WebConfig.class
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
