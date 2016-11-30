package com.kaylerrenslow.armaDialogCreator.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 Simple value observer implementation

 @author Kayler
 @since 05/31/2016. */
public class ValueObserver<V> {
	private V value;
	private final LinkedList<ValueListener<V>> listeners = new LinkedList<>();
	private ReadOnlyValueObserver<V> readOnlyValueObserver;

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
		for (ValueListener<V> listener : listeners) {
			listener.valueUpdated(this, oldValue, this.value);
		}
	}

	/** Set the listener that listens to the state of the value */
	public void addListener(@NotNull ValueListener<V> listener) {
		if (listeners.contains(listener)) {
			return;
		}
		this.listeners.add(listener);
	}

	/** Remove the listener from the list. Returns true if the listener was inside the list */
	public boolean removeListener(@NotNull ValueListener<V> listener) {
		return listeners.remove(listener);
	}

	@Nullable
	public V getValue() {
		return value;
	}

}
