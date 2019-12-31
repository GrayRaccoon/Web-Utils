package com.grayraccoon.webutils.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
@Order(112)
public class TestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.signing-key:test123}")
    private String signingKey;

    @Value("${security.security-realm:test realm}")
    private String securityRealm;

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .and().cors()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic().realmName(securityRealm)
                .and().csrf().disable()
        ;
    }

    @Bean
    @Override
    @ConditionalOnMissingBean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(
                        passwordEncoder().encode("password")
                )
                .roles("USER", "ADMIN")
        .and()
                .withUser("user")
                .password(
                        passwordEncoder().encode("password")
                )
                .roles("USER")
        ;
    }


    @Bean
    @ConditionalOnMissingBean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        converter.setAccessTokenConverter(customAccessTokenConverter());
        return converter;
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenStore testTokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultTokenServices testTokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(testTokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    @Primary
    public CustomTokenEnhancer testTokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    public CustomAccessTokenConverter customAccessTokenConverter() {
        return new CustomAccessTokenConverter();
    }

    protected static class CustomAccessTokenConverter extends DefaultAccessTokenConverter {
        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
            final OAuth2Authentication authentication = super.extractAuthentication(claims);
            authentication.setDetails(claims);
            return authentication;
        }
    }
    protected static class CustomTokenEnhancer implements TokenEnhancer {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
            final Map<String, Object> additionalInfo = new HashMap<>();
            final String username = oAuth2Authentication.getName();
            if(username.equals("admin")) {
                final OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();

                additionalInfo.put("userId", "01e3d8d5-1119-4111-b3d0-be6562ca5914");
                additionalInfo.put("username", "admin");
                additionalInfo.put("email", "admin@admin.com");
                additionalInfo.put("clientId", oAuth2Request.getClientId());

                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);
            }
            return oAuth2AccessToken;
        }
    }

}
