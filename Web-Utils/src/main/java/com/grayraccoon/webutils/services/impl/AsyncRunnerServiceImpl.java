package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.services.AsyncRunnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * This class should allow spring to create a Proxy to run the async tasks
 * without the need of exposing our async jobs as public methods on the actual impl.
 *
 * @author Heriberto Reyes Esparza
 */
@Service
public class AsyncRunnerServiceImpl implements AsyncRunnerService {

    private static Logger LOGGER = LoggerFactory.getLogger(AsyncRunnerServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void doAsync(Runnable asyncJob) {
        Objects.requireNonNull(asyncJob);
        asyncJob.run();
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public <T> CompletableFuture<T> doAsync(Supplier<T> asyncJob) {
        Objects.requireNonNull(asyncJob);
        final T result = asyncJob.get();
        return CompletableFuture.completedFuture(result);
    }

}
