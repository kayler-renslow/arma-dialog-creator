package com.armadialogcreator.control;

import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 A {@link ControlClass} used for testing. DO NOT MODIFY THIS CLASS.

 @author Kayler
 @since 05/30/2017 */
class TestControlClass extends ControlClass {
	/** All required nested {@link ControlClassSpecification} used */
	static final List<ControlClassSpecification> reqNested = Arrays.asList(
			new ControlClassSpecification("requiredNestedClass1",
					Collections.emptyList(),
					Collections.emptyList()
			),
			new ControlClassSpecification("requiredNestedClass2",
					Collections.emptyList(),
					Collections.emptyList()
			)
	);
	/** All optional nested {@link ControlClassSpecification} used */
	static final List<ControlClassSpecification> optNested = Arrays.asList(
			new ControlClassSpecification("optionalNestedClass1",
					Collections.emptyList(),
					Collections.emptyList()
			),
			new ControlClassSpecification("optionalNestedClass2",
					Collections.emptyList(),
					Collections.emptyList()
			)
	);

	/** All required {@link ControlPropertyLookupConstant} used */
	static final ControlPropertyLookupConstant[] requiredProperties = {ControlPropertyLookup.IDC, ControlPropertyLookup.X};
	/** All optional {@link ControlPropertyLookupConstant} used */
	static final ControlPropertyLookupConstant[] optionalProperties = {ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.BLINKING_PERIOD};

	public TestControlClass(@NotNull SpecificationRegistry registry) {
		super("TestControlClass",
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
					public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
						return new ReadOnlyList<>(Arrays.asList(requiredProperties));
					}

					@NotNull
					@Override
					public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
						return new ReadOnlyList<>(Arrays.asList(optionalProperties));
					}
				},
				registry
		);
	}
}
