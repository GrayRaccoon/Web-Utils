package com.grayraccoon.webutils;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
public class WebUtilsContextIT extends AbstractJUnit4SpringContextTests {
    @Test
    public void contextLoads() { }
}
