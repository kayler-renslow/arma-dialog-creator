package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassSpecificationProvider;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public class ArmaControlSpecProvider implements ControlClassSpecificationProvider {
	/** Returns a new array of the properties that are required for all controls */
	public final static ControlPropertyLookup[] DEFAULT_REQUIRED_PROPERTIES = {
			ControlPropertyLookup.TYPE,
			ControlPropertyLookup.IDC,
			ControlPropertyLookup.STYLE,
			ControlPropertyLookup.X,
			ControlPropertyLookup.Y,
			ControlPropertyLookup.W,
			ControlPropertyLookup.H
	};


	/** Returns a new array of properties that are optional for all controls */
	public static final ControlPropertyLookup[] DEFAULT_OPTIONAL_PROPERTIES = {
			ControlPropertyLookup.ACCESS
	};

	@NotNull
	@Override
	public ControlClass[] getRequiredSubClasses() {
		return ControlClass.EMPTY;
	}

	@NotNull
	@Override
	public ControlClass[] getOptionalSubClasses() {
		return ControlClass.EMPTY;
	}

	@NotNull
	@Override
	public ControlPropertyLookup[] getRequiredProperties() {
		return DEFAULT_REQUIRED_PROPERTIES;
	}

	@NotNull
	@Override
	public ControlPropertyLookup[] getOptionalProperties() {
		return DEFAULT_OPTIONAL_PROPERTIES;
	}
}
