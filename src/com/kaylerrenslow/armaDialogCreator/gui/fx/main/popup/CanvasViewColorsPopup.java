/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

/**
 Creates a popup for changing the canvas colors

 @author Kayler
 @since 05/20/2016. */
public class CanvasViewColorsPopup extends StageDialog<VBox> {

	private final ColorPicker cpSelection = new ColorPicker(CanvasViewColors.SELECTION);
	private final ColorPicker cpGrid = new ColorPicker(CanvasViewColors.GRID);
	private final ColorPicker cpEditorBg = new ColorPicker(CanvasViewColors.EDITOR_BG);
	private final ColorPicker cpAbsRegion = new ColorPicker(CanvasViewColors.ABS_REGION);

	private ChangeListener<Color> colorChangeListener = new ChangeListener<Color>() {
		@Override
		public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
			CanvasViewColors.ABS_REGION = cpAbsRegion.getValue();
			CanvasViewColors.EDITOR_BG = cpEditorBg.getValue();
			CanvasViewColors.GRID = cpGrid.getValue();
			CanvasViewColors.SELECTION = cpSelection.getValue();
			ArmaDialogCreator.getCanvasView().updateCanvas();
		}
	};

	public CanvasViewColorsPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), Lang.ApplicationBundle().getString("Popups.Colors.popup_title"), false, true, false);
		myStage.initStyle(StageStyle.UTILITY);
		setupColorPickers();
		myStage.setMinWidth(400);
		myRootElement.setPadding(new Insets(5, 5, 5, 5));
		myRootElement.setAlignment(Pos.TOP_LEFT);
		myRootElement.getChildren().addAll(
				colorOption(Lang.ApplicationBundle().getString("Popups.Colors.selection"), cpSelection),
				colorOption(Lang.ApplicationBundle().getString("Popups.Colors.abs_region"), cpAbsRegion),
				colorOption(Lang.ApplicationBundle().getString("Popups.Colors.grid"), cpGrid),
				colorOption(Lang.ApplicationBundle().getString("Popups.Colors.background"), cpEditorBg)
		);
	}

	private void setupColorPickers() {
		cpSelection.valueProperty().addListener(colorChangeListener);
		cpAbsRegion.valueProperty().addListener(colorChangeListener);
		cpEditorBg.valueProperty().addListener(colorChangeListener);
		cpGrid.valueProperty().addListener(colorChangeListener);
	}

	private static HBox colorOption(String label, ColorPicker cp) {
		HBox hb = new HBox(10);
		hb.getChildren().addAll(cp, new Label(label));
		return hb;
	}

}
