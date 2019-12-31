package com.grayraccoon.webutils;

import com.grayraccoon.webutils.config.WebUtilsAppContextOauth2ResourceServerOnly;
import com.grayraccoon.webutils.test.auth.Oauth2Utils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(SpringExtension.class)
@DirtiesContext
@SpringBootTest(
        classes = WebUtilsAppContextOauth2ResourceServerOnly.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {
        "spring.web-utils.security.oauth2-server.enabled=false",
        "spring.web-utils.security.oauth2-resource.remote-token-services.enabled=true",
})
public class WebUtilsContextOauth2ResourceServerOnlyIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void contextLoads() { }

    @Test
    public void getExtraInfo_Success() throws Exception {
        final String access_token = Oauth2Utils.getUserAccessToken(mockMvc, "admin", "password");

        mockMvc.perform(get("/ws/authenticated/userId")
                .header("Authorization", "Bearer " + access_token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId", Matchers.notNullValue()))
        ;
    }

}
