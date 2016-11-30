package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 11/14/2016.
 */
public interface ControlPropertyUpdate {

	/** Get the ControlProperty that received an update */
	@NotNull
	ControlProperty getControlProperty();

}
