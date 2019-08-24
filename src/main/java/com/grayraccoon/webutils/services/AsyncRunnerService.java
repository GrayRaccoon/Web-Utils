package com.grayraccoon.webutils.services;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * This interface should allow spring to create a Proxy to run the async tasks
 * without the need of exposing our async jobs as public methods on the actual impl.
 *
 * @author Heriberto Reyes Esparza <hery.chemo@gmail.com>
 */
public interface AsyncRunnerService {

    /**
     * Use this method to execute Async Methods that doesn't require to have a return type.
     *
     * Usage:
     * <pre>{@code
     *
     * @Autowired
     * private SimpleAsyncRunnerService simpleAsyncRunnerService;
     *
     * public void doSomethingAsync() {
     *     this.simpleAsyncRunnerService.doAsync(this::doSomething);
     * }
     *
     * public void doSomething() {
     *     // Some cool code
     * }
     *
     * public void doSomethingWithParamsAsync(
     *              final String coolParam1,
     *              final String coolParam2) {
     *     this.simpleAsyncRunnerService.doAsync(() -> this.doSomethingWithParams(coolParam1, coolParam2));
     * }
     *
     * public void doSomethingWithParams(String coolParam1, String coolParam2) {
     *     // Some cool code
     * }
     *
     * }</pre>
     *
     * @param asyncJob Input runnable to execute inside async task,
     *                 it can also be a method reference or a lambda.
     */
    void doAsync(Runnable asyncJob);

    /**
     * Use this method to execute Async methods that require to have return type.
     * The return type will be wrapped into a {@link CompletableFuture}.
     *
     * Usage:
     * <pre>{@code
     *
     * @Autowired
     * private SimpleAsyncRunnerService simpleAsyncRunnerService;
     *
     * public CompletableFuture<Integer> doSomethingAsync() throws Exception {
     *     return this.simpleAsyncRunnerService.doAsync(this::doSomething);
     * }
     *
     * public Integer doSomething() {
     *     // Some cool code
     *     return 0;
     * }
     *
     * public CompletableFuture<Integer> doSomethingWithParamsAsync(
     *              final String coolParam1,
     *              final String coolParam2) {
     *     return this.simpleAsyncRunnerService.doAsync(() -> this.doSomethingWithParams(coolParam1, coolParam2));
     * }
     *
     * public Integer doSomethingWithParams(String coolParam1, String coolParam2) {
     *     // Some cool code
     *     return 0;
     * }
     *
     * }</pre>
     *
     * @param asyncJob Input supplier to execute inside async task,
     *                 it can also be a method reference or a lambda.
     * @param <T> Required return Type.
     * @return Supplier result wrapped into a {@link CompletableFuture}.
     */
    <T> CompletableFuture<T> doAsync(Supplier<T> asyncJob);

}
