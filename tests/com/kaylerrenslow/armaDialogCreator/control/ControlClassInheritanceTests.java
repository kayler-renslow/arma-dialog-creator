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
	public void overrideProperty_fail() throws Exception {
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		try {
			tcc.overrideProperty(TestFakeControlPropertyLookupConstant.INSTANCE);
		} catch (IllegalArgumentException e) {
			return;
		}
		assertEquals("Expected an exception", true, false);
	}


}
