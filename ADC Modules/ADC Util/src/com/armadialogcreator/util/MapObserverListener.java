package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/12/2016.
 */
public interface MapObserverListener<K, V> extends ObserverListener {
	/**
	 A change was invoked on the given {@link MapObserver}. Only one operation is performed at once. Therefore, only one of the following will be true:<br>
	 <ul>
	 <li>{@link MapObserverChange#wasReplaced()}</li>
	 <li>{@link MapObserverChange#wasPut()}</li>
	 <li>{@link MapObserverChange#wasRemoved()}</li>
	 <li>{@link MapObserverChange#wasMoved()}</li>
	 <li>{@link MapObserverChange#wasCleared()}</li>
	 </ul>

	 @param list list the change happened to
	 @param change the change that occurred
	 */
	void onChanged(@NotNull MapObserver<K, V> list, @NotNull MapObserverChange<K, V> change);
}
