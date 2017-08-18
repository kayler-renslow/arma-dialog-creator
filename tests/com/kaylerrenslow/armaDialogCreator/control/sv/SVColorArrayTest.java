package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaPrecision;
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
				ArmaPrecision.isEqualTo(array.getRed(), 0.5) &&
						ArmaPrecision.isEqualTo(array.getGreen(), 0.5) &&
						ArmaPrecision.isEqualTo(array.getBlue(), 0.5) &&
						ArmaPrecision.isEqualTo(array.getAlpha(), 0.5)
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