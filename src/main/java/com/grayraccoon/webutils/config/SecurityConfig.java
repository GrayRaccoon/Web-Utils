package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.abstracts.BaseUserService;
import com.grayraccoon.webutils.config.components.CustomAccessTokenConverter;
import com.grayraccoon.webutils.config.components.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.context.request.RequestContextListener;

import javax.sql.DataSource;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.web-utils.security.oauth2-server.signing-key:someDefaultValue.}")
    private String signingKey;

    @Value("${spring.web-utils.security.oauth2-server.security-realm:someDefaultValue.}")
    private String securityRealm;

    @Value("${spring.web-utils.security.oauth2-server.enabled:false}")
    private boolean isOauth2ServerEnabled;

    @Value("${spring.web-utils.security.oauth2-resource.remote-token-services.client-id:someDefaultValue.}")
    private String tokenServicesClientId;

    @Value("${spring.web-utils.security.oauth2-resource.remote-token-services.client-secret:someDefaultValue.}")
    private String tokenServicesClientSecret;

    @Value("${spring.web-utils.security.oauth2-resource.remote-token-services.check-token-url:someDefaultValue.}")
    private String tokenServicesCheckTokenUrl;


    @Autowired
    private DataSource utilsDataSource;

    @Autowired
    private BaseUserService baseUserService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder userPasswordEncoder;


    // Common Config Starts

    @Bean
    protected CustomAccessTokenConverter customAccessTokenConverter() {
        return new CustomAccessTokenConverter();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }


    // Oauth2 Server Config Starts

    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if (!isOauth2ServerEnabled) {
            return;
        }
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(userPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!isOauth2ServerEnabled) {
            return;
        }
        http.requestMatchers()
                .and().cors()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic().realmName(securityRealm)
                .and().csrf().disable()
        ;
    }

    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        converter.setAccessTokenConverter(customAccessTokenConverter());
        return converter;
    }


    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public ApprovalStore approvalStore() {
        ApprovalStore approvalStore = new JdbcApprovalStore(utilsDataSource);
        return approvalStore;
    }

    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public TokenStore tokenStore() {
        return new JdbcTokenStore(utilsDataSource);
    }

    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(utilsDataSource);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    //Making this primary to avoid any accidental duplication with another token service instance of the same name
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        defaultTokenServices.setTokenStore(tokenStore());

        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-server.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer(baseUserService);
    }



    // Oauth2 Resource Only Config Starts

    @Bean
    @Primary
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-resource.remote-token-services.enabled",
            havingValue = "true",
            matchIfMissing = false)
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices tokenService = new RemoteTokenServices();

        tokenService.setCheckTokenEndpointUrl( // OAuth2 Server Url
                tokenServicesCheckTokenUrl);

        tokenService.setAccessTokenConverter(customAccessTokenConverter());

        // Client Id n Secret from oauth_client_details table
        tokenService.setClientId(tokenServicesClientId);
        tokenService.setClientSecret(tokenServicesClientSecret);

        return tokenService;
    }


    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-resource.enabled",
            havingValue = "true",
            matchIfMissing = true)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
