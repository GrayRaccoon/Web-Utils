package com.grayraccoon.webutils.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Heriberto Reyes Esparza
 */
@Data
@Validated
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "spring.web-utils.tasks")
public class AsyncTaskExecutorProperties {

    @NotNull
    @Min(1)
    private Integer maxPoolSize;

    @NotNull
    @Min(1)
    private Integer corePoolSize;

    @NotNull
    @Min(0)
    private Integer queueCapacity;

    @NotNull
    @Min(0)
    private Integer scheduledTasksPoolSize;

    @NotEmpty
    private String threadNamePrefix;

    @NotEmpty
    private String scheduledTasksPoolNamePrefix;

}
