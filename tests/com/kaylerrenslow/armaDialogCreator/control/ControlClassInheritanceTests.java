package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.AFont;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVInteger;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVString;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 Tests for {@link ControlClass} that tests inheritance ({@link ControlClass#extendControlClass(ControlClass)}
 and {@link ControlClass#overrideProperty(ControlPropertyLookupConstant)}) in many different ways

 @author Kayler
 @since 05/30/2017 */
public class ControlClassInheritanceTests {
	@Test
	public void inherit_checkNotInherited() throws Exception {
		//set property that exists in the class, but make sure that it wasn't inherited
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass tcc2 = new TestControlClass(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = TestControlClass.requiredProperties[0];
		tcc.findRequiredProperty(constant).setValue(new SVString("Woah"));

		tcc.extendControlClass(tcc2);
		SerializableValue jenny = new SVInteger(867_5309);
		if (constant.getPropertyType() != PropertyType.Int) {
			throw new IllegalStateException("propertyType for the lookup should be int for testing purposes");
		}
		tcc2.findRequiredProperty(constant).setValue(jenny);

		assertNotEquals(jenny, tcc.findRequiredProperty(constant).getValue());
	}

	//todo we need tests that test for creating a temporary inherited property from a long change of inheritance (A extends B, B extends C, etc)
	//and test if the changes are properly handled when B no longer extends C for example

	@Test
	public void inherit_checkInherited() throws Exception {
		//inherit property that exists in the class
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass tcc2 = new TestControlClass(new TestSpecRegistry());
		tcc.extendControlClass(tcc2);
		SerializableValue jenny = new SVInteger(867_5309);
		ControlPropertyLookupConstant constant = TestControlClass.requiredProperties[0];
		if (constant.getPropertyType() != PropertyType.Int) {
			throw new IllegalStateException("propertyType for the lookup should be int for testing purposes");
		}
		tcc2.findRequiredProperty(constant).setValue(jenny);
		tcc.inheritProperty(constant);

		assertEquals(jenny, tcc.findRequiredProperty(constant).getValue());
	}

	@Test
	public void inherit_success_exists_change_after() throws Exception {
		//test value updates to inherited properties
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass tcc2 = new TestControlClass(new TestSpecRegistry());
		tcc.extendControlClass(tcc2);
		ControlPropertyLookupConstant constant = TestControlClass.requiredProperties[0];
		if (constant.getPropertyType() != PropertyType.Int) {
			// we need to make sure that the current value in the ControlClass will guarantee an update.
			// an int to a font will require an update since we are expecting a font later in the test
			throw new IllegalStateException("propertyType for the lookup should be int for testing purposes");
		}

		//make sure this value is not equal to what the merge method returns
		//so that we can assert that the value actually changed with the merge method!
		tcc2.findRequiredProperty(constant).setValue(AFont.LUCIDA_CONSOLE_B);

		tcc.inheritProperty(constant);

		tcc2.findRequiredProperty(constant).setValue(AFont.DEFAULT);

		assertEquals(AFont.DEFAULT, tcc.findRequiredProperty(constant).getValue());
	}

	@Test
	public void inheritProperty_success_create_temp() throws Exception { //todo
		//inherit property that does not exist in the class, but does in the parent class

		throw new RuntimeException("todo");
	}

	@Test
	public void inheritProperty_success_exist_wrong_lookup() throws Exception { //todo
		//override property that does exist in the class, but the lookups aren't equal and requires a merge

		throw new RuntimeException("todo");
	}

	@Test
	public void overrideProperty_success_exists() throws Exception {
		//override property that exists in the class
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass tcc2 = new TestControlClass(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = TestControlClass.requiredProperties[0];
		SerializableValue oldValue = tcc.findProperty(constant).getValue();

		tcc.extendControlClass(tcc2);
		SerializableValue jenny = new SVInteger(867_5309);
		if (constant.getPropertyType() != PropertyType.Int) {
			throw new IllegalStateException("propertyType for the lookup should be int for testing purposes");
		}
		tcc2.findRequiredProperty(constant).setValue(jenny);
		tcc.inheritProperty(constant);
		tcc.overrideProperty(constant);

		assertEquals(oldValue, tcc.findRequiredProperty(constant).getValue());
	}

	@Test
	public void overrideProperty_success_does_not_exist() throws Exception { //todo
		//override property that does not exist in the class, but was inherited

		throw new RuntimeException("todo");
	}

}
