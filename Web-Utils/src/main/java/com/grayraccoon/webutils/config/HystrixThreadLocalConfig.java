package com.grayraccoon.webutils.config;

import com.grayraccoon.webutils.config.hook.HystrixThreadLocalHook;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Heriberto Reyes Esparza
 */
@Configuration
@ConditionalOnProperty(
        value="spring.web-utils.web.hystrix-share-thread-local",
        havingValue = "true")
public class HystrixThreadLocalConfig {

    @PostConstruct
    public void initialize() {
        // Keeps references of existing Hystrix plugins.
        final HystrixConcurrencyStrategy concurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
        final HystrixEventNotifier eventNotifier        = HystrixPlugins.getInstance().getEventNotifier();
        final HystrixMetricsPublisher metricsPublisher     = HystrixPlugins.getInstance().getMetricsPublisher();
        final HystrixPropertiesStrategy propertiesStrategy   = HystrixPlugins.getInstance().getPropertiesStrategy();

        HystrixPlugins.reset();
        HystrixPlugins.getInstance().registerConcurrencyStrategy(concurrencyStrategy);
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        HystrixPlugins.getInstance().registerCommandExecutionHook(HystrixThreadLocalHook.getInstance());
    }

}
