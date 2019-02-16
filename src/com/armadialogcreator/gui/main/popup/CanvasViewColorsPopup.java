package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.canvas.CanvasViewColors;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.lang.Lang;
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

import java.util.ResourceBundle;

/**
 Creates a popup for changing the canvas colors

 @author Kayler
 @since 05/20/2016. */
public class CanvasViewColorsPopup extends StageDialog<VBox> {

	private final ColorPicker cpSelection = new ColorPicker(CanvasViewColors.SELECTION);
	private final ColorPicker cpGrid = new ColorPicker(CanvasViewColors.GRID);
	private final ColorPicker cpEditorBg = new ColorPicker(CanvasViewColors.EDITOR_BG);
	private final ColorPicker cpAbsRegion = new ColorPicker(CanvasViewColors.ABS_REGION);

	private ChangeListener<Color> colorChangeListener = new ChangeListener<>() {
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
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, false, true, false);

		ResourceBundle bundle = Lang.ApplicationBundle();

		setTitle(bundle.getString("Popups.Colors.popup_title"));

		myStage.initStyle(StageStyle.UTILITY);
		setupColorPickers();
		myStage.setMinWidth(400);
		myRootElement.setPadding(new Insets(5, 5, 5, 5));
		myRootElement.setAlignment(Pos.TOP_LEFT);
		myRootElement.getChildren().addAll(
				colorOption(bundle.getString("Popups.Colors.selection"), cpSelection),
				colorOption(bundle.getString("Popups.Colors.abs_region"), cpAbsRegion),
				colorOption(bundle.getString("Popups.Colors.grid"), cpGrid),
				colorOption(bundle.getString("Popups.Colors.background"), cpEditorBg)
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
