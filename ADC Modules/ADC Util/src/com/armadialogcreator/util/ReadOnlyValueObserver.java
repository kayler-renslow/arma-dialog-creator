package com.armadialogcreator.util;

import javafx.beans.InvalidationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A wrapper class for a {@link ValueObserver} instance that provides read-only functionality

 @author Kayler
 @since 09/16/2016. */
public class ReadOnlyValueObserver<V> extends ValueObserver<V> {
	private final ValueObserver<V> observer;

	public ReadOnlyValueObserver(@NotNull ValueObserver<V> observer) {
		this.observer = observer;
	}

	@Override
	@NotNull
	public ReadOnlyValueObserver<V> getReadOnlyValueObserver() {
		return this;
	}

	@Override
	public void updateValue(@Nullable V newValue) {

	}

	@Nullable
	@Override
	public V getValue() {
		return observer.getValue();
	}

	@Override
	public void addListener(@NotNull ValueListener<V> listener) {
		observer.addListener(listener);
	}

	@Override
	public void removeListener(@NotNull ValueListener<V> listener) {
		observer.removeListener(listener);
	}

	@Override
	@NotNull
	public ReadOnlyList<ValueListener<V>> getListeners() {
		return observer.getListeners();
	}

	@Override
	public void clearListeners() {
		observer.clearListeners();
	}

	@Override
	public void invalidate() {
		observer.invalidate();
	}

	@Override
	public void addListener(@NotNull InvalidationListener listener) {
		observer.addListener(listener);
	}

	@Override
	public void removeListener(@NotNull InvalidationListener listener) {
		observer.removeListener(listener);
	}
}
