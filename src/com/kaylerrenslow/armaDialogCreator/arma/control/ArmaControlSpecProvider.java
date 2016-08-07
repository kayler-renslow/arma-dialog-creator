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

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.control.ControlClassSpecificationProvider;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
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
	
	public ControlStyle[] getAllowedStyles(){
		return ControlStyle.EMPTY;
	}
}
