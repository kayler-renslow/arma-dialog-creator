package com.armadialogcreator.util;

import org.jetbrains.annotations.Nullable;

/**
 A simple class for making references

 @author Kayler
 @since 05/01/2017 */
public class Reference<E> {
	private E value;

	public Reference(@Nullable E value) {
		this.value = value;
	}

	public Reference() {
	}

	@Nullable
	public E getValue() {
		return value;
	}

	public void setValue(@Nullable E value) {
		this.value = value;
	}
}
