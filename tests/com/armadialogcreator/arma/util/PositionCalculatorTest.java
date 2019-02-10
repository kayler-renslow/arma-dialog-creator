package com.armadialogcreator.arma.util;

import com.armadialogcreator.util.ScreenDimension;
import org.junit.Test;

/**
 @author Kayler
 @since 08/17/2017 */
public class PositionCalculatorTest {
	private final ArmaResolution resolution = new ArmaResolution(ScreenDimension.D960, ArmaUIScale.SMALL);

	@Test
	public void getSafeZoneExpressionX() throws Exception {
		assertEquals("safeZoneX + safeZoneW * 0.5", PositionCalculator.getSafeZoneExpressionX(resolution, resolution.getScreenWidth() / 2));
	}

	@Test
	public void getSafeZoneExpressionY() throws Exception {
		assertEquals("safeZoneY + safeZoneH * 0.5", PositionCalculator.getSafeZoneExpressionY(resolution, resolution.getScreenHeight() / 2));
	}

	@Test
	public void getSafeZoneExpressionW() throws Exception {
		assertEquals("safeZoneW * 0.5", PositionCalculator.getSafeZoneExpressionW(resolution, resolution.getScreenWidth() / 2));
	}

	@Test
	public void getSafeZoneExpressionH() throws Exception {
		assertEquals("safeZoneH * 0.5", PositionCalculator.getSafeZoneExpressionH(resolution, resolution.getScreenHeight() / 2));
	}

}
