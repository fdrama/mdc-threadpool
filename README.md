# MDC上下文传递

MDC上下文传递是指在多个线程之间传递MDC内容，以便在整个请求处理过程中记录日志。

## 支持异步任务包装

```java
    Runnable wrapRunnable = MdcUtils.wrap(() -> {
                // do something
    }, MDC.getCopyOfContextMap());
```

## 提供自定义线程池

```java
    ThreadPoolExecutor executor = new MdcThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
    executor.execute(() -> {
        logger.info("async task executed!");
    });

    ScheduledThreadPoolExecutor executor = new MdcScheduledThreadPoolExecutor(1);
    executor.scheduleAtFixedRate(() -> {
        logger.info("scheduled task executed!");
    }, 1, 1, TimeUnit.SECONDS);
```