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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 11/14/2016.
 */
public class ControlPropertyCustomDataUpdate implements ControlPropertyUpdate {


	private final ControlProperty property;
	private final Object oldCustomData;
	private final Object newCustomData;
	private final boolean setValueUpdate;
	private final boolean isUsingCustomData;

	public ControlPropertyCustomDataUpdate(@NotNull ControlProperty property, @Nullable Object oldCustomData, @Nullable Object newCustomData, boolean setValueUpdate, boolean isUsingCustomData) {
		this.property = property;
		this.oldCustomData = oldCustomData;
		this.newCustomData = newCustomData;
		this.setValueUpdate = setValueUpdate;
		this.isUsingCustomData = isUsingCustomData;
	}

	@Override
	public @NotNull ControlProperty getControlProperty() {
		return property;
	}

	@Nullable
	public Object getNewCustomData() {
		return newCustomData;
	}

	@Nullable
	public Object getOldCustomData() {
		return oldCustomData;
	}

	/**
	 Return true if the {@link #getControlProperty()} is using custom data, false otherwise (same as doing {@link ControlProperty#isUsingCustomData()})
	 */
	public boolean isUsingCustomData() {
		return isUsingCustomData;
	}

	/** Return true if the update was invoked by {@link ControlProperty#setCustomDataValue(Object)}, false if was invoked by {@link ControlProperty#setUsingCustomData(boolean)} */
	public boolean isSetValueUpdate() {
		return setValueUpdate;
	}
}
