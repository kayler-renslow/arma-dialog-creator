package com.kaylerrenslow.armaDialogCreator.gui.fx.control.inputfield;

import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;

/**
 @author Kayler
 Checker for hex strings (#fff for example)
 Created on 05/31/2016. */
public class HexColorFieldDataChecker implements InputFieldDataChecker<AHexColor> {
	@Override
	public boolean validData(String data) {
		try {
			AHexColor.convertToColorArray(data);
			return true;
		} catch (IllegalArgumentException e ) {
			return false;
		}
	}

	@Override
	public AHexColor parse(String data) {
		return new AHexColor(data);
	}

	@Override
	public String getTypeName() {
		return "Hex Color String";
	}
}
