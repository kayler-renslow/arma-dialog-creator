package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A {@link ControlPropertyLookupConstant} implementation used for testing.
 NEVER USE THIS in a {@link ControlClass} or {@link ControlClassSpecification}, including testing related ones.
 If you do use this, you are defeating the purpose of this class,
 which is for guaranteeing no {@link ControlPropertyLookupConstant} is found via a {@link ControlClass#findProperty(ControlPropertyLookupConstant)} related method..

 @author Kayler
 @since 05/30/2017 */
public class TestFakeControlPropertyLookupConstant implements ControlPropertyLookupConstant {
	public static final ControlPropertyLookupConstant INSTANCE = new TestFakeControlPropertyLookupConstant();

	@Nullable
	@Override
	public ControlPropertyOption[] getOptions() {
		return new ControlPropertyOption[0];
	}

	@Override
	public @NotNull String getPropertyName() {
		return "**FAKE PROPERTY**";
	}

	@Override
	public @NotNull PropertyType getPropertyType() {
		return PropertyType.Int;
	}

	@Override
	public int getPropertyId() {
		return 0;
	}

	@Override
	public String getAbout() {
		return null;
	}
}
