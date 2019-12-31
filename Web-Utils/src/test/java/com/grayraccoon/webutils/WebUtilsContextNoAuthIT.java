package com.grayraccoon.webutils;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
public class WebUtilsContextNoAuthIT extends AbstractJUnit4SpringContextTests {
    @Test
    public void contextLoads() { }
}
