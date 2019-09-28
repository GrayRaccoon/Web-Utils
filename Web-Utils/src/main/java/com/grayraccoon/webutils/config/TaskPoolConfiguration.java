package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.config.properties.AsyncTaskExecutorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
@EnableAsync
@EnableScheduling
public class TaskPoolConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    private AsyncTaskExecutorProperties asyncTaskExecutorProperties;

    @Autowired
    public void setAsyncTaskExecutorProperties(
            AsyncTaskExecutorProperties asyncTaskExecutorProperties) {
        this.asyncTaskExecutorProperties = asyncTaskExecutorProperties;
    }

    @Bean("asyncExecutor")
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

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(asyncTaskExecutorProperties.getScheduledTasksPoolSize());
        threadPoolTaskScheduler.setThreadNamePrefix(
                asyncTaskExecutorProperties.getScheduledTasksPoolNamePrefix());
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskScheduler());
    }

}
