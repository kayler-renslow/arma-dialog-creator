package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 05/04/2017 */
public class ControlPropertyTest {

	@Test
	public void setValue() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		SerializableValue hi = new SVString("hi");
		p.setValue(hi);
		assertEquals(p.getValue(), hi);
	}

	@Test
	public void setCustomDataValue() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		Object c = "custom data";
		p.setCustomDataValue(c);
		assertEquals(p.getCustomData(), c);
	}

	@Test
	public void setUsingCustomData() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		p.setUsingCustomData(true);
		assertEquals(p.isUsingCustomData(), true);
	}

	@Test
	public void setDefaultValue() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		SerializableValue v = new SVString("hello");
		p.setDefaultValue(false, v);
		assertEquals(p.getDefaultValue(), v);
	}

	@Test
	public void setDefaultValue1() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		p.setDefaultValue(false, 0);
		assertEquals(p.getDefaultValue(), new SVInteger(0));
	}

	@Test
	public void setDefaultValue2() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		p.setDefaultValue(false, 1.0);
		assertEquals(p.getDefaultValue(), new SVDouble(1.0));
	}

	@Test
	public void setDefaultValue3() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		p.setDefaultValue(false, "he");
		assertEquals(p.getDefaultValue(), new SVString("he"));
	}

	@Test
	public void setValueToMacro() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		Macro m = Macro.newMacro("KEY", new SVString("Macro Value"));
		p.setValueToMacro(m);
		assertEquals(p.getValue(), m.getValue());
	}

	@Test
	public void getName() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		assertEquals(p.getName(), ControlPropertyLookup.IDC.getPropertyName());
	}

	@Test
	public void isPropertyType() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, (SerializableValue) null);
		assertEquals(p.isPropertyType(PropertyType.Array), false);
	}

	@Test
	public void isPropertyType2() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, new SVInteger(1));
		assertEquals(p.isPropertyType(PropertyType.Int), true);
	}

	@Test
	public void getInitialPropertyType() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, new SVInteger(1));
		assertEquals(p.getInitialPropertyType(), ControlPropertyLookup.IDC.getPropertyType());
	}

	@Test
	public void getIntValue() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, new SVInteger(1));
		assertEquals(p.getIntValue(), 1);
	}

	@Test
	public void getFloatValue() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, new SVDouble(42.1));
		assertEquals(p.getFloatValue(), 42.1, 0.000001);
	}

	@Test
	public void getBooleanValue() throws Exception {
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, SVBoolean.TRUE);
		assertEquals(p.getBooleanValue(), true);
	}

	@Test
	public void deepCopy_succeed() throws Exception {
		SVColor color = new SVColor(0, 0, 0, 1);
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, color);
		ControlProperty pCopy = p.deepCopy();
		assertEquals(true, p != pCopy && p.equals(pCopy));
	}

	@Test
	public void deepCopy_fail() throws Exception {
		SVColor color = new SVColor(0, 0, 0, 1);
		ControlProperty p = new ControlProperty(ControlPropertyLookup.IDC, color);
		ControlProperty pCopy = p.deepCopy();
		pCopy.setValue(new SVString("Why so serious?"));
		assertEquals(false, p.equals(pCopy));
	}

	@Test
	public void setTo() throws Exception {
		throw new RuntimeException("todo");
	}

	@Test
	public void setTo1() throws Exception {
		throw new RuntimeException("todo");
	}

	@Test
	public void inherit() throws Exception {
		throw new RuntimeException("todo");
	}

	@Test
	public void isInherited() throws Exception {
		throw new RuntimeException("todo");
	}

	@Test
	public void getInherited() throws Exception {
		throw new RuntimeException("todo");
	}

}