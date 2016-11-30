package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A {@link DisplayProperty} is similar to a {@link ControlProperty}. However, {@link DisplayProperty} is used to separate {@link ControlPropertyLookupConstant}
 instances such that {@link DisplayProperty}'s lookups are not from {@link ControlPropertyLookup}
 @author Kayler
 @since 09/15/2016.
 */
public class DisplayProperty extends ControlProperty{
	public DisplayProperty(DisplayPropertyLookup propertyLookup, @Nullable SerializableValue value) {
		super(propertyLookup, value);
	}

	public DisplayProperty(DisplayPropertyLookup propertyLookup) {
		super(propertyLookup);
	}

	@NotNull
	@Override
	public DisplayPropertyLookup getPropertyLookup() {
		return (DisplayPropertyLookup) super.getPropertyLookup();
	}
}
