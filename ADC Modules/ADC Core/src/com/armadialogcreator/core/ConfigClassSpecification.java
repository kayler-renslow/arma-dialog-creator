package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyIterable;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/11/2019 */
public interface ConfigClassSpecification {
	/**
	 Get the category of a {@link ConfigPropertyKey}

	 @param property the prop
	 @return the category
	 */
	@NotNull
	ConfigPropertyCategory getPropertyCategory(@NotNull ConfigPropertyKey property);

	/**
	 Iterate all {@link ConfigProperty} instances owned by this specification

	 @return an iterable
	 */
	@NotNull ReadOnlyIterable<ConfigProperty> iterateProperties();

	/** @return true if the property is inherited, false otherwise */
	boolean propertyIsInherited(@NotNull String propertyName);

	/** @return the name of the class that this specification extends, or null if not extending */
	@Nullable String getExtendClassName();

	/**
	 Get an iterable for iterating all {@link ConfigProperty} that could exist from this specification.

	 @return an iterable
	 */
	@Nullable ReadOnlyIterable<ConfigPropertyLookupConstant> iterateLookupProperties();

	/** @return an {@link UpdateListenerGroup} that listens to all changes of this specification */
	@NotNull
	UpdateListenerGroup<ConfigClassUpdate> getClassUpdateGroup();

	/**
	 Get the name of the bundle where the documentation for each {@link ConfigProperty}
	 owned by this {@link ConfigClassSpecification}. If unspecified, return null.

	 @return the name, or null
	 */
	@Nullable
	default String getConfigPropertyDocumentationBundle() {
		return null;
	}

	/**
	 Get the {@link ConfigClassSpecification} that owns this one in the event that this class is a nested class.
	 If not a nested class, will return null.

	 @return the owner class, or null
	 */
	@Nullable
	ConfigClassSpecification getOwnerClass();

	/** @return the class name of this specification */
	@NotNull
	String getClassName();
}
