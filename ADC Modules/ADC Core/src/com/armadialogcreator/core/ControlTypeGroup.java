package com.armadialogcreator.core;

import com.armadialogcreator.lang.Lang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/21/2016 */
public enum ControlTypeGroup {
	TEXT(getString("ControlType.TypeGroup.text")), BUTTON(getString("ControlType.TypeGroup.button")), COMBO(getString("ControlType.TypeGroup.combo")), SLIDER(getString("ControlType.TypeGroup.slider")),
	LIST_BOX(getString("ControlType.TypeGroup.list_box")), CHECK_BOX(getString("ControlType.TypeGroup.check_box")), MENU(getString("ControlType.TypeGroup.menu")), OBJECT(getString("ControlType.TypeGroup.object")),
	MAP(getString("ControlType.TypeGroup.map")), MISC(getString("ControlType.TypeGroup.misc"));

	private final String displayName;

	ControlTypeGroup(String displayName) {
		this.displayName = displayName;
	}

	@NotNull
	public String getDisplayName() {
		return displayName;
	}


	private static String getString(String s) {
		return Lang.LookupBundle().getString(s);
	}
}
