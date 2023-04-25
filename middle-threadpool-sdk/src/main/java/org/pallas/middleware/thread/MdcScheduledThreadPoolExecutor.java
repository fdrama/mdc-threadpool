package org.pallas.middleware.thread;

import org.slf4j.MDC;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MdcScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	public MdcScheduledThreadPoolExecutor(int corePoolSize) {
		this(corePoolSize, Executors.defaultThreadFactory());

	}

	public MdcScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
		this(corePoolSize, threadFactory, new AbortPolicy());
	}

	public MdcScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		super(corePoolSize, threadFactory, handler);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		if (command == null || unit == null) {
			throw new NullPointerException();
		}
		return super.schedule(MdcUtils.wrap(command, MDC.getCopyOfContextMap()), delay, unit);
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		if (callable == null || unit == null) {
			throw new NullPointerException();
		}
		return super.schedule(MdcUtils.wrap(callable, MDC.getCopyOfContextMap()), delay, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		if (command == null || unit == null) {
			throw new NullPointerException();
		}
		if (period <= 0) {
			throw new IllegalArgumentException();
		}
		return super.scheduleAtFixedRate(MdcUtils.wrap(command, MDC.getCopyOfContextMap()), initialDelay, period, unit);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		if (command == null || unit == null) {
			throw new NullPointerException();
		}
		if (delay <= 0) {
			throw new IllegalArgumentException();
		}
		return super.scheduleWithFixedDelay(MdcUtils.wrap(command, MDC.getCopyOfContextMap()), initialDelay, delay,
				unit);
	}

	@Override
	public void execute(Runnable command) {
		schedule(command, 0, NANOSECONDS);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return schedule(task, 0, NANOSECONDS);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return schedule(Executors.callable(task, result), 0, NANOSECONDS);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return schedule(task, 0, NANOSECONDS);
	}
}
