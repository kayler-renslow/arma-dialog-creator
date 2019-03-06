package com.armadialogcreator;

import com.armadialogcreator.application.ADCDataWriteException;
import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.canvas.UICanvasEditorColors;
import com.armadialogcreator.data.ApplicationProperties;
import com.armadialogcreator.data.ProjectSettings;
import com.armadialogcreator.data.Settings;
import com.armadialogcreator.data.SettingsManager;
import com.armadialogcreator.gui.main.ADCMainWindow;
import com.armadialogcreator.gui.styles.ADCStyleSheets;
import com.armadialogcreator.util.AColorConstant;
import javafx.stage.Stage;

/**
 @author K
 @since 3/6/19 */
public class ADCGuiManager {
	public static final ADCGuiManager instance = new ADCGuiManager();

	void guiReady() {
		applyDarkThemeIfOn();
	}

	public void applyDarkThemeIfOn() {
		Settings.BooleanSetting useDarkTheme = ApplicationProperties.instance.useDarkTheme;
		try {
			ApplicationManager.instance.saveApplicationData();
		} catch (ADCDataWriteException e) {
			//todo report this to error log
			throw new RuntimeException(e);
		}

		final String darkTheme = ADCStyleSheets.getStylesheet("dark.css");
		Stage stage = ArmaDialogCreator.getPrimaryStage();
		ProjectSettings projectSettings = SettingsManager.instance.getProjectSettings();
		if (useDarkTheme.isTrue()) {
			projectSettings.EditorBackgroundSetting.set(
					new AColorConstant(UICanvasEditorColors.DARK_THEME_EDITOR_BG)
			);
			projectSettings.EditorGridColorSetting.set(
					new AColorConstant(UICanvasEditorColors.DARK_THEME_GRID)
			);
			stage.getScene().getStylesheets().add(darkTheme);
		} else {
			stage.getScene().getStylesheets().remove(darkTheme);
			projectSettings.EditorBackgroundSetting.set(
					new AColorConstant(UICanvasEditorColors.DEFAULT_EDITOR_BG)
			);
			projectSettings.EditorGridColorSetting.set(
					new AColorConstant(UICanvasEditorColors.DEFAULT_GRID)
			);
		}

		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		if (mainWindow.isShowing()) {
			mainWindow.getCanvasEditor().updateCanvas();
		}
	}
}
