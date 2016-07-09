package com.kaylerrenslow.armaDialogCreator.gui.fx.main.controlPropertiesEditor;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlClassSpecificationProvider;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;

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

	/** Set whether or not the property can be edited by the user. */
	void disableEditing(boolean disable);

	/**Return true if the property is optional (not in required list returned from {@link ControlClassSpecificationProvider#getRequiredProperties()})*/
	boolean isOptional();

	/** Return the update groud for the control property. Any time the controlProperty is updated, this listener group will be notified */
	UpdateListenerGroup<ControlProperty> getControlPropertyUpdateGroup();
}
