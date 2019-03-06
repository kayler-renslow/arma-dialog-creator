package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.data.ApplicationProperties;
import com.armadialogcreator.data.Settings;
import com.armadialogcreator.gui.fxcontrol.MenuItemEventHandler;
import com.armadialogcreator.gui.styles.ADCStyleSheets;
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
		if (useDarkTheme.isTrue()) {
			stage.getScene().getStylesheets().add(darkTheme);
		} else {
			stage.getScene().getStylesheets().remove(darkTheme);
		}
	}

	//	public static void setToDarkTheme(boolean set) {
	//		final String darkTheme = ADCStyleSheets.getStylesheet("dark.css");
	//		if (set) {
	//			CanvasViewColors.EDITOR_BG = CanvasViewColors.DARK_THEME_EDITOR_BG;
	//			CanvasViewColors.GRID = CanvasViewColors.DARK_THEME_GRID;
	//			INSTANCE.primaryStage.getScene().getStylesheets().add(darkTheme);
	//		} else {
	//			CanvasViewColors.EDITOR_BG = CanvasViewColors.DEFAULT_EDITOR_BG;
	//			CanvasViewColors.GRID = CanvasViewColors.DEFAULT_GRID;
	//			INSTANCE.primaryStage.getScene().getStylesheets().remove(darkTheme);
	//		}
	//		if (getADCWindow().isShowing()) {
	//			getCanvasView().updateCanvas();
	//		}
	//		getApplicationDataManager().getApplicationProperties().put(ApplicationProperty.DARK_THEME, set);
	//		getApplicationDataManager().saveApplicationProperties();
	//	}
	@Override
	public void setMenuItem(@NotNull CheckMenuItem menuItem) {
		menuItem.setSelected(useDarkTheme.isTrue());
	}
}
