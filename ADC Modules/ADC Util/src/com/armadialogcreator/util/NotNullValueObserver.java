package com.armadialogcreator.util;

import org.jetbrains.annotations.NotNull;

/**
 Simple value observer implementation

 @author Kayler
 @since 05/31/2016. */
public class NotNullValueObserver<V> extends ValueObserver<V> {

	public NotNullValueObserver(@NotNull V value) {
		super(value);
	}

	@Override
	public void updateValue(@NotNull V newValue) {
		super.updateValue(newValue);
	}

	@NotNull
	@Override
	public V getValue() {
		return super.getValue();
	}
}
