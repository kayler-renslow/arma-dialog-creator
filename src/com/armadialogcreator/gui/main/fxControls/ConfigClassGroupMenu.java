package com.armadialogcreator.gui.main.fxControls;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.gui.fxcontrol.CBMBGroupMenu;

/**
 @author Kayler
 @since 11/15/2016 */
public class ConfigClassGroupMenu extends CBMBGroupMenu<ConfigClass> {

	public ConfigClassGroupMenu(String groupName, ConfigClassMenuItem... cbmbMenuItems) {
		super(groupName, cbmbMenuItems);
	}
}
