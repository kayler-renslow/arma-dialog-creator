/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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

	/** Update the value and notify the value listener. The listeners will only be notified if the value is not equal (via {@link #equals(Object)}).  */
	public void updateValue(@Nullable V newValue) {
		if((newValue == null && this.value == null) || (newValue != null && newValue.equals(this.value))){
			return;
		}
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
