package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.canvas.UICanvasEditorColors;
import com.armadialogcreator.data.ProjectSettings;
import com.armadialogcreator.data.SettingsManager;
import com.armadialogcreator.gui.StageDialog;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.AColorConstant;
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

	private final ColorPicker cpSelection = new ColorPicker();
	private final ColorPicker cpGrid = new ColorPicker();
	private final ColorPicker cpEditorBg = new ColorPicker();
	private final ColorPicker cpAbsRegion = new ColorPicker();
	private final UICanvasEditorColors colors;

	private ChangeListener<Color> colorChangeListener = new ChangeListener<>() {
		@Override
		public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
			colors.absRegion = cpAbsRegion.getValue();
			colors.editorBg = cpEditorBg.getValue();
			colors.grid = cpGrid.getValue();
			colors.selection = cpSelection.getValue();
			ArmaDialogCreator.getMainWindow().getCanvasEditor().updateCanvas();
		}
	};

	public CanvasViewColorsPopup() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(10), null, false, true, false);

		ResourceBundle bundle = Lang.ApplicationBundle();

		setTitle(bundle.getString("Popups.Colors.popup_title"));

		myStage.initStyle(StageStyle.UTILITY);

		myStage.setMinWidth(400);
		myRootElement.setPadding(new Insets(5, 5, 5, 5));
		myRootElement.setAlignment(Pos.TOP_LEFT);
		myRootElement.getChildren().addAll(
				colorOption(bundle.getString("Popups.Colors.selection"), cpSelection),
				colorOption(bundle.getString("Popups.Colors.abs_region"), cpAbsRegion),
				colorOption(bundle.getString("Popups.Colors.grid"), cpGrid),
				colorOption(bundle.getString("Popups.Colors.background"), cpEditorBg)
		);
		this.colors = ArmaDialogCreator.getMainWindow().getCanvasEditor().getColors();
		setupColorPickers();
	}

	private void setupColorPickers() {
		cpSelection.setValue(colors.selection);
		cpAbsRegion.setValue(colors.absRegion);
		cpEditorBg.setValue(colors.editorBg);
		cpGrid.setValue(colors.grid);

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

	@Override
	protected void ok() {
		ProjectSettings projectSettings = SettingsManager.instance.getProjectSettings();
		projectSettings.EditorBackgroundSetting.set(
				new AColorConstant(cpEditorBg.getValue())
		);
		projectSettings.EditorGridColorSetting.set(
				new AColorConstant(cpGrid.getValue())
		);
		projectSettings.AbsRegionColorSetting.set(
				new AColorConstant(cpAbsRegion.getValue())
		);
		projectSettings.EditorSelectionColorSetting.set(
				new AColorConstant(cpAbsRegion.getValue())
		);
		super.ok();
	}
}
