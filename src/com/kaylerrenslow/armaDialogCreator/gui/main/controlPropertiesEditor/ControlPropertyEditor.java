package com.kaylerrenslow.armaDialogCreator.gui.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/08/2016.
 */
public interface ControlPropertyEditor {
	/** @return the {@link ControlProperty} being edited. */
	@NotNull
	ControlProperty getControlProperty();

	/** @return true if all data entered is valid, false if the data is not valid */
	boolean hasValidData();

	/** Set whether or not the property can be edited by the user. */
	void disableEditing(boolean disable);

}
