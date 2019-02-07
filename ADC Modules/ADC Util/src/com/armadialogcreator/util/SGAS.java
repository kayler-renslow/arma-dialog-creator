package com.armadialogcreator.util;

import org.jetbrains.annotations.Nullable;

/**
 A Simple Get And Set (SGAS) class

 @author Kayler
 @since 05/01/2017 */
public class SGAS<E> {
	private E value;

	public SGAS(@Nullable E value) {
		this.value = value;
	}

	public SGAS() {
	}

	@Nullable
	public E getValue() {
		return value;
	}

	public void setValue(@Nullable E value) {
		this.value = value;
	}
}
