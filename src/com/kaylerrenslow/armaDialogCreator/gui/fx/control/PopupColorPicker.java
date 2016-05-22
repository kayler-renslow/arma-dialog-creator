package com.kaylerrenslow.armaDialogCreator.gui.fx.control;

import javafx.scene.control.ColorPicker;
import javafx.stage.Popup;

/**
 Created by Kayler on 05/18/2016.
 */
public class PopupColorPicker extends Popup {
	private ColorPicker picker;

	public PopupColorPicker(ColorPicker colorPicker) {
		this.picker = colorPicker;
		getContent().addAll(picker);
	}

	public ColorPicker getPicker() {
		return picker;
	}


}
