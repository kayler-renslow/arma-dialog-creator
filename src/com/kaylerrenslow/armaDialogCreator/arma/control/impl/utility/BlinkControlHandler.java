package com.kaylerrenslow.armaDialogCreator.arma.control.impl.utility;

import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import javafx.scene.canvas.GraphicsContext;
import org.jetbrains.annotations.NotNull;

/**
 Handles blinking period (commonly associated with {@link com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup#BLINKING_PERIOD}).

 @author Kayler
 @since 11/22/2016 */
public class BlinkControlHandler {
	private final ControlProperty blinkProperty;
	private long lastPaint;
	private boolean blinkIn = false;
	private long blinkDurationPast = 0;

	public BlinkControlHandler(@NotNull ControlProperty blinkProperty) {
		this.blinkProperty = blinkProperty;
	}

	public void paint(@NotNull GraphicsContext gc, @NotNull DataContext dataContext) {
		long now = System.currentTimeMillis();

		if (blinkingDuration() != -1) {
			double blinkDuration = blinkingDuration();
			long timePast = now - lastPaint;
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

		lastPaint = now;
	}

	/** return how many seconds duration lasts (in milliseconds) */
	private double blinkingDuration() {
		if (blinkProperty.getValue() == null) {
			return -1;
		}
		return blinkProperty.getFloatValue() * 1000;
	}
}
