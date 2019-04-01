package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyIterable;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/11/2019 */
public interface ConfigClassSpecification {
	@NotNull ConfigPropertyCategory getPropertyCategory(@NotNull ConfigProperty property);

	@NotNull ReadOnlyIterable<ConfigProperty> iterateProperties();

	boolean propertyIsInherited(@NotNull String propertyName);

	@Nullable String getExtendClassName();

	@Nullable ReadOnlyIterable<ConfigPropertyLookupConstant> iterateLookupProperties();

	@NotNull
	UpdateListenerGroup<ConfigClassUpdate> getClassUpdateGroup();
}
