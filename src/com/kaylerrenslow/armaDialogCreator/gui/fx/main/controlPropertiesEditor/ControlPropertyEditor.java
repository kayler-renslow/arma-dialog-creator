package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/08/2016.
 */
public interface ControlPropertyEditor {
	/** Get the ControlProperty being edited. */
	@NotNull
	ControlProperty getControlProperty();

	/** Returns true if all data entered is valid, false if the data is not valid */
	boolean hasValidData();

	/** Set whether or not the property can be edited by the user. */
	void disableEditing(boolean disable);

}
