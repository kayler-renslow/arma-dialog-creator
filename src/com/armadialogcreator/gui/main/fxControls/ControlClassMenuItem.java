package com.armadialogcreator.gui.main.fxControls;

import com.armadialogcreator.core.ControlClass;
import com.armadialogcreator.gui.fxcontrol.CBMBMenuItem;
import com.armadialogcreator.gui.fxcontrol.ImageContainer;
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
