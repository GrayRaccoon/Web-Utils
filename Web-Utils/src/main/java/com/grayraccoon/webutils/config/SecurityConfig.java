package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.abstracts.BaseUserService;
import com.grayraccoon.webutils.config.components.CustomAccessTokenConverter;
import com.grayraccoon.webutils.config.components.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * @author Heriberto Reyes Esparza
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Import(EncodersConfig.class)
@Configuration
public class SecurityConfig {

    @Order(1)
    @Configuration
    @ConditionalOnProperty(
            value="spring.web-utils.security.actuator.enabled",
            havingValue = "true", matchIfMissing = false)
    public static class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private PasswordEncoder userPasswordEncoder;

        @Value("${spring.web-utils.security.actuator.security-realm:someDefaultValue.}")
        private String securityRealm;

        @Value("${spring.web-utils.security.actuator.user}")
        private String actuatorUsername;

        @Value("${spring.web-utils.security.actuator.password}")
        private String actuatorPassword;

        @Value("${management.endpoints.web.base-path}")
        private String actuatorPath;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser(actuatorUsername)
                    .password(userPasswordEncoder.encode(actuatorPassword))
                    .roles("ACTUATOR")
                    .and()
                    .passwordEncoder(userPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .requestMatcher(ActuatorRequestMatcher.getForPath(actuatorPath))
                    .authorizeRequests()
                    .antMatchers(actuatorPath, actuatorPath + "/health",
                            actuatorPath + "/info", actuatorPath + "/hystrix.stream").permitAll()
                    .antMatchers(actuatorPath + "/**").authenticated()

                    .and().httpBasic().realmName(securityRealm)

                    .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ;
        }

        /**
         * Quick Request matcher to detect when current url contains '/actuator'.
         */
        private static class ActuatorRequestMatcher implements RequestMatcher {
            private String actuatorPath = "/actuator";

            @Override
            public boolean matches(final HttpServletRequest httpRequest) {
                // Actuator Request Matcher
                final String requestUri = httpRequest.getRequestURI();
                return requestUri.contains(actuatorPath);
            }
            public static ActuatorRequestMatcher getForPath(final String actuatorPath) {
                final ActuatorRequestMatcher actuatorRequestMatcher = new ActuatorRequestMatcher();
                actuatorRequestMatcher.actuatorPath = actuatorPath;
                return actuatorRequestMatcher;
            }
        }
    }


    @Configuration
    public static class Oauth2SecurityConfig extends WebSecurityConfigurerAdapter {

        @Value("${spring.web-utils.security.oauth2-server.enabled:false}")
        private boolean isOauth2ServerEnabled;

        @Value("${spring.web-utils.security.oauth2-server.signing-key:someDefaultValue.}")
        private String signingKey;

        @Value("${spring.web-utils.security.oauth2-resource.remote-token-services.client-id:someDefaultValue.}")
        private String tokenServicesClientId;

        @Value("${spring.web-utils.security.oauth2-resource.remote-token-services.client-secret:someDefaultValue.}")
        private String tokenServicesClientSecret;

        @Value("${spring.web-utils.security.oauth2-resource.remote-token-services.check-token-url:someDefaultValue.}")
        private String tokenServicesCheckTokenUrl;


        @Autowired(required = false)
        private DataSource utilsDataSource;

        @Autowired(required = false)
        private BaseUserService baseUserService;

        @Autowired(required = false)
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

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    // this is to allow display in iframe
                    .headers().frameOptions().disable()
                    .and().cors()

                    .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    .and().csrf().disable()
            ;
        }

        // Oauth2 Server Config Starts

        @Bean
        @ConditionalOnMissingBean
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

        @Bean
        @ConditionalOnProperty(
                value="spring.web-utils.security.oauth2-server.enabled",
                havingValue = "true",
                matchIfMissing = false)
        public JwtAccessTokenConverter accessTokenConverter() {
            final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setSigningKey(signingKey);
            converter.setAccessTokenConverter(customAccessTokenConverter());
            return converter;
        }


        @Bean
        @ConditionalOnProperty(
                value="spring.web-utils.security.oauth2-server.enabled",
                havingValue = "true",
                matchIfMissing = false)
        public ApprovalStore approvalStore() {
            final ApprovalStore approvalStore = new JdbcApprovalStore(utilsDataSource);
            return approvalStore;
        }

        @Bean
        @ConditionalOnProperty(
                value="spring.web-utils.security.oauth2-server.enabled",
                havingValue = "true",
                matchIfMissing = false)
        public TokenStore tokenStore() {
            return new JdbcTokenStore(utilsDataSource);
        }

        @Bean
        @ConditionalOnProperty(
                value="spring.web-utils.security.oauth2-server.enabled",
                havingValue = "true",
                matchIfMissing = false)
        public JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(utilsDataSource);
        }

        @Bean
        @Primary
        @ConditionalOnProperty(
                value="spring.web-utils.security.oauth2-server.enabled",
                havingValue = "true",
                matchIfMissing = false)
        //Making this primary to avoid any accidental duplication with another token service instance of the same name
        public DefaultTokenServices tokenServices() {
            final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

            defaultTokenServices.setTokenEnhancer(accessTokenConverter());
            defaultTokenServices.setTokenStore(tokenStore());

            defaultTokenServices.setSupportRefreshToken(true);
            defaultTokenServices.setReuseRefreshToken(false);
            return defaultTokenServices;
        }

        @Bean
        @ConditionalOnProperty(
                value="spring.web-utils.security.oauth2-server.enabled",
                havingValue = "true",
                matchIfMissing = false)
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
            final RemoteTokenServices tokenService = new RemoteTokenServices();

            tokenService.setCheckTokenEndpointUrl( // OAuth2 Server Url
                    tokenServicesCheckTokenUrl);

            tokenService.setAccessTokenConverter(customAccessTokenConverter());

            // Client Id n Secret from oauth_client_details table
            tokenService.setClientId(tokenServicesClientId);
            tokenService.setClientSecret(tokenServicesClientSecret);

            return tokenService;
        }

    }


    @Bean
    @ConditionalOnProperty(
            value="spring.web-utils.security.oauth2-resource.enabled",
            havingValue = "true",
            matchIfMissing = false)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
