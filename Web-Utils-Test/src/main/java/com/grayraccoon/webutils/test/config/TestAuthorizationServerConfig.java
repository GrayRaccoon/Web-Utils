package com.grayraccoon.webutils.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
@Import(TestSecurityConfig.class)
@EnableAuthorizationServer
public class TestAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final JwtAccessTokenConverter accessTokenConverter;
    private final TokenEnhancer testTokenEnhancer;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TestAuthorizationServerConfig(
            TokenStore tokenStore,
            JwtAccessTokenConverter accessTokenConverter,
            TokenEnhancer testTokenEnhancer,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
        this.testTokenEnhancer = testTokenEnhancer;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.inMemory()
                .withClient("test-client-id")
                .secret(passwordEncoder.encode("test-client-secret"))
                .resourceIds("test-resource-id")
                .scopes("read", "write", "user_info")
                .authorizedGrantTypes("password","authorization_code","client_credentials")
                .autoApprove(true)
                .and().build();
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(testTokenEnhancer, accessTokenConverter));
        endpoints.tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager);
    }

}
