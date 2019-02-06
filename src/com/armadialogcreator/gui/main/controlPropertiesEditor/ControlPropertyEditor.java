package com.armadialogcreator.gui.main.controlPropertiesEditor;

import com.armadialogcreator.core.old.ControlProperty;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/08/2016.
 */
public interface ControlPropertyEditor {
	/** @return the {@link ControlProperty} being edited. */
	@NotNull
	ControlProperty getControlProperty();

	/** Set whether or not the property can be edited by the user. */
	void disableEditing(boolean disable);

}
