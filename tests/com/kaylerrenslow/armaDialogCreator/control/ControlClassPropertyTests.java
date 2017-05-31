package com.kaylerrenslow.armaDialogCreator.control;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

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
		assertNotNull("Shouldn't be null", tcc.findRequiredProperty(TestControlClass.requiredProperties[0]));
	}

	@Test
	public void findRequiredPropertyNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findRequiredPropertyNullable(TestFakeControlPropertyLookupConstant.INSTANCE));
	}

	@Test
	public void findRequiredPropertyNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNotNull("Shouldn't be null.", tcc.findRequiredPropertyNullable(TestControlClass.requiredProperties[0]));
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
		assertNotNull("Shouldn't be null", tcc.findOptionalProperty(TestControlClass.requiredProperties[0]));
	}

	@Test
	public void findOptionalPropertyNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findOptionalPropertyNullable(TestFakeControlPropertyLookupConstant.INSTANCE));
	}

	@Test
	public void findOptionalPropertyNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNotNull("Shouldn't be null.", tcc.findOptionalPropertyNullable(TestControlClass.requiredProperties[0]));
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
		assertNotNull("Shouldn't be null", tcc.findProperty(TestControlClass.requiredProperties[0]));
	}

	@Test
	public void findProperty_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNotNull("Shouldn't be null", tcc.findProperty(TestControlClass.optionalProperties[0]));
	}

	@Test
	public void findPropertyNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findProperty(TestFakeControlPropertyLookupConstant.INSTANCE));
	}

	@Test
	public void findPropertyNullable_required_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNotNull("Shouldn't be null", tcc.findProperty(TestControlClass.requiredProperties[0]));
	}

	@Test
	public void findPropertyNullable_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNotNull("Shouldn't be null", tcc.findProperty(TestControlClass.optionalProperties[0]));
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
