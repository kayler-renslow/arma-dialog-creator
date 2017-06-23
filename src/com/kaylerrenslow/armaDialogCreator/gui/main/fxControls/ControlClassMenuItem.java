package com.kaylerrenslow.armaDialogCreator.gui.main.fxControls;

import com.kaylerrenslow.armaDialogCreator.control.ControlClass;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CBMBMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.ImageContainer;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 11/15/2016 */
public class ControlClassMenuItem extends CBMBMenuItem<ControlClass> {

	public ControlClassMenuItem(ControlClass value, @Nullable ImageContainer graphic) {
		super(value, graphic);
	}

	public ControlClassMenuItem(ControlClass value) {
		super(value);
	}
}
