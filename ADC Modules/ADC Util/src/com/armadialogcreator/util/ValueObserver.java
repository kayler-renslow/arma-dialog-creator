package com.armadialogcreator.util;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 Simple value observer implementation

 @author Kayler
 @since 05/31/2016. */
public class ValueObserver<V> implements Observable, Observer<ValueListener<V>> {
	private V value;
	private final List<ValueListener<V>> valueListeners = new LinkedList<>();
	private final ReadOnlyList<ValueListener<V>> valueListenersRO = new ReadOnlyList<>(valueListeners);
	private ReadOnlyValueObserver<V> readOnlyValueObserver;

	private final List<InvalidationListener> invalidationListeners = new LinkedList<>();

	public ValueObserver() {
		this(null);
	}

	public ValueObserver(@Nullable V value) {
		this.value = value;
	}

	@NotNull
	public ReadOnlyValueObserver<V> getReadOnlyValueObserver() {
		if (readOnlyValueObserver == null) {
			readOnlyValueObserver = new ReadOnlyValueObserver<>(this); //only initialize it when needed
		}
		return readOnlyValueObserver;
	}

	/**
	 Update the value and notify the value listener. The listeners will only be notified if the value is not equal (via {@link #equals(Object)}).

	 @param newValue new value to set to
	 */
	public void updateValue(@Nullable V newValue) {
		if ((newValue == null && this.value == null) || (newValue != null && newValue.equals(this.value))) {
			return;
		}
		V oldValue = this.value;
		this.value = newValue;

		for (ValueListener<V> listener : valueListeners) {
			listener.valueUpdated(this, oldValue, this.value);
		}

		for (InvalidationListener listener : invalidationListeners) {
			listener.invalidated(this);
		}
	}

	/** Set the listener that listens to the state of the value. The listener will only be added once. If it exists in the listeners list, nothing will happen. */
	public void addListener(@NotNull ValueListener<V> listener) {
		if (valueListeners.contains(listener)) {
			return;
		}
		this.valueListeners.add(listener);
	}

	/** Remove the listener from the list */
	public void removeListener(@NotNull ValueListener<V> listener) {
		listener.listenerDetached();
		valueListeners.remove(listener);
	}

	@NotNull
	public ReadOnlyList<ValueListener<V>> getListeners() {
		return valueListenersRO;
	}

	@Override
	public void clearListeners() {
		for (ValueListener l : valueListeners) {
			l.listenerDetached();
		}
		valueListeners.clear();
		invalidationListeners.clear();
	}

	@Override
	public void addListener(@NotNull InvalidationListener listener) {
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(@NotNull InvalidationListener listener) {
		invalidationListeners.remove(listener);
	}

	@Nullable
	public V getValue() {
		return value;
	}

}
