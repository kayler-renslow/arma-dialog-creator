/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
