package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 05/15/2017 */
public class ControlClassOldConstructorTests {
	@Test
	public void controlClassConstructor() throws Exception {
		SpecificationRegistry specRegistry = TestSpecRegistry.newInstance();

		ControlClassSpecification reqNested1 = new ControlClassSpecification("requiredNestedClass1",
				Collections.emptyList(),
				Collections.emptyList()
		);
		ControlClassSpecification reqNested2 = new ControlClassSpecification("requiredNestedClass2",
				Collections.emptyList(),
				Collections.emptyList()
		);
		ControlClassSpecification optNested1 = new ControlClassSpecification("optionalNestedClass1",
				Collections.emptyList(),
				Collections.emptyList()
		);
		ControlClassSpecification optNested2 = new ControlClassSpecification("optionalNestedClass2",
				Collections.emptyList(),
				Collections.emptyList()
		);

		ControlPropertyLookupConstant[] requiredProperties = {ControlPropertyLookup.IDC, ControlPropertyLookup.X};
		ControlPropertyLookupConstant[] optionalProperties = {ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.BLINKING_PERIOD};

		ControlClassOld c = new ControlClassOld("className", new ControlClassRequirementSpecification() {
			@NotNull
			@Override
			public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
				return new ReadOnlyList<>(Arrays.asList(reqNested1, reqNested2));
			}

			@NotNull
			@Override
			public ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
				return new ReadOnlyList<>(Arrays.asList(optNested1, optNested2));
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
		}, specRegistry);

		if (!c.getRequiredNestedClasses().get(0).classEquals(reqNested1.constructNewControlClass(specRegistry))) {
			assertEquals("Required nested class 1 of the constructed control class isn't equal to expected ControlClassOld", true, false);
		}

		if (!c.getRequiredNestedClasses().get(1).classEquals(reqNested2.constructNewControlClass(specRegistry))) {
			assertEquals("Required nested class 2 of the constructed control class isn't equal to expected ControlClassOld", true, false);
		}

		if (!c.getOptionalNestedClasses().get(0).classEquals(optNested1.constructNewControlClass(specRegistry))) {
			assertEquals("Optional nested class 1 of the constructed control class isn't equal to expected ControlClassOld", true, false);
		}

		if (!c.getOptionalNestedClasses().get(1).classEquals(optNested2.constructNewControlClass(specRegistry))) {
			assertEquals("Optional nested class 2 of the constructed control class isn't equal to expected ControlClassOld", true, false);
		}
		testPropertiesEqual(requiredProperties, optionalProperties, c);
	}

	/**
	 helper method for seeing if a {@link ControlClassOld} has the required and optional properties given.
	 This method will report any missing properties through the testing library.
	 Note: This will not mutate the given arrays!
	 */
	private void testPropertiesEqual(ControlPropertyLookupConstant[] requiredProperties, ControlPropertyLookupConstant[] optionalProperties, ControlClassOld c) {
		requiredProperties = Arrays.copyOf(requiredProperties, requiredProperties.length);
		optionalProperties = Arrays.copyOf(optionalProperties, optionalProperties.length);
		{
			for (int i = 0; i < requiredProperties.length; i++) {
				ControlPropertyLookupConstant lookup = requiredProperties[i];
				if (c.findRequiredPropertyNullable(lookup) != null) {
					requiredProperties[i] = null;
				}
			}
		}

		{
			for (int i = 0; i < optionalProperties.length; i++) {
				ControlPropertyLookupConstant lookup = optionalProperties[i];
				if (c.findOptionalPropertyNullable(lookup) != null) {
					optionalProperties[i] = null;
				}

			}
		}
		List<ControlPropertyLookupConstant> missingReq = new ArrayList<>();
		List<ControlPropertyLookupConstant> missingOpt = new ArrayList<>();
		{
			for (ControlPropertyLookupConstant lookup : requiredProperties) {
				if (lookup != null) {
					missingReq.add(lookup);
				}
			}
			for (ControlPropertyLookupConstant lookup : optionalProperties) {
				if (lookup != null) {
					missingOpt.add(lookup);
				}
			}
		}
		assertEquals(String.format("Missing properties:\nRequired:%s\nOptional:%s\n", missingReq.toString(), missingOpt.toString()), true, missingOpt.size() == 0 && missingReq.size() == 0);
	}


}
