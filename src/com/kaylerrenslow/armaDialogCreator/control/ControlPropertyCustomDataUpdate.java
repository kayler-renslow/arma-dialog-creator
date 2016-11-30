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
