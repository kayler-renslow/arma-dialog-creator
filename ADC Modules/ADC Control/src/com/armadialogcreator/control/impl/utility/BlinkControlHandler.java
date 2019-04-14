package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.canvas.Graphics;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.sv.SVInteger;
import com.armadialogcreator.core.sv.SVNumericValue;
import org.jetbrains.annotations.NotNull;

/**
 Handles blinking period (commonly associated with
 {@link ConfigPropertyLookup#BLINKING_PERIOD}).

 @author Kayler
 @since 11/22/2016 */
public class BlinkControlHandler {
	private double blinkDuration;
	private boolean blinkDurationSet = false;
	private long lastPaint;
	private boolean blinkIn = false;
	private long blinkDurationPast = 0;

	public BlinkControlHandler(@NotNull ArmaControlRenderer renderer, @NotNull ConfigPropertyLookupConstant constant) {
		renderer.addValueListener(constant, new SVInteger(-1), (observer, oldValue, newValue) -> {
			double v = ((SVNumericValue) newValue).toDouble();
			blinkDuration = v * 1000; //*1000 because its in millis
			if (blinkDuration <= 0) {
				blinkDurationSet = false;
				return;
			}
			blinkDurationSet = true;
		});
	}

	/**
	 Will manipulate {@link Graphics#getGlobalAlpha()} based on an internal clock.
	 */
	public void paint(@NotNull Graphics g) {
		long now = System.currentTimeMillis();
		long timePast = now - lastPaint;
		lastPaint = now;

		if (!blinkDurationSet) {
			return;
		}

		if (blinkIn) {
			blinkDurationPast += timePast;
			if (blinkDurationPast >= blinkDuration) {
				blinkIn = false;
				blinkDurationPast = (long) blinkDuration;
			}
		} else {
			blinkDurationPast -= timePast;
			if (blinkDurationPast <= 0) {
				blinkIn = true;
				blinkDurationPast = 0;
			}
		}
		g.setGlobalAlpha(blinkDurationPast / blinkDuration);
	}
}
