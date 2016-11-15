/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.util;

import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.UIScale;

/**
 Constant values for Arma 3's UI scale
 @author Kayler
 @since 05/18/2016. */
public enum ArmaUIScale implements UIScale {
	VERY_LARGE("Very Large", 1), LARGE("Large", 0.85), MEDIUM("Medium", 0.7), SMALL("Small", 0.55), VERY_SMALL("Very Small", 0.47);
	
	public final String label;
	public final double value;
	
	ArmaUIScale(String label, double value) {
		this.label = label;
		this.value = value;
	}
	
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public double getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
	/** Constant field for default UIScale */
	public static final ArmaUIScale DEFAULT = SMALL;
}
