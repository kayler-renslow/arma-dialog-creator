package com.armadialogcreator.gui.main.actions.mainMenu.view;

import com.armadialogcreator.ADCGuiManager;
import com.armadialogcreator.data.ApplicationProperties;
import com.armadialogcreator.data.Settings;
import com.armadialogcreator.gui.fxcontrol.MenuItemEventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckMenuItem;
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
		useDarkTheme.not();
		ADCGuiManager.instance.applyDarkThemeIfOn();
	}



	@Override
	public void setMenuItem(@NotNull CheckMenuItem menuItem) {
		menuItem.setSelected(useDarkTheme.isTrue());
	}
}
