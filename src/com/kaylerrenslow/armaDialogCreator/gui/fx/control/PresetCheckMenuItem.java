package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;

/**
 Created by Kayler on 05/18/2016.
 */
public class PresetCheckMenuItem extends CheckMenuItem {
	public PresetCheckMenuItem(boolean checked) {
		this(null, null, checked);
	}

	public PresetCheckMenuItem(String text, boolean checked) {
		this(text, null, checked);
	}

	public PresetCheckMenuItem(String text, Node graphic, boolean checked) {
		super(text, graphic);
		this.setSelected(checked);
	}
}
