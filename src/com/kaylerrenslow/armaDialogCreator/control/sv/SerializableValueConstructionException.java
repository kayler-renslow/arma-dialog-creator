package com.kaylerrenslow.armaDialogCreator.control.sv;

import org.jetbrains.annotations.NonNls;

/**
 @author Kayler
 @since 01/29/2017 */
public class SerializableValueConstructionException extends RuntimeException {
	public SerializableValueConstructionException() {
	}

	public SerializableValueConstructionException(@NonNls String message) {
		super(message);
	}
}
