package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.canvas.UICanvasEditorColors;
import com.armadialogcreator.data.ApplicationProperties;
import com.armadialogcreator.data.ProjectSettings;
import com.armadialogcreator.data.Settings;
import com.armadialogcreator.data.SettingsManager;
import com.armadialogcreator.gui.fxcontrol.MenuItemEventHandler;
import com.armadialogcreator.gui.main.ADCMainCanvasEditor;
import com.armadialogcreator.gui.main.ADCMainWindow;
import com.armadialogcreator.gui.styles.ADCStyleSheets;
import com.armadialogcreator.util.AColorConstant;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/24/2016.
 */
public class ViewDarkThemeAction implements MenuItemEventHandler<CheckMenuItem> {

	private final Settings.BooleanSetting useDarkTheme = ApplicationProperties.instance.useDarkTheme;

	public ViewDarkThemeAction() {

	}
	
	@Override
	public void handle(ActionEvent event) {
		CheckMenuItem source = (CheckMenuItem) event.getSource();
		useDarkTheme.not();
		source.setSelected(useDarkTheme.isTrue());

		final String darkTheme = ADCStyleSheets.getStylesheet("dark.css");
		Stage stage = ArmaDialogCreator.getPrimaryStage();
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		ADCMainCanvasEditor canvasEditor = mainWindow.getCanvasEditor();
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
		UICanvasEditorColors colors = canvasEditor.getColors();
		colors.editorBg = projectSettings.EditorBackgroundSetting.get().toJavaFXColor();
		colors.grid = projectSettings.EditorGridColorSetting.get().toJavaFXColor();
		canvasEditor.updateCanvas();
	}

	//	public static void setToDarkTheme(boolean set) {
	//		final String darkTheme = ADCStyleSheets.getStylesheet("dark.css");
	//		if (set) {
	//			UICanvasEditorColors.editorBg = UICanvasEditorColors.DARK_THEME_EDITOR_BG;
	//			UICanvasEditorColors.grid = UICanvasEditorColors.DARK_THEME_GRID;
	//			INSTANCE.primaryStage.getScene().getStylesheets().add(darkTheme);
	//		} else {
	//			UICanvasEditorColors.editorBg = UICanvasEditorColors.DEFAULT_EDITOR_BG;
	//			UICanvasEditorColors.grid = UICanvasEditorColors.DEFAULT_GRID;
	//			INSTANCE.primaryStage.getScene().getStylesheets().remove(darkTheme);
	//		}
	//		if (getADCWindow().isShowing()) {
	//			getCanvasEditor().updateCanvas();
	//		}
	//		getApplicationDataManager().getApplicationProperties().put(ApplicationProperty.DARK_THEME, set);
	//		getApplicationDataManager().saveApplicationProperties();
	//	}
	@Override
	public void setMenuItem(@NotNull CheckMenuItem menuItem) {
		menuItem.setSelected(useDarkTheme.isTrue());
	}
}
