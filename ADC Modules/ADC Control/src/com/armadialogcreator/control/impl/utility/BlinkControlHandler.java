package com.armadialogcreator.control.impl.utility;

import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.sv.SVNumericValue;
import javafx.scene.canvas.GraphicsContext;
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
		renderer.addValueListener(constant, (observer, oldValue, newValue) -> {
			if (newValue == null || !(newValue instanceof SVNumericValue)) {
				blinkDurationSet = false;
			} else {
				blinkDuration = ((SVNumericValue) newValue).toDouble() * 1000; //*1000 because its in millis
				if (blinkDuration <= 0) {
					blinkDurationSet = false;
					return;
				}
				blinkDurationSet = true;
			}
		});
	}

	/**
	 Will manipulate {@link GraphicsContext#getGlobalAlpha()} based on an internal clock.
	 */
	public void paint(@NotNull GraphicsContext gc) {
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
		gc.setGlobalAlpha(blinkDurationPast / blinkDuration);
	}
}
