package com.armadialogcreator.core;

import com.armadialogcreator.core.old.*;
import com.armadialogcreator.core.sv.SVString;
import com.armadialogcreator.core.sv.SerializableValue;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 This test is designed to check if properties that are inherited, and are temporary inherited properties, will be
 inherited by future sub classes.

 @author Kayler
 @since 07/03/2017 */
public class ControlClassOldTest_DeepInheritance {
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

		rscText.findProperty(ConfigPropertyLookup.STYLE).setValue(style);
		rscText.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_rscText);
		hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_hint);

		hint.extendControlClass(rscText);
		hintRep.extendControlClass(hint);

		assertEquals(style, hint.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(style, hintRep.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(colorText_hint, hintRep.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
		assertEquals(colorText_hint, hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
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

		rscText.findProperty(ConfigPropertyLookup.STYLE).setValue(style);
		rscText.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_rscText);
		hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_hint);

		hint.extendControlClass(rscText);
		hintRep.extendControlClass(hint);

		hint.inheritProperty(ConfigPropertyLookup.COLOR_TEXT);

		assertEquals(style, hint.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(style, hintRep.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(colorText_rscText, hintRep.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
		assertEquals(colorText_rscText, hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
	}

	@Test
	public void test3() {
		EmptySpecRegistry registry = new EmptySpecRegistry();
		RscText rscText = new RscText(registry);
		Hint hint = new Hint(registry);
		HintRep hintRep = new HintRep(registry);

		//HintRep's values will all be empty

		SerializableValue style = new SVString("style");
		SerializableValue colorText_rscText = new SVString("colorText RscText");
		SerializableValue colorText_hint = new SVString("colorText Hint");

		rscText.findProperty(ConfigPropertyLookup.STYLE).setValue(style);
		rscText.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_rscText);
		hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_hint);

		hint.extendControlClass(rscText);
		hintRep.extendControlClass(hint);

		hint.inheritProperty(ConfigPropertyLookup.COLOR_TEXT);

		hint.overrideProperty(ConfigPropertyLookup.COLOR_TEXT);

		assertEquals(style, hint.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(style, hintRep.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(colorText_rscText, hintRep.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
		assertEquals(colorText_hint, hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
	}

	@Test
	public void test4() {
		//this tests have a chain of inheritance and if the control classes react appropriately if a control class
		//somewhere in the middle clears its extend control class (says extendControlClass(null))

		EmptySpecRegistry registry = new EmptySpecRegistry();
		RscText rscText = new RscText(registry);
		Hint hint = new Hint(registry);
		HintRep hintRep = new HintRep(registry);
		HintWithNestedClass hintWithNestedClass = new HintWithNestedClass("HintWithNestedClass", registry);
		Empty empty = new Empty("Empty", registry);
		Empty empty2 = new Empty("Empty2", registry);

		//HintRep's values will all be empty

		SerializableValue style = new SVString("style");
		SerializableValue colorText_rscText = new SVString("colorText RscText");
		SerializableValue colorText_hint = new SVString("colorText Hint");
		SerializableValue hintWithNestedClassPropertyValue = new SVString("hintWithNestedClassPropertyValue");

		rscText.findProperty(ConfigPropertyLookup.STYLE).setValue(style);
		rscText.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_rscText);
		hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).setValue(colorText_hint);

		ControlClassOld hintWithNestedClass_controlClass = hintWithNestedClass.findNestedClass(hintWithNestedClass_nestedClassName);
		hintWithNestedClass_controlClass.findProperty(hintWithNestedClass_propertyLookup).setValue(hintWithNestedClassPropertyValue);

		hint.extendControlClass(rscText);
		hintWithNestedClass.extendControlClass(rscText);
		empty.extendControlClass(hintWithNestedClass);
		empty2.extendControlClass(empty);
		hintRep.extendControlClass(hint);

		hint.inheritProperty(ConfigPropertyLookup.COLOR_TEXT);

		assertEquals(true, hintWithNestedClass_controlClass == empty.findNestedClass(hintWithNestedClass_nestedClassName));
		assertEquals(true, hintWithNestedClass_controlClass == empty2.findNestedClass(hintWithNestedClass_nestedClassName));
		assertEquals(hintWithNestedClassPropertyValue, empty.findNestedClass(hintWithNestedClass_nestedClassName).findProperty(hintWithNestedClass_propertyLookup).getValue());

		hint.extendControlClass(null);
		empty.extendControlClass(null);

		assertEquals(null, empty.findNestedClassNullable(hintWithNestedClass_nestedClassName));
		assertEquals(null, empty2.findNestedClassNullable(hintWithNestedClass_nestedClassName));
		assertEquals(hintWithNestedClassPropertyValue, hintWithNestedClass.findNestedClass(hintWithNestedClass_nestedClassName).findProperty(hintWithNestedClass_propertyLookup).getValue());


		assertEquals(null, hint.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(null, hintRep.findProperty(ConfigPropertyLookup.STYLE).getValue());
		assertEquals(colorText_hint, hintRep.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
		assertEquals(colorText_hint, hint.findProperty(ConfigPropertyLookup.COLOR_TEXT).getValue());
	}


	//	@Test
	//	public void forceInheritNestedClasses() {
	//		//this tests for forcibly inheriting nested classes
	//
	//		EmptySpecRegistry registry = new EmptySpecRegistry();
	//		HintWithNestedClass hintWithNestedClass = new HintWithNestedClass("HintWithNestedClass", registry);
	//		HintWithNestedClass hintWithNestedClass2 = new HintWithNestedClass("HintWithNestedClass2", registry);
	//		Empty empty = new Empty("Empty", registry);
	//		Empty empty2 = new Empty("Empty2", registry);
	//
	//		SerializableValue nestedClassValue1 = new SVString("nestedClassValue1");
	//		SerializableValue nestedClassValue2 = new SVString("nestedClassValue2");
	//		hintWithNestedClass.findNestedClass(hintWithNestedClass_nestedClassName).findPropertySpecification(hintWithNestedClass_propertyLookup).setValue(nestedClassValue1);
	//		hintWithNestedClass2.findNestedClass(hintWithNestedClass_nestedClassName).findPropertySpecification(hintWithNestedClass_propertyLookup).setValue(nestedClassValue2);
	//
	//		hintWithNestedClass2.extendControlClass(hintWithNestedClass);
	//		empty.extendControlClass(hintWithNestedClass2);
	//		empty2.extendControlClass(empty);
	//
	//		hintWithNestedClass2.inheritNestedClass(hintWithNestedClass_nestedClassName);
	//
	//		assertEquals(nestedClassValue1, hintWithNestedClass2.findNestedClass(hintWithNestedClass_nestedClassName).findPropertySpecification(hintWithNestedClass_propertyLookup).getValue());
	//		assertEquals(nestedClassValue1, empty.findNestedClass(hintWithNestedClass_nestedClassName).findPropertySpecification(hintWithNestedClass_propertyLookup).getValue());
	//		assertEquals(nestedClassValue1, empty2.findNestedClass(hintWithNestedClass_nestedClassName).findPropertySpecification(hintWithNestedClass_propertyLookup).getValue());
	//	}

	static final ConfigPropertyLookupConstant[] requiredProperties = {
			ConfigPropertyLookup.STYLE
	};

	static final ConfigPropertyLookupConstant[] optionalProperties = {
			ConfigPropertyLookup.COLOR_TEXT
	};

	static final ConfigPropertyLookup hintWithNestedClass_propertyLookup = ConfigPropertyLookup.ANIM_TEXTURE_DEFAULT;

	static final String hintWithNestedClass_nestedClassName = "HintWithNestedClass_NestedClass";

	/** This class will have {@link #requiredProperties} and {@link #optionalProperties} */
	private class RscText extends ControlClassOld {
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

	/**
	 This class will have no required properties to force temporary property inheritance.
	 This class will also have {@link #optionalProperties} to check for non temporary property inheritance propogation.
	 */
	private class Hint extends ControlClassOld {
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
						public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
							return new ReadOnlyList<>(Collections.emptyList());
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

	/**
	 This class will have no required properties to force temporary property inheritance.
	 This class will also have {@link #optionalProperties} to check for non temporary property inheritance propagation.
	 This class will also have a required nested class that has 1 required property itself
	 */
	private class HintWithNestedClass extends ControlClassOld {

		public HintWithNestedClass(@NotNull String className, @NotNull SpecificationRegistry registry) {
			super(className,
					new ControlClassRequirementSpecification() {
						@NotNull
						@Override
						public ReadOnlyList<ControlClassSpecification> getRequiredNestedClasses() {
							return new ReadOnlyList<>(Arrays.asList(
									new ControlClassSpecification(
											hintWithNestedClass_nestedClassName,
											Arrays.asList(
													new ControlPropertySpecification(
															hintWithNestedClass_propertyLookup
													)
											),
											Collections.emptyList()
									)
							));
						}

						@NotNull
						@Override
						public ReadOnlyList<ControlClassSpecification> getOptionalNestedClasses() {
							return new ReadOnlyList<>(Collections.emptyList());
						}

						@NotNull
						@Override
						public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
							return new ReadOnlyList<>(Collections.emptyList());
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

	/** This class will have {@link #requiredProperties} and {@link #optionalProperties} */
	private class HintRep extends ControlClassOld {
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

	/**
	 This class will have no properties and no nested classes.
	 */
	private class Empty extends ControlClassOld {
		public Empty(@NotNull String className, @NotNull SpecificationRegistry registry) {
			super(className,
					new ControlClassRequirementSpecification() {
					},
					registry
			);
		}
	}

}


