package com.grayraccoon.webutils;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Heriberto Reyes Esparza
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
@TestPropertySource(properties = {
        "spring.web-utils.security.oauth2-server.enabled=false",
        "spring.web-utils.security.oauth2-resource.remote-token-services.enabled=true",
})
public class WebUtilsContextOauth2ResourceServerOnlyIT {
    @Test
    public void contextLoads() { }
}
