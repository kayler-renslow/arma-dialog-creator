package com.kaylerrenslow.armaDialogCreator.data;

/**
 Created by Kayler on 08/02/2016.
 */
public interface Change {
	String getShortName();

	String getDescription();

	ChangeRegistrar getRegistrar();

	enum ChangeType {
		/** an undo change */
		UNDO,
		/** a redo change */
		REDO,
		/** a change that was just placed on the stack */
		CREATED
	}
}
