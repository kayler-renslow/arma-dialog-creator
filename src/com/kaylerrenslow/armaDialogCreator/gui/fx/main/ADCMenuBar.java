/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaUIScale;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationDataManager;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationProperty;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.UIScale;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.PresetCheckMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.SettingsChangeSaveDirAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.TestAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create.CreateMacroAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create.CreateNewControlAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create.CreateNewCustomControlAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.edit.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.help.WikiUrlAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view.*;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.AboutDialog;
import com.kaylerrenslow.armaDialogCreator.gui.img.Images;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ProgramArgument;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import static com.kaylerrenslow.armaDialogCreator.gui.fx.control.MenuUtil.addOnAction;
import static com.kaylerrenslow.armaDialogCreator.main.Lang.ApplicationBundle;

/**
 Created by Kayler on 05/15/2016.
 */
class ADCMenuBar extends MenuBar {

	/*File*/
	final MenuItem file_open = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.file_open")), new FileOpenAction());
	final MenuItem file_save = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.file_save")), new FileSaveAction());
	final MenuItem file_export = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.file_export")), new FileExportAction());
	final MenuItem file_closeProject = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.file_close_project")), new FileCloseProjectAction());
	final MenuItem file_exit = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.file_exit")), new FileExitAction());
	final MenuItem file_restart = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.file_restart")), new FileRestartAction());


	/*Edit*/
	final MenuItem edit_undo = new MenuItem(ApplicationBundle().getString("MainMenuBar.edit_undo"), new ImageView(Images.ICON_UNDO));
	final MenuItem edit_redo = new MenuItem(ApplicationBundle().getString("MainMenuBar.edit_redo"), new ImageView(Images.ICON_REDO));
	{
		edit_redo.setOnAction(new EditRedoAction(edit_redo));
		edit_undo.setOnAction(new EditUndoAction(edit_undo));
	}

	final MenuItem edit_viewChanges = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.edit_changes")), new EditViewChangesAction());
	final MenuItem edit_macros = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.edit_macros")), new EditMacrosAction());
	final MenuItem edit_custom_control = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.edit_custom_control")), new EditCustomControlAction());
	final MenuItem edit_exportConfig = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.edit_export_config")), new EditExportConfigAction());


	/*View*/
	final MenuItem view_preview = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.view_preview"), null), new ViewPreviewAction());
	final CheckMenuItem view_showGrid = addOnAction(new CheckMenuItem(ApplicationBundle().getString("MainMenuBar.view_show_grid")), new ViewShowGridAction());

	final CheckMenuItem view_darkTheme = addOnAction(new CheckMenuItem(ApplicationBundle().getString("MainMenuBar.view_dark_theme")), new
			ViewDarkThemeAction(ApplicationProperty.DARK_THEME.get(ApplicationDataManager.getApplicationProperties())));
	{
		view_darkTheme.setSelected(ApplicationProperty.DARK_THEME.get(ApplicationDataManager.getApplicationProperties()));
	}

	final MenuItem view_colors = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.view_change_colors")), new ViewColorsAction());
	final MenuItem view_fullScreen = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.view_full_screen")), new ViewFullScreenAction());
	/*screen*/
	final ChoiceBox<UIScale> choiceBoxUiScale = new ChoiceBox<>();

	{
		choiceBoxUiScale.getItems().addAll(ArmaUIScale.values());
		choiceBoxUiScale.getSelectionModel().select(ArmaUIScale.DEFAULT);
		choiceBoxUiScale.getSelectionModel().selectedItemProperty().addListener(new ViewScreenUIScaleChangeAction());
	}

	final CustomMenuItem view_uiScale = new CustomMenuItem(choiceBoxUiScale, false);

	final Menu view_ui = new Menu(ApplicationBundle().getString("MainMenuBar.view_ui"), null, view_uiScale);
	/*abs region*/
	final PresetCheckMenuItem view_absRegion_show = addOnAction(new PresetCheckMenuItem(ApplicationBundle().getString("MainMenuBar.view_abs_region_show"), true), new ViewAbsRegionShowAction());
	final PresetCheckMenuItem view_absRegion_alwaysFront = addOnAction(new PresetCheckMenuItem(ApplicationBundle().getString("MainMenuBar.view_abs_region_always_front"), true), new ViewAbsRegionAlwaysFrontAction());
	final Menu view_absRegionAll = new Menu(ApplicationBundle().getString("MainMenuBar.view_abs_region"), null, view_absRegion_show, view_absRegion_alwaysFront);
	/*background*/
	final RadioMenuItem view_bg_img1 = addOnAction(new RadioMenuItem(ApplicationBundle().getString("MainMenuBar.view_change_background_image1")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_1));
	final RadioMenuItem view_bg_img2 = addOnAction(new RadioMenuItem(ApplicationBundle().getString("MainMenuBar.view_change_background_image2")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_2));
	final RadioMenuItem view_bg_img3 = addOnAction(new RadioMenuItem(ApplicationBundle().getString("MainMenuBar.view_change_background_image3")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_3));
	final RadioMenuItem view_bg_custom = addOnAction(new RadioMenuItem(ApplicationBundle().getString("MainMenuBar.view_change_background_image_custom")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_CUSTOM));
	final RadioMenuItem view_bg_noImage = addOnAction(new RadioMenuItem(ApplicationBundle().getString("MainMenuBar.view_change_background_none")), new ViewBackgroundAction(ViewBackgroundAction.NO_IMAGE));

	{
		final ToggleGroup view_bg_toggleGroup = new ToggleGroup();
		view_bg_img1.setToggleGroup(view_bg_toggleGroup);
		view_bg_img2.setToggleGroup(view_bg_toggleGroup);
		view_bg_img3.setToggleGroup(view_bg_toggleGroup);
		view_bg_custom.setToggleGroup(view_bg_toggleGroup);
		view_bg_noImage.setToggleGroup(view_bg_toggleGroup);
		view_bg_toggleGroup.selectToggle(view_bg_noImage);
	}

	final Menu backgroundAll = new Menu(ApplicationBundle().getString("MainMenuBar.view_background_image"), null, view_bg_img1, view_bg_img2, view_bg_img3, view_bg_custom, view_bg_noImage);

	/*settings*/
	final MenuItem settings_configureDirs = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.settings_configure_dirs")), new SettingsChangeSaveDirAction());

	/*create*/
	final MenuItem create_macro = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.create_macro")), new CreateMacroAction());
	final MenuItem create_control_class = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.create_control_class")), new CreateNewCustomControlAction());
	final MenuItem create_new_control = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.create_control")), new CreateNewControlAction());

	/*help*/
	final MenuItem help_wiki = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.help_wiki")), new WikiUrlAction());
	final MenuItem help_about = addOnAction(new MenuItem(ApplicationBundle().getString("MainMenuBar.help_about")), event -> new AboutDialog().show());

	final Menu menuFile = new Menu(
			ApplicationBundle().getString("MainMenuBar.file"), null,
			file_open,
			file_save,
			file_export,
			new SeparatorMenuItem(),
			file_closeProject,
			file_restart,
			file_exit
	);
	final Menu menuEdit = new Menu(
			ApplicationBundle().getString("MainMenuBar.edit"), null,
			edit_undo,
			edit_redo,
			edit_viewChanges,
			new SeparatorMenuItem(),
			edit_macros,
			edit_custom_control/*,
			edit_exportConfig*/ //todo
	);
	final Menu menuView = new Menu(
			ApplicationBundle().getString("MainMenuBar.view"), null,
			view_preview,
			view_showGrid,
			view_colors,
			view_darkTheme,
			backgroundAll,
			view_absRegionAll,
			view_ui,
			view_fullScreen
	);
	final Menu menuSettings = new Menu(
			ApplicationBundle().getString("MainMenuBar.settings"), null,
			settings_configureDirs
	);
	final Menu menuCreate = new Menu(
			ApplicationBundle().getString("MainMenuBar.create"), null,
			create_macro,
			create_control_class,
			create_new_control
	);
	final Menu menuHelp = new Menu(
			ApplicationBundle().getString("MainMenuBar.help"), null,
			help_wiki,
			help_about
	);

	ADCMenuBar() {
		this.getMenus().addAll(menuFile, menuEdit, menuView, menuSettings, menuCreate, menuHelp);
		if (ArmaDialogCreator.containsUnamedLaunchParameter(ProgramArgument.ShowDebugFeatures)) {
			this.getMenus().add(new Menu("Test", null, addOnAction(new MenuItem("Test"), new TestAction())));
		}

		view_showGrid.getParentMenu().showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				view_showGrid.setSelected(ArmaDialogCreator.getCanvasView().getConfiguration().showGrid());
			}
		});
	}

}
