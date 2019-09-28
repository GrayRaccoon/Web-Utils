package com.grayraccoon.webutils.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.Executor;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
public class AsynchronousSpringEventsConfig {

    private final Executor getAsyncExecutor;

    @Autowired
    public AsynchronousSpringEventsConfig(
            Executor asyncExecutor) {
        this.getAsyncExecutor = asyncExecutor;
    }

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster
                = new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(getAsyncExecutor);
        return eventMulticaster;
    }

}
