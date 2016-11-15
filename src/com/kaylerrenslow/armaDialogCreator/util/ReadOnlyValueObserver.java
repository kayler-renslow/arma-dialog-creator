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
		observer.addValueListener(new ValueListener<V>() {
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
