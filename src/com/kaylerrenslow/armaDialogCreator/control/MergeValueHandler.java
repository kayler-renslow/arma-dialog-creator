package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;

/**
 An interface used as a callback for converting a {@link SerializableValue} into
 a different {@link SerializableValue} because the conversion via {@link SerializableValue#convert(DataContext, SerializableValue, PropertyType)} couldn't be finished.

 @author Kayler
 @see ControlClass#inheritProperty(ControlPropertyLookupConstant, MergeValueHandler)
 @since 06/04/2017 */
public interface MergeValueHandler {

	/**
	 Turn a {@link SerializableValue} into a new instance that has a different {@link PropertyType}

	 @param currentValue the current value
	 @param newPropertyType the new type to use
	 @return the new value
	 */
	@NotNull SerializableValue merge(@NotNull SerializableValue currentValue, @NotNull PropertyType newPropertyType);
}
