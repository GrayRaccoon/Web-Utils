package com.grayraccoon.webutils;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
@TestPropertySource(properties = {
        "spring.web-utils.security.oauth2-server.enabled=false",
        "spring.web-utils.security.oauth2-resource.enabled=false",
        "spring.web-utils.security.oauth2-resource.remote-token-services.enabled=false",
})
public class WebUtilsContextNoAuthIT {
    @Test
    public void contextLoads() { }
}
