package com.grayraccoon.webutils.test.auth;

import com.grayraccoon.webutils.test.config.WebUtilsTestsAppContext;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Heriberto Reyes Esparza
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebUtilsTestsAppContext.class)
public class Oauth2UtilsIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void contextLoads() { }

    @Test
    public void getUserAccessToken_admin_Success() throws Exception {
        String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "admin","password");

        Assertions.assertThat(access_token).isNotNull();
        Assertions.assertThat(access_token).isNotEmpty();
    }

    @Test
    public void getUserAccessToken_user_Success() throws Exception {
        String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "user","password");

        Assertions.assertThat(access_token).isNotNull();
        Assertions.assertThat(access_token).isNotEmpty();
    }

    @Test
    public void getOauthTestAuthentication() {
        final Authentication authentication = Oauth2Utils.getOauthTestAuthentication();

        Assertions.assertThat(authentication).isNotNull();
    }

    @Test
    public void getExtraInfo_Success() throws Exception {
        final String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "admin", "password");

        mockMvc.perform(get("/ws/authenticated/principal")
                .header("Authorization", "Bearer " + access_token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.username", Matchers.notNullValue()))
                .andExpect(jsonPath("$.email", Matchers.notNullValue()))
        ;
    }

}
