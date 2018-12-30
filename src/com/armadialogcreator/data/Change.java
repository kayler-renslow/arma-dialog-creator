package com.armadialogcreator.data;

import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/02/2016.
 */
public interface Change {
	@NotNull String getShortName();

	@NotNull String getDescription();

	@NotNull ChangeRegistrar getRegistrar();

	enum ChangeType {
		/** an undo change */
		UNDO,
		/** a redo change */
		REDO,
		/** a change that was just placed on the stack */
		CREATED
	}
}
