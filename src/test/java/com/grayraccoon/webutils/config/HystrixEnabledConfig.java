package com.grayraccoon.webutils.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@Configuration
@EnableCircuitBreaker
@EnableAspectJAutoProxy
public class HystrixEnabledConfig {
}
