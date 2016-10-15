/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 A {@link ControlPropertyLookupConstant} is a way to check if one {@link ControlProperty} equals another. Each instance that implements this interface must be instantiated only <b>once</b>.
 Created on 09/15/2016. */
public interface ControlPropertyLookupConstant {
	/** All values that the property can be, or null if user defined. */
	@Nullable
	ControlPropertyOption[] getOptions();

	/** Get the name associated with the property. Is not guaranteed to be unique. */
	String getPropertyName();

	/** Get the {@link PropertyType} associated with the property. */
	@NotNull
	PropertyType getPropertyType();

	/**
	 A unique id for the lookup item to guarantee a match by despite order change or property name change, or some other change.
	 <br>When the lookup item is written, the propertyId must <b>NEVER</b> change.
	 */
	int getPropertyId();

	/** Get documentation on the property. Each index corresponds to a newline (\n) separator being inserted in the end result. */
	String[] getAbout();

	default String getAboutText() {
		String r = "";
		if(getAbout() == null){
			return Lang.ApplicationBundle.getString("Misc.no_documentation");
		}
		for (String s : getAbout()) {
			r += s + "\n";
		}
		return r.substring(0, r.length() - 1);//remove last newline
	}
}
