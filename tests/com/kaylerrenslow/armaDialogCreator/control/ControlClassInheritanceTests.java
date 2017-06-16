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
			//Need to guarantee that the value doesn't change.
			//By differing the types, we can guarantee that there was or wasn't a change.
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
	public void inherit_success_exists_changeAfter() throws Exception {
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
		//so that we can assert that the value actually changed
		tcc2.findRequiredProperty(constant).setValue(AFont.LUCIDA_CONSOLE_B);

		tcc.inheritProperty(constant);

		tcc2.findRequiredProperty(constant).setValue(AFont.DEFAULT);

		assertEquals(AFont.DEFAULT, tcc.findRequiredProperty(constant).getValue());
	}

	@Test
	public void inheritProperty_success_createTemp() throws Exception {
		//inherit property that does not exist in the class, but does in the parent class
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass2 tcc2 = new TestControlClass2(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = ControlPropertyLookup.STYLE;
		if (tcc.findPropertyNullable(constant) != null) {
			throw new IllegalStateException("the constant " + constant + " shouldn't exist in the ControlClass for testing purposes");
		}
		tcc2.findProperty(constant).setValue(new SVString("value"));

		tcc.extendControlClass(tcc2);

		tcc.inheritProperty(constant);

		assertEquals(tcc2.findProperty(constant).getValue(), tcc.findProperty(constant).getValue());
	}

	@Test
	public void inheritProperty_success_exist_wrongLookup() throws Exception {
		//override property that does exist in the class, but the lookups aren't equal

		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass2 tcc2 = new TestControlClass2(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = ControlPropertyLookup.IDC;
		ControlPropertyLookupConstant constantLookalike = TestControlPropertyLookup.IDC;

		tcc2.findProperty(constant).setValue(new SVString("value"));

		tcc.extendControlClass(tcc2);

		tcc.inheritProperty(constantLookalike);

		assertEquals(tcc2.findProperty(constant).getValue(), tcc.findProperty(constant).getValue());
	}

	@Test
	public void overrideProperty_success_exists() throws Exception {
		//override property that exists in the class
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass tcc2 = new TestControlClass(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = TestControlClass.requiredProperties[0];
		SerializableValue oldValue = new SVString("Leroy Jenkins");
		tcc.findProperty(constant).setValue(oldValue);

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
	public void overrideProperty_success_exists_wrongLookup() throws Exception {
		//override property that exists in the class, but lookups aren't the same
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass tcc2 = new TestControlClass(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = ControlPropertyLookup.IDC;
		ControlPropertyLookupConstant constantLookalike = TestControlPropertyLookup.IDC;
		SerializableValue oldValue = new SVString("Leroy Jenkins");
		tcc.findProperty(constant).setValue(oldValue);

		tcc.extendControlClass(tcc2);
		SerializableValue jenny = new SVInteger(867_5309);
		if (constant.getPropertyType() != PropertyType.Int) {
			throw new IllegalStateException("propertyType for the lookup should be int for testing purposes");
		}
		tcc2.findRequiredProperty(constant).setValue(jenny);
		tcc.inheritProperty(constant);
		tcc.overrideProperty(constantLookalike);

		assertEquals(oldValue, tcc.findRequiredProperty(constant).getValue());
	}

	@Test
	public void overrideProperty_success_doesNotExist() throws Exception {
		//override property that does not exist in the class, but was inherited
		TestControlClass tcc = new TestControlClass(new TestSpecRegistry());
		TestControlClass2 tcc2 = new TestControlClass2(new TestSpecRegistry());
		ControlPropertyLookupConstant constant = ControlPropertyLookup.STYLE;
		if (tcc.findPropertyNullable(constant) != null) {
			throw new IllegalStateException("the constant " + constant + " shouldn't exist in the ControlClass for testing purposes");
		}
		tcc2.findProperty(constant).setValue(new SVString("value"));

		tcc.extendControlClass(tcc2);

		tcc.inheritProperty(constant);

		if (tcc.findPropertyNullable(constant) == null) {
			throw new IllegalStateException("the constant " + constant + " SHOULD exist in the ControlClass since it was inherited");
		}

		tcc.overrideProperty(constant);


		//check if the overridden property exists after the override
		assertEquals(constant.getPropertyName(), tcc.findProperty(constant).getName());
	}

}
