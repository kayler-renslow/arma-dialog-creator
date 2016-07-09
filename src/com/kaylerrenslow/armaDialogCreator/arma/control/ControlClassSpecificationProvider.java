package com.kaylerrenslow.armaDialogCreator.arma.control;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Provides the specification for a control class that says what sub-classes are required/optional and what properties are required/optional.
 Since the specification shouldn't change, it is best to store the returned values for later use
 Created on 07/07/2016. */
public interface ControlClassSpecificationProvider {

	/** Get all required sub-classes for the control class. Default implementation returns {@link ControlClass#EMPTY} */
	@NotNull
	default ControlClass[] getRequiredSubClasses() {
		return ControlClass.EMPTY;
	}

	/** Get all <b>optional</b> sub-classes for the control class. Default implementation returns {@link ControlClass#EMPTY} */
	@NotNull
	default ControlClass[] getOptionalSubClasses() {
		return ControlClass.EMPTY;
	}

	/** Get all required properties for the control class. Default implementation returns {@link ControlProperty#EMPTY} */
	@NotNull
	default ControlPropertyLookup[] getRequiredProperties() {
		return ControlPropertyLookup.EMPTY;
	}

	/** Get all <b>optional</b> properties for the control class. Default implementation returns {@link ControlProperty#EMPTY} */
	@NotNull
	default ControlPropertyLookup[] getOptionalProperties() {
		return ControlPropertyLookup.EMPTY;
	}

	/** Get all <b>optional event</b> properties for the control class. Default implementation returns {@link ControlProperty#EMPTY} */
	@NotNull
	default ControlPropertyLookup[] getEventProperties(){
		return ControlPropertyLookup.EMPTY;
	}
}
