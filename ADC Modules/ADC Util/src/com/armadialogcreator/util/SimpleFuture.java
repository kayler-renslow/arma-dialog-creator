package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

/**
 @author K
 @since 5/27/19 */
public class SimpleFuture<V> implements Future<V> {

	private volatile V value;
	private volatile boolean cancelled = false;
	private final CountDownLatch latch = new CountDownLatch(1);

	public void setValue(@NotNull V value) {
		this.value = value;
		latch.countDown();
	}

	@Override
	public boolean cancel(boolean b) {
		cancelled = true;
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean isDone() {
		return latch.getCount() <= 0;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		latch.await();
		return value;
	}

	@Override
	public V get(long l, @NotNull TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
		latch.await(l, timeUnit);
		return value;
	}
}
