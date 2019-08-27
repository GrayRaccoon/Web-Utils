package com.grayraccoon.webutils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.Map;

@Configuration
@EnableResourceServer
@ConditionalOnProperty(
        value="spring.web-utils.security.oauth2-resource.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Value("${spring.web-utils.security.oauth2-resource.local-resource-id}")
    private String localResourceId;

    @Value("${spring.web-utils.security.oauth2-resource.secured-matchers}")
    private String[] securedMatchers;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(localResourceId).tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                // this is to allow display in iframe
                .headers().frameOptions().disable()
                .and()
                .cors().and()
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/actuator", "/actuator/health", "/actuator/info", "/actuator/hystrix.stream").permitAll()
                .antMatchers("/actuator/**").authenticated()

                .antMatchers(securedMatchers).authenticated()

                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().permitAll();
    }

    public static Map<String, Object> getExtraInfo(Authentication auth) {
        OAuth2AuthenticationDetails oauthDetails
                = (OAuth2AuthenticationDetails) auth.getDetails();
        return (Map<String, Object>) oauthDetails
                .getDecodedDetails();
    }

}
