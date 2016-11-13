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
 Created by Kayler on 11/12/2016.
 */
public class ControlClassSpecification implements ControlClassRequirementSpecification {
	public static final ControlClassSpecification[] EMPTY = new ControlClassSpecification[0];

	private final String controlClassName;
	private final ControlPropertySpecification[] requiredProperties;
	private final ControlPropertySpecification[] optionalProperties;
	private final ControlClassSpecification[] requiredSubClasses;
	private final ControlClassSpecification[] optionalSubClasses;
	private final ControlPropertyLookup[] requiredPropertiesLookup, optionalPropertiesLookup;
	private @Nullable String extendClass;

	public ControlClassSpecification(@NotNull String controlClassName, @NotNull ControlPropertySpecification[] requiredProperties, @NotNull ControlPropertySpecification[] optionalProperties,
									 @NotNull ControlClassSpecification[] requiredSubClasses, @NotNull ControlClassSpecification[] optionalSubClasses) {
		this.controlClassName = controlClassName;
		this.requiredProperties = requiredProperties;
		this.optionalProperties = optionalProperties;
		this.requiredSubClasses = requiredSubClasses;
		this.optionalSubClasses = optionalSubClasses;

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
		this.requiredSubClasses = controlClass.getSpecProvider().getRequiredSubClasses();
		this.optionalSubClasses = controlClass.getSpecProvider().getOptionalSubClasses();

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
	public ControlClassSpecification[] getRequiredSubClasses() {
		return requiredSubClasses;
	}

	@NotNull
	@Override
	public ControlClassSpecification[] getOptionalSubClasses() {
		return optionalSubClasses;
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

	/** Just invokes {@link ControlClass(ControlClassSpecification)} with this instance provided */
	@NotNull
	public ControlClass constructNewControlClass() {
		return new ControlClass(this);
	}
}
