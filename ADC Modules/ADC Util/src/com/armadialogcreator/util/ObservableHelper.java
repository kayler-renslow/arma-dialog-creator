package com.armadialogcreator.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 @author K
 @since 4/2/19 */
public abstract class ObservableHelper implements Observable {
	protected final List<InvalidationListener> invalidationListeners = new LinkedList<>();

	@Override
	public void addListener(@NotNull InvalidationListener listener) {
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(@NotNull InvalidationListener listener) {
		invalidationListeners.remove(listener);
	}
}
