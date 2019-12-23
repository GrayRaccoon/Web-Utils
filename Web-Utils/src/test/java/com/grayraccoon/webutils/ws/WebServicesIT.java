package com.grayraccoon.webutils.ws;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import com.grayraccoon.webutils.test.auth.Oauth2Utils;
import org.flywaydb.core.Flyway;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * @author Heriberto Reyes Esparza
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
public class WebServicesIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebServicesIT.class);

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private Flyway flyway;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();

        // We need to reapply migrations for tests to work
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void contextLoads() {}

    @Test
    public void findAllUsers_Success_Test() throws Exception {
        final String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "admin","password");

        mockMvc.perform(get("/ws/secured/users")
                .header("Authorization", "Bearer " + access_token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].userId", is("01e3d8d5-1119-4111-b3d0-be6562ca5914")))
                .andExpect(jsonPath("$.data[0].username", is("admin")))
                .andExpect(jsonPath("$.data[1].userId", is("01e3d8d5-1119-4111-b3d0-be6562ca5901")))
                .andExpect(jsonPath("$.data[1].username", is("user")))
        ;
    }

    @Test
    public void findAllUsers_Unauthorized_Test() throws Exception {
        mockMvc.perform(get("/ws/secured/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())   //  401
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data").doesNotExist())
        ;
    }

    @Test
    public void findAllUsers_Forbidden_Test() throws Exception {
        final String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "user","password");

        mockMvc.perform(get("/ws/secured/users")
                .header("Authorization", "Bearer " + access_token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())   //  403
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", Matchers.notNullValue()))
                .andExpect(jsonPath("$.data").doesNotExist())
        ;
    }

    @Test
    public void findUser_NotFound_Test() throws Exception {
        final String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "admin","password");

        final String user_userId = "01e3d8d5-0009-4111-b3d0-be6562ca5922";

        mockMvc.perform(get(String.format("/ws/secured/users/%s", user_userId))
                .header("Authorization", "Bearer " + access_token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())   //  404
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error.message", is("Not Found")))
                .andExpect(jsonPath("$.error.subErrors", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error.subErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.error.subErrors[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error.subErrors[0].rejectedValue", Matchers.notNullValue()))
                .andExpect(jsonPath("$.error.subErrors[0].rejectedValue", is(user_userId)))
        ;
    }


}
