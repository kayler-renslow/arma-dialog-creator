package com.kaylerrenslow.armaDialogCreator.control;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 Tests for {@link ControlClass} that tests inheritance ({@link ControlClass#extendControlClass(ControlClass)}
 and {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}) in many different ways

 @author Kayler
 @since 05/30/2017 */
public class ControlClassInheritanceTests {
	@Test
	public void overrideProperty_success_exists() throws Exception {
		//override property that exists in the class
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		try {
			tcc.overrideProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", true, false);
	}

	@Test
	public void overrideProperty_success_does_not_exist() throws Exception {
		//override property that does not exist in the class, but was inherited
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		try {
			tcc.overrideProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", true, false);
	}

	@Test
	public void inheritProperty_success_create_temp() throws Exception {
		//inherit property that does not exist in the class, but does in the parent class
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		try {
			tcc.overrideProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", true, false);
	}

	@Test
	public void inheritProperty_success_exist_wrong_lookup() throws Exception {
		//override property that does exist in the class, but the lookups aren't equal and requires a merge
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		try {
			tcc.overrideProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", true, false);
	}
}
