package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.control.ControlClassRequirementSpecification;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookupConstant;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public interface ArmaControlSpecRequirement extends ControlClassRequirementSpecification {
	ArmaControlSpecRequirement TRIVIAL = new ArmaControlSpecRequirement() {
	};

	/** Returns a new array of the properties that are required for all controls */
	ControlPropertyLookupConstant[] defaultRequiredProperties = {
			ControlPropertyLookup.TYPE,
			ControlPropertyLookup.IDC,
			ControlPropertyLookup.STYLE,
			ControlPropertyLookup.X,
			ControlPropertyLookup.Y,
			ControlPropertyLookup.W,
			ControlPropertyLookup.H};


	/** Returns a new array of properties that are optional for all controls */
	ControlPropertyLookupConstant[] defaultOptionalProperties = {ControlPropertyLookup.ACCESS};

	ReadOnlyList<ControlPropertyLookupConstant> defaultRequiredPropertiesReadOnly = new ReadOnlyList<>(defaultRequiredProperties);
	ReadOnlyList<ControlPropertyLookupConstant> defaultOptionalPropertiesReadOnly = new ReadOnlyList<>(defaultOptionalProperties);

	@NotNull
	@Override
	default ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
		return defaultRequiredPropertiesReadOnly;
	}

	@NotNull
	@Override
	default ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
		return defaultOptionalPropertiesReadOnly;
	}

	default ControlStyle[] getAllowedStyles() {
		return ControlStyle.EMPTY;
	}
}
