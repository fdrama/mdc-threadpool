package org.pallas.middleware.thread;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fdrama date 2023年04月25日 16:08
 */
public class ThreadPoolExecutorTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorTest.class);

    @Test
    public void testThreadPoolExecutor() {
        MDC.put("traceId", UUID.randomUUID().toString());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            logger.info("async task executed!");
        });
    }


    @Test
    public void testMdcUtils() {
        MDC.put("traceId", UUID.randomUUID().toString());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(MdcUtils.wrap(() -> {
            logger.info("async task executed!");
        }, MDC.getCopyOfContextMap()));

    }


    @Test
    public void testMdcThreadPoolExecutor() {
        MDC.put("traceId", UUID.randomUUID().toString());
        ThreadPoolExecutor executor = new MdcThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        executor.execute(() -> {
            logger.info("async task executed!");
        });

    }

    @Test
    public void testMdcScheduledThreadPoolExecutor() throws InterruptedException {
        MDC.put("traceId", UUID.randomUUID().toString());
        ScheduledThreadPoolExecutor executor = new MdcScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(() -> {
            logger.info("scheduled task executed!");
        }, 1, 1, TimeUnit.SECONDS);
        Thread.sleep(10000);
    }
}
