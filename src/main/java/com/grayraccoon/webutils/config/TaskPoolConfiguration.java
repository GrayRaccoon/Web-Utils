package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.config.properties.AsyncTaskExecutorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
@Configuration
@EnableAsync
@EnableScheduling
public class TaskPoolConfiguration implements AsyncConfigurer {

    private AsyncTaskExecutorProperties asyncTaskExecutorProperties;

    @Autowired
    public void setAsyncTaskExecutorProperties(AsyncTaskExecutorProperties asyncTaskExecutorProperties) {
        this.asyncTaskExecutorProperties = asyncTaskExecutorProperties;
    }

    @Override
    public Executor getAsyncExecutor() {
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(asyncTaskExecutorProperties.getMaxPoolSize());
        threadPoolTaskExecutor.setCorePoolSize(asyncTaskExecutorProperties.getCorePoolSize());
        threadPoolTaskExecutor.setQueueCapacity(asyncTaskExecutorProperties.getQueueCapacity());
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix(asyncTaskExecutorProperties.getThreadNamePrefix());
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
