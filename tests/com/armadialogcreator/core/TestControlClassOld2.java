package com.armadialogcreator.core;

import com.armadialogcreator.core.old.*;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 A {@link ControlClassOld} used for testing. DO NOT MODIFY THIS CLASS.

 @author Kayler
 @since 06/12/2017 */
class TestControlClassOld2 extends ControlClassOld {
	/** All required nested {@link ControlClassSpecification} used */
	static final List<ControlClassSpecification> reqNested = Collections.emptyList();
	/** All optional nested {@link ControlClassSpecification} used */
	static final List<ControlClassSpecification> optNested = Collections.emptyList();

	/** All required {@link ConfigPropertyLookupConstant} used */
	static final ConfigPropertyLookupConstant[] requiredProperties = {ConfigPropertyLookup.IDC, ConfigPropertyLookup.X, ConfigPropertyLookup.STYLE};
	/** All optional {@link ConfigPropertyLookupConstant} used */
	static final ConfigPropertyLookupConstant[] optionalProperties = {ConfigPropertyLookup.COLOR_TEXT, ConfigPropertyLookup.BLINKING_PERIOD, ConfigPropertyLookup.ALIGN};

	public TestControlClassOld2(@NotNull SpecificationRegistry registry) {
		super("TestControlClassOld",
				new ControlClassRequirementSpecification() {
					@NotNull
					@Override
					public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
						return new ReadOnlyList<>(reqNested);
					}

					@NotNull
					@Override
					public ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
						return new ReadOnlyList<>(optNested);
					}

					@NotNull
					@Override
					public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
						return new ReadOnlyList<>(Arrays.asList(requiredProperties));
					}

					@NotNull
					@Override
					public ReadOnlyList<ConfigPropertyLookupConstant> getOptionalProperties() {
						return new ReadOnlyList<>(Arrays.asList(optionalProperties));
					}
				},
				registry
		);
	}
}
