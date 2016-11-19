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

import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 @see CustomControlClass
 @since 11/12/2016. */
public class ControlClassSpecification implements ControlClassRequirementSpecification {
	public static final ControlClassSpecification[] EMPTY = new ControlClassSpecification[0];

	private String controlClassName;
	private final ControlPropertySpecification[] requiredProperties;
	private final ControlPropertySpecification[] optionalProperties;
	private final ControlClassSpecification[] requiredNestedClasses;
	private final ControlClassSpecification[] optionalNestedClasses;
	private final ControlPropertyLookup[] requiredPropertiesLookup, optionalPropertiesLookup;
	private final List<ControlPropertySpecification> overriddenProperties = new LinkedList<>();
	private @Nullable String extendClass;

	public ControlClassSpecification(@NotNull String controlClassName, @NotNull ControlPropertySpecification[] requiredProperties, @NotNull ControlPropertySpecification[] optionalProperties,
									 @NotNull ControlClassSpecification[] requiredNestedClasses, @NotNull ControlClassSpecification[] optionalNestedClasses) {
		this.controlClassName = controlClassName;
		this.requiredProperties = requiredProperties;
		this.optionalProperties = optionalProperties;
		this.requiredNestedClasses = requiredNestedClasses;
		this.optionalNestedClasses = optionalNestedClasses;

		if (requiredProperties.length == 0) {
			requiredPropertiesLookup = ControlPropertyLookup.EMPTY;
		} else {
			requiredPropertiesLookup = new ControlPropertyLookup[requiredProperties.length];
			for (int i = 0; i < requiredPropertiesLookup.length; i++) {
				requiredPropertiesLookup[i] = requiredProperties[i].getLookup();
			}
		}
		if (optionalProperties.length == 0) {
			optionalPropertiesLookup = ControlPropertyLookup.EMPTY;
		} else {
			optionalPropertiesLookup = new ControlPropertyLookup[optionalProperties.length];
			for (int i = 0; i < optionalPropertiesLookup.length; i++) {
				optionalPropertiesLookup[i] = optionalProperties[i].getLookup();
			}
		}

	}

	public ControlClassSpecification(@NotNull String controlClassName, @NotNull ControlPropertySpecification[] requiredProperties, @NotNull ControlPropertySpecification[] optionalProperties) {
		this(controlClassName, requiredProperties, optionalProperties, ControlClassSpecification.EMPTY, ControlClassSpecification.EMPTY);
	}

	/** Create a specification from the given {@link ControlClass} */
	public ControlClassSpecification(@NotNull ControlClass controlClass) {
		this.controlClassName = controlClass.getClassName();
		this.requiredProperties = new ControlPropertySpecification[controlClass.getRequiredProperties().size()];
		this.optionalProperties = new ControlPropertySpecification[controlClass.getOptionalProperties().size()];
		this.requiredNestedClasses = controlClass.getSpecProvider().getRequiredNestedClasses();
		this.optionalNestedClasses = controlClass.getSpecProvider().getOptionalNestedClasses();

		for (int i = 0; i < requiredProperties.length; i++) {
			requiredProperties[i] = new ControlPropertySpecification(controlClass.getRequiredProperties().get(i));
		}
		for (int i = 0; i < optionalProperties.length; i++) {
			optionalProperties[i] = new ControlPropertySpecification(controlClass.getOptionalProperties().get(i));
		}

		this.optionalPropertiesLookup = controlClass.getSpecProvider().getOptionalProperties();
		this.requiredPropertiesLookup = controlClass.getSpecProvider().getRequiredProperties();

		if (controlClass.getExtendClass() != null) {
			setExtendClass(controlClass.getExtendClass().getClassName());
		}
	}

	public void setClassName(@NotNull String className) {
		this.controlClassName = className;
	}

	public void setExtendClass(@Nullable String extendClass) {
		this.extendClass = extendClass;
	}

	@Nullable
	public String getExtendClassName() {
		return extendClass;
	}

	@NotNull
	public String getClassName() {
		return controlClassName;
	}

	@NotNull
	@Override
	public ControlClassSpecification[] getRequiredNestedClasses() {
		return requiredNestedClasses;
	}

	@NotNull
	@Override
	public ControlClassSpecification[] getOptionalNestedClasses() {
		return optionalNestedClasses;
	}

	@NotNull
	public ControlPropertySpecification[] getRequiredControlProperties() {
		return requiredProperties;
	}

	@NotNull
	public ControlPropertySpecification[] getOptionalControlProperties() {
		return optionalProperties;
	}

	@NotNull
	@Override
	public ControlPropertyLookup[] getRequiredProperties() {
		return requiredPropertiesLookup;
	}

	@NotNull
	@Override
	public ControlPropertyLookup[] getOptionalProperties() {
		return optionalPropertiesLookup;
	}

	@NotNull
	public List<ControlPropertySpecification> getOverriddenProperties() {
		return overriddenProperties;
	}


	/**
	 Get a {@link ControlPropertySpecification} with the given lookup

	 @return the matched instance, or null if nothing could be matched
	 */
	@Nullable
	public ControlPropertySpecification findOverridenProperty(@NotNull ControlPropertyLookupConstant lookup) {
		for (ControlPropertySpecification propertySpecification : overriddenProperties) {
			if (propertySpecification.getLookup() == lookup) {
				return propertySpecification;
			}
		}
		return null;
	}

	/** Just invokes {@link ControlClass#ControlClass(ControlClassSpecification)} with this instance provided */
	@NotNull
	public ControlClass constructNewControlClass() {
		return new ControlClass(this);
	}

	/**
	 Find a {@link ControlClassSpecification} instance between {@link #getRequiredControlProperties()} and {@link #getOptionalControlProperties()}

	 @param lookup the lookup to fetch instance for
	 @return the instance, or null if nothing could be matched
	 */
	public ControlPropertySpecification findProperty(@NotNull ControlPropertyLookupConstant lookup) {
		for (ControlPropertySpecification property : getRequiredControlProperties()) {
			if (property.getLookup() == lookup) {
				return property;
			}
		}
		for (ControlPropertySpecification property : getOptionalControlProperties()) {
			if (property.getLookup() == lookup) {
				return property;
			}
		}
		return null;
	}
}
