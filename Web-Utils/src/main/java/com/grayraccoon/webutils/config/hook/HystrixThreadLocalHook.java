package com.grayraccoon.webutils.config.hook;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * This hook supports passing ThreadId, LocaleCtx and RequestCtx.
 *
 * @author Heriberto Reyes Esparza
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HystrixThreadLocalHook extends HystrixCommandExecutionHook {

    @Getter
    private static final HystrixThreadLocalHook instance = new HystrixThreadLocalHook();

    private final HystrixRequestVariableDefault<Integer> hrvThreadId
            = new HystrixRequestVariableDefault<>();
    private final HystrixRequestVariableDefault<LocaleContext> hrvLocaleCtx
            = new HystrixRequestVariableDefault<>();
    private final HystrixRequestVariableDefault<RequestAttributes> hrvRequestCtx
            = new HystrixRequestVariableDefault<>();

    @Override
    public <T> void onStart(final HystrixInvokable<T> commandInstance) {
        HystrixRequestContext.initializeContext();
        getThreadLocals();
    }

    @Override
    public <T> void onExecutionStart(final HystrixInvokable<T> commandInstance) {
        setThreadLocals();
    }


    @Override
    public <T> void onFallbackStart(final HystrixInvokable<T> commandInstance) {
        setThreadLocals();
    }


    @Override
    public <T> void onSuccess(final HystrixInvokable<T> commandInstance) {
        HystrixRequestContext.getContextForCurrentThread().shutdown();
        super.onSuccess(commandInstance);
    }

    @Override
    public <T> Exception onError(
            final HystrixInvokable<T> commandInstance,
            final HystrixRuntimeException.FailureType failureType,
            final Exception e) {
        HystrixRequestContext.getContextForCurrentThread().shutdown();
        return super.onError(commandInstance, failureType, e);
    }

    private void getThreadLocals() {
        hrvThreadId.set(ThreadLocalUtil.getId());
        hrvLocaleCtx.set(LocaleContextHolder.getLocaleContext());
        hrvRequestCtx.set(RequestContextHolder.getRequestAttributes());
    }

    private void setThreadLocals() {
        ThreadLocalUtil.setId(hrvThreadId.get());
        LocaleContextHolder.setLocaleContext(hrvLocaleCtx.get());
        RequestContextHolder.setRequestAttributes(hrvRequestCtx.get());
    }

    /**
     * Thread local utility to manage thread id.
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ThreadLocalUtil {
        private static final ThreadLocal<Integer> idTL = new ThreadLocal<>();

        /**
         * Sets current thread id.
         *
         * @param id Thread id.
         */
        public static void setId(final Integer id) {
            idTL.set(id);
        }

        /**
         * Gets the current thread id.
         */
        public static Integer getId() {
            return idTL.get();
        }
    }
}
