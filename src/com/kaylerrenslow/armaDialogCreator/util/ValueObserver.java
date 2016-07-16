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
	private ArrayList<ValueListener<V>> listeners = new ArrayList<>();

	public ValueObserver(V value) {
		this.value = value;
	}

	/** Update the value and notify the value listener. Doesn't have to be a new value. */
	public void updateValue(@Nullable V newValue) {
		V oldValue = this.value;
		this.value = newValue;
		for (ValueListener<V> listener : listeners) {
			listener.valueUpdated(this, oldValue, this.value);
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

}
