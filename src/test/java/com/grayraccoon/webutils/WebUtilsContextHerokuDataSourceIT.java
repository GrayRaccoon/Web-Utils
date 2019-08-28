package com.grayraccoon.webutils;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
@TestPropertySource(properties = {
        "spring.web-utils.data.heroku.jdbc-url-template=jdbc:_DB_TYPE_:_HOST_:_DB_;MODE=PostgreSQL;" +
                "INIT=CREATE SCHEMA IF NOT EXISTS _SCHEMA_",
        "spring.web-utils.data.heroku.url-env-name=HEROKU_DB_URL",
        "spring.web-utils.data.heroku.db-schema=public",
        "spring.web-utils.data.heroku.db-type=h2",
        "HEROKU_DB_URL=postgres://sa:sa@mem:5432/cloud_db",
})
@ActiveProfiles("heroku")
public class WebUtilsContextHerokuDataSourceIT extends AbstractJUnit4SpringContextTests {

    @Test
    public void contextLoads() { }

}
