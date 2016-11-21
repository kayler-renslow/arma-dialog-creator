/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
