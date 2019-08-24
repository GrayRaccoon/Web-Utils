package com.grayraccoon.webutils.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
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

    @NotEmpty
    private String threadNamePrefix;

}
