package com.armadialogcreator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 A central place for storing and shutting down all {@link ExecutorService} instances since
 Java doesn't currently allow shutting down all instances from somewhere else.

 @author Kayler
 @since 06/13/2017 */
public class ADCExecutors {
	private static final List<ExecutorService> executors = Collections.synchronizedList(new ArrayList<>());

	public static synchronized void registerExecutorService(@NotNull ExecutorService service) {
		//synchronized to prevent terminating and register happening at same time
		//because we want to terminate everything before accepting new executors

		executors.add(service);
	}

	/**
	 Terminate all registered executors. This should only be used when the application is exiting.
	 */
	public static synchronized void terminateAll() {
		//synchronized to prevent terminating and register happening at same time
		//because we want to terminate everything before accepting new executors

		for (ExecutorService executorService : executors) {
			executorService.shutdownNow();
		}
		executors.clear();
	}
}
