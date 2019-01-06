package com.armadialogcreator.core.sv;

import org.jetbrains.annotations.NonNls;

/**
 @author Kayler
 @since 01/29/2017 */
public class SerializableValueConstructionException extends RuntimeException {
	public SerializableValueConstructionException() {
	}

	public SerializableValueConstructionException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializableValueConstructionException(@NonNls String message) {
		super(message);
	}
}
