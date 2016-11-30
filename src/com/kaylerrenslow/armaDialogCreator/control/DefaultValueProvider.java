package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 @author Kayler
 @since 11/22/2016 */
public interface DefaultValueProvider {
	/** Get a default value for the given property lookup */
	@Nullable SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup);

	/**
	 Tells the provider that the given properties will be needed for a series of {@link #getDefaultValue(ControlPropertyLookupConstant)} calls. This will not be called before
	 {@link #getDefaultValue(ControlPropertyLookupConstant)}
	 */
	void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch);
}
