package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/11/2019 */
public interface ConfigClassSpecification {
	@NotNull ConfigPropertyCategory getPropertyCategory(@NotNull ConfigProperty property);

	@NotNull ReadOnlyIterable<ConfigProperty> iterateProperties();

	boolean propertyIsInherited(@NotNull ConfigProperty property);

	@Nullable String getExtendClassName();

	@Nullable ReadOnlyIterable<ConfigPropertyLookup> iterateLookupProperties();
}
