package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 This update is created at the end of {@link ControlProperty#inherit(ControlProperty)}.

 @see PreemptiveControlPropertyInheritUpdate
 @author Kayler
 @since 11/20/2016 */
public class ControlPropertyInheritUpdate implements ControlPropertyUpdate {
	private final ControlProperty property;
	private final ControlProperty inheritedProperty;

	public ControlPropertyInheritUpdate(@NotNull ControlProperty property, @Nullable ControlProperty inheritedProperty) {
		this.property = property;
		this.inheritedProperty = inheritedProperty;
	}

	@Override
	@NotNull
	public ControlProperty getControlProperty() {
		return property;
	}

	@Nullable
	public ControlProperty getInheritedProperty() {
		return inheritedProperty;
	}

	public boolean wasInherited() {
		return inheritedProperty != null;
	}
}
