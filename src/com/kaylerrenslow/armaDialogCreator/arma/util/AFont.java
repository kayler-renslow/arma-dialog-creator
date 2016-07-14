package com.kaylerrenslow.armaDialogCreator.arma.util;

import com.kaylerrenslow.armaDialogCreator.control.SerializableValue;

/**
 @author Kayler
 All avaialable Arma 3 fonts
 Created on 05/22/2016. */
public enum AFont implements SerializableValue {
	PuristaLight,
	PuristaMedium,
	PuristaSemiBold,
	PuristaBold,
	LucidaConsoleB,
	EtelkaMonospacePro,
	EtelkaMonospaceProBold,
	EtelkaNarrowMediumPro,
	TahomaB;

	/** Default Arma 3 font */
	public static AFont DEFAULT = PuristaMedium;

	private String[] meAsArray = new String[]{name()};

	@Override
	public String toString() {
		return name();
	}


	@Override
	public String[] getAsStringArray() {
		return meAsArray;
	}
}
