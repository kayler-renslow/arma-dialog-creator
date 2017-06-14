package com.kaylerrenslow.armaDialogCreator.main;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 A central place for storing and shutting down all {@link ExecutorService} instances since
 Java doesn't currently allow shutting down all instances from somewhere else.

 @author Kayler
 @since 06/13/2017 */
public class ADCExecutors {
	private static LinkedBlockingQueue<ExecutorService> executors = new LinkedBlockingQueue<>();

	public static void registerExecutorService(@NotNull ExecutorService service) {
		executors.add(service);
	}

	public static synchronized void terminateAll() {
		for (ExecutorService executorService : executors) {
			executorService.shutdownNow();
		}
		executors.clear();
	}
}
