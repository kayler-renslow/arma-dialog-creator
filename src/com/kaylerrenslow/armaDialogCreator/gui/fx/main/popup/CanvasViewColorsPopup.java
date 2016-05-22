package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.Label;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.CanvasViewColors;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.kaylerrenslow.armaDialogCreator.main.Lang.ColorsPopup;

/**
 @author Kayler
 Creates a popup for changing the canvas colors
 Created on 05/20/2016. */
public class CanvasViewColorsPopup extends StagePopup {

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

	public CanvasViewColorsPopup(Stage primaryStage) {
		super(primaryStage, new VBox(10), Lang.ColorsPopup.POPUP_TITLE);
		myStage.initModality(Modality.APPLICATION_MODAL);
		setupColorPickers();
		VBox root = (VBox) myRootElement;
		myStage.setMinWidth(400);
		root.setPadding(new Insets(5, 5, 5, 5));
		root.setAlignment(Pos.TOP_LEFT);
		root.getChildren().addAll(colorOption(ColorsPopup.SELECTION, cpSelection), colorOption(ColorsPopup.ABS_REGION, cpAbsRegion), colorOption(ColorsPopup.GRID, cpGrid), colorOption(ColorsPopup.BACKGROUND, cpEditorBg));
	}

	private void setupColorPickers() {
		cpSelection.valueProperty().addListener(colorChangeListener);
		cpAbsRegion.valueProperty().addListener(colorChangeListener);
		cpEditorBg.valueProperty().addListener(colorChangeListener);
		cpGrid.valueProperty().addListener(colorChangeListener);
	}

	private static HBox colorOption(String label, ColorPicker cp) {
		HBox hb = new HBox(10);
		hb.setAlignment(Pos.TOP_LEFT);
		hb.getChildren().addAll(cp, new Label(label));
		return hb;
	}

}
