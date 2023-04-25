package org.pallas.middleware.thread;

import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

public class MdcUtils {

	public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
		return () -> {
			Map<String, String> previous = MDC.getCopyOfContextMap();
			if (context == null || context.isEmpty()) {
				MDC.clear();
			} else {
				MDC.setContextMap(context);
			}
			try {
				return callable.call();
			} finally {
				if (previous == null) {

					MDC.clear();
				} else {
					MDC.setContextMap(previous);
				}
			}
		};
	}

	public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
		return () -> {
			Map<String, String> previous = MDC.getCopyOfContextMap();
			if (context == null || context.isEmpty()) {
				MDC.clear();
			} else {
				MDC.setContextMap(context);
			}
			try {
				runnable.run();
			} finally {
				if (previous == null) {
					MDC.clear();
				} else {
					MDC.setContextMap(previous);
				}
			}
		};
	}

	public static <T> Collection<? extends Callable<T>> decorate(Collection<? extends Callable<T>> tasks) {
		if (tasks == null) {
			throw new NullPointerException();
		}
		Collection<Callable<T>> newTasks = new ArrayList<>(tasks.size());
		for (Callable<T> task : tasks) {
			newTasks.add(wrap(task, MDC.getCopyOfContextMap()));
		}
		return newTasks;
	}
}
