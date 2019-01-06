package com.armadialogcreator.core;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 Tests for find related methods (e.g. {@link ControlClass#findProperty(ControlPropertyLookupConstant)} and {@link ControlClass#findNestedClass(String)})

 @author Kayler
 @since 05/30/2017 */
public class ControlClassFindMethodsTests {

	@Test
	public void getExtendControlClass() throws Exception {
		TestControlClass tcc1 = newTestControlClass();
		assertEquals(null, tcc1.getExtendClass());
	}

	@Test
	public void extendControlClass() throws Exception {
		TestControlClass tcc1 = newTestControlClass();
		TestControlClass tcc2 = newTestControlClass();
		tcc2.setClassName("tcc2");

		tcc1.extendControlClass(tcc2);
		assertEquals(true, tcc1.getExtendClass() == tcc2);
	}

	@Test
	public void extendControlClassUndo() throws Exception {
		TestControlClass tcc1 = newTestControlClass();
		TestControlClass tcc2 = newTestControlClass();
		tcc2.setClassName("tcc2");

		tcc1.extendControlClass(tcc2);
		tcc1.extendControlClass(null);
		assertEquals(null, tcc1.getExtendClass());
	}


	//
	//
	// Control Property finding
	//
	//

	@Test
	public void findRequiredProperty_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findRequiredProperty(TestControlPropertyLookup.FAKE);
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
		assertNull("Should be null", tcc.findRequiredPropertyNullable(TestControlPropertyLookup.FAKE));
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
			tcc.findOptionalProperty(TestControlPropertyLookup.FAKE);
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
		assertNull("Should be null", tcc.findOptionalPropertyNullable(TestControlPropertyLookup.FAKE));
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
			tcc.findProperty(TestControlPropertyLookup.FAKE);
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
		assertNull("Should be null", tcc.findPropertyNullable(TestControlPropertyLookup.FAKE));
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
	public void findPropertyByNameNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findPropertyByNameNullable("---------------"));
	}

	@Test
	public void findPropertyByNameNullable_required_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findPropertyByNameNullable(match.getPropertyName()).getPropertyLookup());
	}

	@Test
	public void findPropertyByNameNullable_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findPropertyByNameNullable(match.getPropertyName()).getPropertyLookup());
	}

	@Test
	public void findRequiredPropertyByName_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match", match, tcc.findRequiredPropertyByNameNullable(match.getPropertyName()).getPropertyLookup());
	}

	@Test
	public void findRequiredPropertyByNameNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findRequiredPropertyByNameNullable("-------------"));
	}

	@Test
	public void findRequiredPropertyByNameNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.requiredProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findRequiredPropertyByNameNullable(match.getPropertyName()).getPropertyLookup());
	}

	@Test
	public void findOptionalPropertyByNameNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findOptionalPropertyByNameNullable(match.getPropertyName()).getPropertyLookup());
	}

	@Test
	public void findOptionalPropertyByNameNullableNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findOptionalPropertyByNameNullable("-------------"));
	}

	@Test
	public void findOptionalPropertyByNameNullableNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		ControlPropertyLookupConstant match = TestControlClass.optionalProperties[0];
		assertEquals("Shouldn't be null and should match.", match, tcc.findOptionalPropertyByNameNullable(match.getPropertyName()).getPropertyLookup());
	}

	//
	//
	// Nested Class finding
	//
	//

	@Test
	public void findRequiredNestedClass_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findRequiredNestedClass(TestFakeControlClass.INSTANCE.getClassName());
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", false, true);
	}

	@Test
	public void findRequiredNestedClass_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.reqNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match", match, tcc.findRequiredNestedClass(match).getClassName());
	}

	@Test
	public void findRequiredNestedClassNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findRequiredNestedClassNullable(TestFakeControlClass.INSTANCE.getClassName()));
	}

	@Test
	public void findRequiredNestedClassNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.reqNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findRequiredNestedClassNullable(match).getClassName());
	}

	@Test
	public void findOptionalNestedClass_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findOptionalNestedClass(TestFakeControlClass.INSTANCE.getClassName());
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", false, true);
	}

	@Test
	public void findOptionalNestedClass_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.optNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findOptionalNestedClass(match).getClassName());
	}

	@Test
	public void findOptionalNestedClassNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findOptionalNestedClassNullable(TestFakeControlClass.INSTANCE.getClassName()));
	}

	@Test
	public void findOptionalNestedClassNullable_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.optNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findOptionalNestedClassNullable(match).getClassName());
	}

	@Test
	public void findNestedClass_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		try {
			tcc.findNestedClass(TestFakeControlClass.INSTANCE.getClassName());
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", false, true);

	}

	@Test
	public void findNestedClass_required_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.reqNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findNestedClass(match).getClassName());
	}

	@Test
	public void findNestedClass_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.optNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findNestedClass(match).getClassName());
	}

	@Test
	public void findNestedClassNullable_fail() throws Exception {
		TestControlClass tcc = newTestControlClass();
		assertNull("Should be null", tcc.findNestedClassNullable(TestFakeControlClass.INSTANCE.getClassName()));
	}

	@Test
	public void findNestedClassNullable_required_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.reqNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findNestedClassNullable(match).getClassName());
	}

	@Test
	public void findNestedClassNullable_optional_succeed() throws Exception {
		TestControlClass tcc = newTestControlClass();
		String match = TestControlClass.optNested.get(0).getClassName();
		assertEquals("Shouldn't be null and should match.", match, tcc.findNestedClassNullable(match).getClassName());
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
