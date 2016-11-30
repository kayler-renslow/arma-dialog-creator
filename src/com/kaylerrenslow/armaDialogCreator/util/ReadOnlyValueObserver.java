package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 A wrapper class for a {@link ValueObserver} instance that provides read-only functionality

 @author Kayler
 @since 09/16/2016. */
public class ReadOnlyValueObserver<V> {
	private final ValueObserver<V> observer;
	private final LinkedList<ReadOnlyValueListener<V>> listeners = new LinkedList<>();

	public ReadOnlyValueObserver(@NotNull ValueObserver<V> observer) {
		this.observer = observer;
		observer.addListener(new ValueListener<V>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<V> observer, V oldValue, V newValue) {
				for (ReadOnlyValueListener<V> listener : listeners) {
					listener.valueUpdated(ReadOnlyValueObserver.this, oldValue, newValue);
				}
			}
		});
	}

	/** Set the listener that listens to the state of the value */
	public void addValueListener(@NotNull ReadOnlyValueListener<V> listener) {
		if (listeners.contains(listener)) {
			return;
		}
		this.listeners.add(listener);
	}

	/** Remove the listener from the list. Returns true if the listener was inside the list */
	public boolean removeListener(@NotNull ReadOnlyValueListener<V> listener) {
		return listeners.remove(listener);
	}

	public V getValue() {
		return observer.getValue();
	}
}
