package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;

/**
 Created by Kayler on 07/08/2016.
 */
public interface ControlPropertyEditor {
	/** Get the ControlProperty being edited. */
	ControlProperty getControlProperty();

	/** Returns true if all data entered is valid, false if the data is not valid */
	boolean hasValidData();

	/** Set the data to the default values */
	void resetToDefault();

	/** Set whether or not the property can be edited. */
	void disableEditing(boolean disable);
}
