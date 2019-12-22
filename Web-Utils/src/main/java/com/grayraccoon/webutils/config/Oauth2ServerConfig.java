package com.grayraccoon.webutils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
@EnableAuthorizationServer
@ConditionalOnProperty(
        value="spring.web-utils.security.oauth2-server.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private DataSource utilsDataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder oauthClientPasswordEncoder;


    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private ApprovalStore approvalStore;

    @Autowired
    private TokenEnhancer tokenEnhancer;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(oauthClientPasswordEncoder);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer
                .jdbc(utilsDataSource).build();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();

        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, accessTokenConverter));

        endpoints.tokenStore(tokenStore)
                .approvalStore(approvalStore)
                .authorizationCodeServices(authorizationCodeServices)
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService);    // we need this to make refresh token work
    }

}
