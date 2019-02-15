package com.armadialogcreator.gui.main.fxControls;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.gui.fxcontrol.CBMBMenuItem;
import com.armadialogcreator.gui.fxcontrol.ImageContainer;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 11/15/2016 */
public class ConfigClassMenuItem extends CBMBMenuItem<ConfigClass> {

	public ConfigClassMenuItem(ConfigClass value, @Nullable ImageContainer graphic) {
		super(value, graphic);
	}

	public ConfigClassMenuItem(ConfigClass value) {
		super(value);
	}
}
