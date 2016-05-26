package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl;

/**
 Created by Kayler on 05/25/2016.
 */
public enum ControlClassImpl {
	STATIC(StaticControl.class);

	public final Class<? extends ArmaControlClass> clazz;
	ControlClassImpl(Class<? extends ArmaControlClass> clazz) {
		this.clazz = clazz;
	}
}
