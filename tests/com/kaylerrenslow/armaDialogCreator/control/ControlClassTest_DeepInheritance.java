package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVString;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 This test is designed to check if properties that are inherited, and are temporary inherited properties, will be
 inherited by future sub classes.

 @author Kayler
 @since 07/03/2017 */
public class ControlClassTest_DeepInheritance {
	@Test
	public void test1() {
		EmptySpecRegistry registry = new EmptySpecRegistry();
		RscText rscText = new RscText(registry);
		Hint hint = new Hint(registry);
		HintRep hintRep = new HintRep(registry);

		//HintRep's values will all be empty

		SerializableValue style = new SVString("style");
		SerializableValue colorText_rscText = new SVString("colorText RscText");
		SerializableValue colorText_hint = new SVString("colorText Hint");

		rscText.findProperty(ControlPropertyLookup.STYLE).setValue(style);
		rscText.findProperty(ControlPropertyLookup.COLOR_TEXT).setValue(colorText_rscText);
		hint.findProperty(ControlPropertyLookup.COLOR_TEXT).setValue(colorText_hint);

		hint.extendControlClass(rscText);
		hintRep.extendControlClass(hint);

		assertEquals(style, hint.findProperty(ControlPropertyLookup.STYLE).getValue());
		assertEquals(style, hintRep.findProperty(ControlPropertyLookup.STYLE).getValue());
		assertEquals(colorText_hint, hintRep.findProperty(ControlPropertyLookup.COLOR_TEXT).getValue());
		assertEquals(colorText_hint, hint.findProperty(ControlPropertyLookup.COLOR_TEXT).getValue());
	}

	@Test
	public void test2() {
		EmptySpecRegistry registry = new EmptySpecRegistry();
		RscText rscText = new RscText(registry);
		Hint hint = new Hint(registry);
		HintRep hintRep = new HintRep(registry);

		//HintRep's values will all be empty

		SerializableValue style = new SVString("style");
		SerializableValue colorText_rscText = new SVString("colorText RscText");
		SerializableValue colorText_hint = new SVString("colorText Hint");

		rscText.findProperty(ControlPropertyLookup.STYLE).setValue(style);
		rscText.findProperty(ControlPropertyLookup.COLOR_TEXT).setValue(colorText_rscText);
		hint.findProperty(ControlPropertyLookup.COLOR_TEXT).setValue(colorText_hint);

		hint.extendControlClass(rscText);
		hintRep.extendControlClass(hint);

		hint.inheritProperty(ControlPropertyLookup.COLOR_TEXT);

		assertEquals(style, hint.findProperty(ControlPropertyLookup.STYLE).getValue());
		assertEquals(style, hintRep.findProperty(ControlPropertyLookup.STYLE).getValue());
		assertEquals(colorText_rscText, hintRep.findProperty(ControlPropertyLookup.COLOR_TEXT).getValue());
		assertEquals(colorText_rscText, hint.findProperty(ControlPropertyLookup.COLOR_TEXT).getValue());
	}

	static final ControlPropertyLookupConstant[] requiredProperties = {
			ControlPropertyLookup.STYLE
	};

	static final ControlPropertyLookupConstant[] optionalProperties = {
			ControlPropertyLookup.COLOR_TEXT
	};

	/** This class will have {@link #requiredProperties} and {@link #optionalProperties} */
	private class RscText extends ControlClass {
		public RscText(@NotNull SpecificationRegistry registry) {
			super("RscText",
					new ControlClassRequirementSpecification() {
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

	/**
	 This class will have no required properties to force temporary property inheritance.
	 This class will also have {@link #optionalProperties} to check for non temporary property inheritance propogation.
	 */
	private class Hint extends ControlClass {
		public Hint(@NotNull SpecificationRegistry registry) {
			super("Hint",
					new ControlClassRequirementSpecification() {
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
							return new ReadOnlyList<>(Collections.emptyList());
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

	/** This class will have {@link #requiredProperties} and {@link #optionalProperties} */
	private class HintRep extends ControlClass {
		public HintRep(@NotNull SpecificationRegistry registry) {
			super("HintRep",
					new ControlClassRequirementSpecification() {
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

	private class EmptySpecRegistry implements SpecificationRegistry {


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

		@Override
		@Nullable
		public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
			return null;
		}

		@Override
		public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch) {

		}
	}

}


