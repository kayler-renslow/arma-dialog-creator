package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 This update happens PRIOR to {@link ControlPropertyValueUpdate} <b>and</b> PRIOR to {@link ControlPropertyInheritUpdate}
 and happens when ever {@link ControlProperty#inherit(ControlProperty)} is invoked.
 This update aims to alert when a {@link ControlProperty}'s value may change due to inheritance.

 @author Kayler
 @since 11/04/2017 */
public class PreemptiveControlPropertyInheritUpdate implements ControlPropertyUpdate {
	private final ControlProperty property;
	private final ControlProperty toInherit;

	public PreemptiveControlPropertyInheritUpdate(@NotNull ControlProperty property, @Nullable ControlProperty toInherit) {
		this.property = property;
		this.toInherit = toInherit;
	}

	/**
	 @return the {@link ControlProperty} instance that created this updated
	 */
	@Override
	@NotNull
	public ControlProperty getControlProperty() {
		return property;
	}

	/**
	 @return the {@link ControlProperty} that {@link #getControlProperty()} is inheriting
	 via {@link ControlProperty#inherit(ControlProperty)}, or null if clearing inheritance
	 */
	@Nullable
	public ControlProperty getToInherit() {
		return toInherit;
	}
}
