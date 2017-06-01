package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 @author Kayler
 @since 05/30/2017 */
public class ControlClassPropertyTests {


	@Test
	public void getExtendControlClass() throws Exception {
		TestControlClass tcc1 = newTestControlClass();
		assertEquals(null, tcc1.getExtendClass());
	}

	@Test
	public void extendControlClass() throws Exception {
		TestControlClass tcc1 = newTestControlClass();
		TestControlClass tcc2 = newTestControlClass();

		tcc1.extendControlClass(tcc2);
		assertEquals(true, tcc1.getExtendClass() == tcc2);
	}

	@Test
	public void extendControlClassUndo() throws Exception {
		TestControlClass tcc1 = newTestControlClass();
		TestControlClass tcc2 = newTestControlClass();

		tcc1.extendControlClass(tcc2);
		tcc1.extendControlClass(null);
		assertEquals(null, tcc1.getExtendClass());
	}

	@Test
	public void findRequiredProperty_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findRequiredProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", false, true);
	}

	@Test
	public void findRequiredProperty_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match", match, tcc.findRequiredProperty(match).getPropertyLookup());
	}

	@Test
	public void findRequiredPropertyNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findRequiredPropertyNullable(TestFakeControlPropertyLookupConstant.INSTANCE));
	}

	@Test
	public void findRequiredPropertyNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findRequiredPropertyNullable(match).getPropertyLookup());
	}

	@Test
	public void findOptionalProperty_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findOptionalProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", false, true);
	}

	@Test
	public void findOptionalProperty_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findOptionalProperty(match).getPropertyLookup());
	}

	@Test
	public void findOptionalPropertyNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findOptionalPropertyNullable(TestFakeControlPropertyLookupConstant.INSTANCE));
	}

	@Test
	public void findOptionalPropertyNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findOptionalPropertyNullable(match).getPropertyLookup());
	}

	@Test
	public void findProperty_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", false, true);

	}

	@Test
	public void findProperty_required_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findProperty(match).getPropertyLookup());
	}

	@Test
	public void findProperty_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findProperty(match).getPropertyLookup());
	}

	@Test
	public void findPropertyNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findPropertyNullable(TestFakeControlPropertyLookupConstant.INSTANCE));
	}

	@Test
	public void findPropertyNullable_required_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findPropertyNullable(match).getPropertyLookup());
	}

	@Test
	public void findPropertyNullable_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findPropertyNullable(match).getPropertyLookup());
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
	public void classEquals_true() throws Exception {
		assertEquals(true, newTestControlClass().classEquals(newTestControlClass()));
	}

	@Test
	public void classEquals_false() throws Exception {
		TestControlClass tcc = newTestControlClass();
		tcc.setClassName("************************");
		assertEquals(false, newTestControlClass().classEquals(tcc));
	}

	@Test
	public void setTo() throws Exception {
	}

	@Test
	public void propertyIsDefined() throws Exception {
	}

	//helpers

	/** @return new {@link TestControlClass} */
	@NotNull
	private static TestControlClass newTestControlClass(@NotNull SpecificationRegistry registry) {
		return new TestControlClass(registry);
	}

	/** @return new {@link TestControlClass} with a new {@link TestSpecRegistry} */
	@NotNull
	private static TestControlClass newTestControlClass() {
		return newTestControlClass(new TestSpecRegistry());
	}
}
