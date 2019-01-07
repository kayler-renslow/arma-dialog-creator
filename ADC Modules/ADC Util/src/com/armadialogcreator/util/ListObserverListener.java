package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/12/2016.
 */
public interface ListObserverListener<E> extends ObserverListener {
	/**
	 A change was invoked on the given {@link ListObserver}. Only one operation is performed at once. Therefore, only one of the following will be true:<br>
	 <ul>
	 <li>{@link ListObserverChange#wasSet()}</li>
	 <li>{@link ListObserverChange#wasAdded()}</li>
	 <li>{@link ListObserverChange#wasRemoved()}</li>
	 <li>{@link ListObserverChange#wasMoved()}</li>
	 </ul>

	 @param list list the change happened to
	 @param change the change that occurred
	 */
	void onChanged(@NotNull ListObserver<E> list, @NotNull ListObserverChange<E> change);
}
