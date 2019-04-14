package com.armadialogcreator.core.sv;

import com.armadialogcreator.util.ArmaPrecision;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 @author Kayler
 @since 08/17/2017 */
public class SVColorArrayTest {

	@Test
	public void getColors() throws Exception {
		SVColorArray array = new SVColorArray(0.5, 0.5, 0.5, 0.5);
		assertEquals(true,
				ArmaPrecision.isEqualTo(array.getRedF(), 0.5) &&
						ArmaPrecision.isEqualTo(array.getGreenF(), 0.5) &&
						ArmaPrecision.isEqualTo(array.getBlueF(), 0.5) &&
						ArmaPrecision.isEqualTo(array.getAlphaF(), 0.5)
		);
	}

	@Test
	public void getAsStringArray() throws Exception {
		SVColorArray array = new SVColorArray(0.5, 0.5, 0.5, 0.5);
		assertArrayEquals(new String[]{"0.5", "0.5", "0.5", "0.5"}, array.getAsStringArray());
	}

	@Test
	public void asString() throws Exception {
		SVColorArray array = new SVColorArray(0.5, 0.5, 0.5, 0.5);
		assertEquals("{0.5, 0.5, 0.5, 0.5}", array.toString());
	}

}
