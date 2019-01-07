package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A {@link ControlPropertyLookupConstant} implementation used for testing. Do not use outside testing methods/classes.

 @author Kayler
 @since 05/30/2017 */
public enum TestControlPropertyLookup implements ControlPropertyLookupConstant {

	/**
	 NEVER USE THIS in a {@link ControlClassOld} or {@link ControlClassSpecification}, <b>including testing related ones</b>.
	 If you do use this, you are defeating the purpose of this instance, which is for guaranteeing no {@link ControlPropertyLookupConstant} is found via a
	 {@link ControlClassOld#findProperty(ControlPropertyLookupConstant)} related method.
	 */
	FAKE("**FAKE PROPERTY**", null),
	/***/
	IDC("idc", ControlPropertyLookup.IDC);

	private final String name;
	private final ControlPropertyLookup resemble;

	TestControlPropertyLookup(@NotNull String name, @Nullable ControlPropertyLookup resemble) {
		this.name = name;
		this.resemble = resemble;
	}

	@Nullable
	@Override
	public ControlPropertyOption[] getOptions() {
		return resemble != null ? resemble.getOptions() : null;
	}

	@Override
	public @NotNull String getPropertyName() {
		return resemble != null ? resemble.getPropertyName() : name;
	}

	@Override
	public @NotNull PropertyType getPropertyType() {
		return resemble != null ? resemble.getPropertyType() : PropertyType.Int;
	}

	@Override
	public int getPropertyId() {
		return resemble != null ? resemble.getPropertyId() : 0;
	}
}
