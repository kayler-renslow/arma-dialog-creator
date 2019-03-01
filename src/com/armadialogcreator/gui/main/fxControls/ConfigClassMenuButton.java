package com.armadialogcreator.gui.main.fxControls;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.gui.fxcontrol.ComboBoxMenuButton;
import javafx.scene.Node;

/**
 Created by Kayler on 10/19/2016.
 */
public class ConfigClassMenuButton extends ComboBoxMenuButton<ConfigClass> {

	public ConfigClassMenuButton(boolean allowClear, String placeholderText, Node placeholderGraphic) {
		super(allowClear, placeholderText, placeholderGraphic);
	}

	public ConfigClassMenuButton(boolean allowClear, String placeholderText, Node placeholderGraphic, ConfigClassGroupMenu... classGroups) {
		super(allowClear, placeholderText, placeholderGraphic, classGroups);
	}

	public ConfigClassMenuButton(boolean allowClear, String placeholderText, Node placeholderGraphic, ConfigClassMenuItem... items) {
		super(allowClear, placeholderText, placeholderGraphic, items);
	}
}
