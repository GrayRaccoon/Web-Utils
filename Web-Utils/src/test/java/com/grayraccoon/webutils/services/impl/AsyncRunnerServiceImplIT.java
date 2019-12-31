package com.grayraccoon.webutils.services.impl;

import com.grayraccoon.webutils.config.WebUtilsAppContext;
import com.grayraccoon.webutils.services.AsyncRunnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Heriberto Reyes Esparza
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WebUtilsAppContext.class)
public class AsyncRunnerServiceImplIT extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRunnerServiceImplIT.class);

    @Autowired
    private AsyncRunnerService asyncRunnerService;

    @BeforeEach
    public void setUp() throws Exception {}

    @Test
    public void contextLoads() {
        Assertions.assertThat(asyncRunnerService).isNotNull();
    }

    @Test
    public void doSomeWorkWithoutArgsAsync_Success() {
        this.doSomeWorkWithoutArgsAsync();
    }

    @Test
    public void doSomeWorkWithArgsAsync_Success() {
        this.doSomeWorkWithArgsAsync(3, 4);
    }

    @Test
    public void doSomeWorkWithoutArgsAndReturnAsync_Success() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = this.doSomeWorkWithoutArgsAndReturnAsync();
        future.join();
        final Integer result = future.get();
        LOGGER.info("Future Result: {}", result);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(0);
    }

    @Test
    public void doSomeWorkWithArgsAndReturnAsync_Success() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = this.doSomeWorkWithArgsAndReturnAsync(2, 7);
        future.join();
        final Integer result = future.get();
        LOGGER.info("Future Result: {}", result);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(0);
    }

    @Test
    public void doSomeWorkWithoutArgsAndThrowAsync_Success() {
        CompletableFuture<Integer> future = this.doSomeWorkWithoutArgsAndThrowAsync();
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, future::join);
    }


    @Test
    public void tryAllTasksAtSameTime_Success() throws ExecutionException, InterruptedException {
        doSomeWorkWithoutArgsAsync();
        doSomeWorkWithArgsAsync(2, 9);

        CompletableFuture<Integer> future1 = this.doSomeWorkWithoutArgsAndReturnAsync();
        CompletableFuture<Integer> future2 = this.doSomeWorkWithArgsAndReturnAsync(6, 1);

        Arrays.asList(future1, future2).forEach(CompletableFuture::join);
        LOGGER.info("Future Result (doSomeWorkWithoutArgsAndReturnAsync): {}", future1.get());
        LOGGER.info("Future Result (doSomeWorkWithArgsAndReturnAsync): {}", future2.get());
    }


    private void doSomeWorkWithoutArgs() {
        LOGGER.info("doSomeWorkWithoutArgs() -> Hey there!");
    }
    private void doSomeWorkWithoutArgsAsync() {
        LOGGER.info("doSomeWorkWithoutArgsAsync() -> Starting.");
        asyncRunnerService.doAsync(this::doSomeWorkWithoutArgs);
        LOGGER.info("doSomeWorkWithoutArgsAsync() -> Ending.");
    }

    private void doSomeWorkWithArgs(int a, int b) {
        LOGGER.info("doSomeWorkWithArgs(): {}, {}", a, b);
    }
    private void doSomeWorkWithArgsAsync(final int a, final int b) {
        LOGGER.info("doSomeWorkWithArgsAsync() -> Starting: {}, {}", a, b);
        asyncRunnerService.doAsync(() -> this.doSomeWorkWithArgs(a, b));
        LOGGER.info("doSomeWorkWithArgsAsync() -> Ending.");
    }

    private Integer doSomeWorkWithoutArgsAndReturn() {
        LOGGER.info("doSomeWorkWithoutArgsAndReturn() -> Hey there!");
        return 0;
    }
    private CompletableFuture<Integer> doSomeWorkWithoutArgsAndReturnAsync() {
        LOGGER.info("doSomeWorkWithoutArgsAndReturnAsync() -> Starting.");
        final CompletableFuture<Integer> result = asyncRunnerService.doAsync(this::doSomeWorkWithoutArgsAndReturn);
        LOGGER.info("doSomeWorkWithoutArgsAndReturnAsync() -> Ending with Future: {}", result);
        return result;
    }

    private Integer doSomeWorkWithArgsAndReturn(int a, int b) {
        LOGGER.info("doSomeWorkWithArgsAndReturn(): {}, {}", a, b);
        return 0;
    }
    private CompletableFuture<Integer> doSomeWorkWithArgsAndReturnAsync(final int a, final int b) {
        LOGGER.info("doSomeWorkWithArgsAndReturnAsync() -> Starting: {}, {}", a, b);
        final CompletableFuture<Integer> result = asyncRunnerService.doAsync(() -> this.doSomeWorkWithArgsAndReturn(a, b));
        LOGGER.info("doSomeWorkWithArgsAndReturnAsync() -> Ending with Future: {}", result);
        return result;
    }

    private Integer doSomeWorkWithoutArgsAndThrow() {
        LOGGER.info("doSomeWorkWithoutArgsAndThrow() -> Hey there!");
        throw new RuntimeException("Cool Exception");
    }
    private CompletableFuture<Integer> doSomeWorkWithoutArgsAndThrowAsync() {
        LOGGER.info("doSomeWorkWithoutArgsAndThrowAsync() -> Starting.");
        final CompletableFuture<Integer> result = asyncRunnerService.doAsync(this::doSomeWorkWithoutArgsAndThrow);
        LOGGER.info("doSomeWorkWithoutArgsAndThrowAsync() -> Ending with Future: {}", result);
        return result;
    }
    
}
