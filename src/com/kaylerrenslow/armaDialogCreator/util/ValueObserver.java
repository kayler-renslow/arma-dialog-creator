package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 Simple value observer implementation
 Created on 05/31/2016. */
public class ValueObserver<V> {
	private V value;
	private V oldValue;
	private boolean notify = false;
	private ArrayList<ValueListener<V>> listeners = new ArrayList<>();

	public ValueObserver(V value) {
		this.value = value;
	}

	/** Update the value and notify the value listener. Doesn't have to be a new value. */
	public void updateValue(@Nullable V newValue) {
		updateValueSilent(newValue);
		notifyValueListeners();
	}

	/**
	 Notifies all value listeners if the value changed.
	 This is used after {@link #updateValue(Object)} is invoked. This is also useful after invoking {@link #updateValueSilent(Object)}
	 The old value and new value is cached in the observer. Therefore, calling this method more than once consecutively will pass the same old value and new value.
	 */
	public void notifyValueListeners() {
		if (notify) {
			for (ValueListener<V> listener : listeners) {
				listener.valueUpdated(this, this.oldValue, this.value);
			}
			notify = false;
		}
	}

	/** Set the listener that listens to the state of the value */
	public void addValueListener(@NotNull ValueListener<V> listener) {
		this.listeners.add(listener);
	}

	/** Remove the listener from the list. Returns true if the listener was inside the list */
	public boolean removeListener(ValueListener<V> listener) {
		return listeners.remove(listener);
	}

	public V getValue() {
		return value;
	}

	/** Sets the value without telling value listener */
	public void updateValueSilent(V newValue) {
		this.oldValue = this.value;
		notify = true;
		this.value = newValue;
	}
}
