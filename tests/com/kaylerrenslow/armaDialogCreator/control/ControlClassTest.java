package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 @author Kayler
 @since 05/15/2017 */
public class ControlClassTest {
	@Test
	public void controlClassConstructor() throws Exception {
		ControlClass c = new ControlClass("className", new ControlClassRequirementSpecification() {
			@NotNull
			@Override
			public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
				return new ReadOnlyList<>(Collections.emptyList());
			}

			@NotNull
			@Override
			public ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
				return new ReadOnlyList<>(Collections.emptyList());
			}

			@NotNull
			@Override
			public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
				return new ReadOnlyList<>(
						Arrays.asList(ControlPropertyLookup.IDC, ControlPropertyLookup.X)
				);
			}

			@NotNull
			@Override
			public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
				return new ReadOnlyList<>(
						Arrays.asList(ControlPropertyLookup.COLOR_TEXT, ControlPropertyLookup.BLINKING_PERIOD)
				);
			}
		}, SpecRegistry.INSTANCE);
	}

	@Test
	public void controlClassConstructor2() throws Exception {

	}

	@Test
	public void extendControlClass() throws Exception {

	}

	@Test
	public void findRequiredProperty() throws Exception {
	}

	@Test
	public void findRequiredPropertyNullable() throws Exception {
	}

	@Test
	public void findOptionalProperty() throws Exception {
	}

	@Test
	public void findOptionalPropertyNullable() throws Exception {
	}

	@Test
	public void findProperty() throws Exception {
	}

	@Test
	public void findPropertyNullable() throws Exception {
	}

	@Test
	public void findRequiredNestedClass() throws Exception {
	}

	@Test
	public void findRequiredNestedClassNullable() throws Exception {
	}

	@Test
	public void findOptionalNestedClass() throws Exception {
	}

	@Test
	public void findOptionalNestedClassNullable() throws Exception {
	}

	@Test
	public void findNestedClass() throws Exception {
	}

	@Test
	public void findNestedClassNullable() throws Exception {
	}

	@Test
	public void overrideProperty() throws Exception {
	}

	@Test
	public void inheritProperty() throws Exception {
	}

	@Test
	public void classEquals() throws Exception {
	}

	@Test
	public void setTo() throws Exception {
	}

	@Test
	public void propertyIsDefined() throws Exception {
	}

	static class SpecRegistry implements SpecificationRegistry {
		static final SpecRegistry INSTANCE = new SpecRegistry();

		@Override
		@Nullable
		public Macro findMacroByKey(@NotNull String macroKey) {
			return null;
		}

		@Override
		@Nullable
		public ControlClass findControlClassByName(@NotNull String className) {
			return null;
		}

		@Nullable
		@Override
		public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
			return null;
		}

		@Override
		public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch) {

		}
	}

}