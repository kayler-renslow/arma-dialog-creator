package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

/**
 Handles blinking period (commonly associated with
 {@link com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup#BLINKING_PERIOD}).

 @author Kayler
 @since 11/22/2016 */
public class BlinkControlHandler {
	private double blinkDuration;
	private boolean blinkDurationSet = false;
	private long lastPaint;
	private boolean blinkIn = false;
	private long blinkDurationPast = 0;

	public BlinkControlHandler(@NotNull ControlProperty blinkProperty) {
		blinkProperty.getValueObserver().addListener((observer, oldValue, newValue) -> {
			if (newValue == null) {
				blinkDurationSet = false;
			} else {
				blinkDuration = blinkProperty.getFloatValue() * 1000; //*1000 because its in millis
				blinkDurationSet = true;
			}
		});
	}

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
