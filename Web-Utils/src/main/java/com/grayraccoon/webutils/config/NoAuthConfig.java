package com.grayraccoon.webutils.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Heriberto Reyes Esparza
 */
@Order(18)
@Configuration
@EnableWebSecurity
public class NoAuthConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .and().cors()
                .and().authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().permitAll()
                .and().csrf().disable();
    }

}
