package com.armadialogcreator.util;

import javafx.beans.InvalidationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A wrapper class for a {@link NotNullValueObserver} instance that provides read-only functionality

 @author Kayler
 @since 1/7/2019. */
public class ReadOnlyNotNullValueObserver<V> extends NotNullValueObserver<V> {
	private final NotNullValueObserver<V> observer;

	public ReadOnlyNotNullValueObserver(@NotNull NotNullValueObserver<V> observer) {
		super(observer.getValue());
		this.observer = observer;
	}

	@Override
	@NotNull
	public ReadOnlyNotNullValueObserver<V> getReadOnlyValueObserver() {
		return this;
	}

	@Override
	public void updateValue(@Nullable V newValue) {

	}

	@NotNull
	@Override
	public V getValue() {
		return observer.getValue();
	}

	@Override
	public void addListener(@NotNull NotNullValueListener<V> listener) {
		observer.addListener(listener);
	}

	@Override
	public void removeListener(@NotNull NotNullValueListener<V> listener) {
		observer.removeListener(listener);
	}

	@Override
	@NotNull
	public ReadOnlyList<NotNullValueListener<V>> getListeners() {
		return observer.getListeners();
	}

	@Override
	public void clearListeners() {
		observer.clearListeners();
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
