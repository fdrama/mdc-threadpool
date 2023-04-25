package org.pallas.middleware.thread;

import org.slf4j.MDC;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MdcThreadPoolExecutor extends ThreadPoolExecutor {

	public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory());
	}

	public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new AbortPolicy());
	}

	public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), handler);
	}

	public MdcThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		if (task == null) {
			throw new NullPointerException();
		}
		return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		if (task == null) {
			throw new NullPointerException();
		}
		return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()), result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		if (task == null) {
			throw new NullPointerException();
		}
		return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		if (tasks == null) {
			throw new NullPointerException();
		}
		return super.invokeAll(MdcUtils.decorate(tasks));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		if (tasks == null) {
			throw new NullPointerException();
		}
		return super.invokeAll(MdcUtils.decorate(tasks), timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return super.invokeAny(MdcUtils.decorate(tasks));
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (tasks == null) {
			throw new NullPointerException();
		}
		if (tasks.size() == 0) {
			throw new IllegalArgumentException();
		}
		return super.invokeAny(MdcUtils.decorate(tasks), timeout, unit);
	}

	@Override
	public void execute(Runnable command) {
		if (command == null) {
			throw new NullPointerException();
		}
		super.execute(MdcUtils.wrap(command, MDC.getCopyOfContextMap()));
	}
}
