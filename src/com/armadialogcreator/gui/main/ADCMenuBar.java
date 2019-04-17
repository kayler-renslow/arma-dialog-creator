package com.armadialogcreator.gui.main;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.ProgramArgument;
import com.armadialogcreator.canvas.UIScale;
import com.armadialogcreator.control.ArmaUIScale;
import com.armadialogcreator.core.stringtable.KnownLanguage;
import com.armadialogcreator.core.stringtable.Language;
import com.armadialogcreator.data.SettingsManager;
import com.armadialogcreator.gui.fxcontrol.PresetCheckMenuItem;
import com.armadialogcreator.gui.main.actions.mainMenu.EditStringTableAction;
import com.armadialogcreator.gui.main.actions.mainMenu.SettingsChangeSaveDirAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateMacroAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateNewControlAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateNewCustomControlAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateNewFolderAction;
import com.armadialogcreator.gui.main.actions.mainMenu.devmenu.*;
import com.armadialogcreator.gui.main.actions.mainMenu.edit.*;
import com.armadialogcreator.gui.main.actions.mainMenu.file.*;
import com.armadialogcreator.gui.main.actions.mainMenu.help.CheckForUpdateAction;
import com.armadialogcreator.gui.main.actions.mainMenu.help.WikiUrlAction;
import com.armadialogcreator.gui.main.actions.mainMenu.view.*;
import com.armadialogcreator.gui.main.popup.AboutDialog;
import com.armadialogcreator.gui.main.popup.ExpressionEvaluatorPopup;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;

import static com.armadialogcreator.gui.FXUtil.addOnAction;

/**
 Created by Kayler on 05/15/2016.
 */
class ADCMenuBar extends MenuBar {

	final ResourceBundle bundle = Lang.getBundle("MainMenuBarBundle");

	/*File*/
	final MenuItem file_open = addOnAction(new MenuItem(bundle.getString("file_open")), new FileOpenAction());
	final MenuItem file_save = addOnAction(new MenuItem(bundle.getString("file_save"), new ImageView(ADCIcons.ICON_SAVE)), new FileSaveAction());
	final MenuItem file_export = addOnAction(new MenuItem(bundle.getString("file_export"), new ImageView(ADCIcons.ICON_EXPORT)), new FileExportAction());
	final MenuItem file_closeProject = addOnAction(new MenuItem(bundle.getString("file_close_project")), new FileCloseProjectAction());
	final MenuItem file_exit = addOnAction(new MenuItem(bundle.getString("file_exit")), new FileExitAction());
	final MenuItem file_restart = addOnAction(new MenuItem(bundle.getString("file_restart")), new FileRestartAction());


	/*Edit*/
	final MenuItem edit_undo = new MenuItem(bundle.getString("edit_undo"), new ImageView(ADCIcons.ICON_UNDO));
	final MenuItem edit_redo = new MenuItem(bundle.getString("edit_redo"), new ImageView(ADCIcons.ICON_REDO));

	{
		edit_redo.setOnAction(new EditRedoAction(edit_redo));
		edit_undo.setOnAction(new EditUndoAction(edit_undo));
	}

	final MenuItem edit_viewChanges = addOnAction(new MenuItem(bundle.getString("edit_changes")), new EditViewChangesAction());
	final MenuItem edit_macros = addOnAction(new MenuItem(bundle.getString("edit_macros")), new EditMacrosAction());
	final MenuItem edit_custom_control = addOnAction(new MenuItem(bundle.getString("edit_custom_control")), new EditCustomControlAction());
	final MenuItem edit_string_table = addOnAction(new MenuItem(bundle.getString("edit_string_table")), new EditStringTableAction());
	final MenuItem edit_exportConfig = addOnAction(new MenuItem(bundle.getString("edit_export_config")), new EditExportConfigAction());
	final MenuItem edit_projectSettings = addOnAction(new MenuItem(bundle.getString("edit_project_settings")), new EditProjectSettingsAction());

	/*View*/
	final MenuItem view_preview = addOnAction(new MenuItem(bundle.getString("view_preview"), null), new ViewPreviewAction());
	final CheckMenuItem view_showGrid = addOnAction(new CheckMenuItem(bundle.getString("view_show_grid")), new ViewShowGridAction());

	final CheckMenuItem view_darkTheme = addOnAction(new CheckMenuItem(bundle.getString("view_dark_theme")), new ViewDarkThemeAction());


	final MenuItem view_colors = addOnAction(new MenuItem(bundle.getString("view_change_colors")), new ViewColorsAction());
	final MenuItem view_fullScreen = addOnAction(new MenuItem(bundle.getString("view_full_screen")), new ViewFullScreenAction());
	/*screen*/
	final ChoiceBox<UIScale> choiceBoxUiScale = new ChoiceBox<>();
	final Label lblUiScale = new Label(bundle.getString("view_ui_scale"), choiceBoxUiScale);

	{
		lblUiScale.setContentDisplay(ContentDisplay.RIGHT);
		choiceBoxUiScale.getItems().addAll(ArmaUIScale.values());
		choiceBoxUiScale.getSelectionModel().select(ArmaUIScale.DEFAULT);
		choiceBoxUiScale.getSelectionModel().selectedItemProperty().addListener(new ViewScreenUIScaleChangeAction());
	}

	final CustomMenuItem view_uiScale = new CustomMenuItem(lblUiScale, false);

	final ChoiceBox<Language> choiceBoxUiLanguage = new ChoiceBox<>();
	final Label lblUiLanguage = new Label(bundle.getString("view_ui_language"), choiceBoxUiLanguage);

	{
		lblUiLanguage.setContentDisplay(ContentDisplay.RIGHT);
		choiceBoxUiLanguage.getItems().addAll(KnownLanguage.values());
		choiceBoxUiLanguage.getSelectionModel().selectedItemProperty().addListener(new ViewScreenUILanguageChangeAction());
		choiceBoxUiLanguage.getSelectionModel().select(KnownLanguage.Original);
	}

	final CustomMenuItem view_uiLanguage = new CustomMenuItem(lblUiLanguage, false);

	final Menu view_ui = new Menu(bundle.getString("view_ui"), null, view_uiScale, view_uiLanguage);
	/*abs region*/
	final PresetCheckMenuItem view_absRegion_show = addOnAction(new PresetCheckMenuItem(bundle.getString("view_abs_region_show"), true), new ViewAbsRegionShowAction());
	final PresetCheckMenuItem view_absRegion_alwaysFront = addOnAction(new PresetCheckMenuItem(bundle.getString("view_abs_region_always_front"), true), new ViewAbsRegionAlwaysFrontAction());
	final Menu view_absRegionAll = new Menu(bundle.getString("view_abs_region"), null, view_absRegion_show, view_absRegion_alwaysFront);
	/*background*/
	final RadioMenuItem view_bg_img1 = addOnAction(new RadioMenuItem(bundle.getString("view_change_background_image1")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_1));
	final RadioMenuItem view_bg_img2 = addOnAction(new RadioMenuItem(bundle.getString("view_change_background_image2")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_2));
	final RadioMenuItem view_bg_img3 = addOnAction(new RadioMenuItem(bundle.getString("view_change_background_image3")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_3));
	final RadioMenuItem view_bg_custom = addOnAction(new RadioMenuItem(bundle.getString("view_change_background_image_custom")), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_CUSTOM));
	final RadioMenuItem view_bg_noImage = addOnAction(new RadioMenuItem(bundle.getString("view_change_background_none")), new ViewBackgroundAction(ViewBackgroundAction.NO_IMAGE));

	{
		final ToggleGroup view_bg_toggleGroup = new ToggleGroup();
		view_bg_img1.setToggleGroup(view_bg_toggleGroup);
		view_bg_img2.setToggleGroup(view_bg_toggleGroup);
		view_bg_img3.setToggleGroup(view_bg_toggleGroup);
		view_bg_custom.setToggleGroup(view_bg_toggleGroup);
		view_bg_noImage.setToggleGroup(view_bg_toggleGroup);
		view_bg_toggleGroup.selectToggle(view_bg_noImage);
	}

	final Menu backgroundAll = new Menu(
			bundle.getString("view_background_image"), null,
			view_bg_img1,
			view_bg_img2,
			view_bg_img3,
			view_bg_custom,
			view_bg_noImage
	);

	/*settings*/
	final MenuItem settings_configureDirs = addOnAction(
			new MenuItem(bundle.getString("settings_configure_dirs")), new SettingsChangeSaveDirAction()
	);

	/*create*/
	final MenuItem create_macro = addOnAction(new MenuItem(bundle.getString("create_macro")), new CreateMacroAction());

	{
		create_macro.setGraphic(new ImageView(ADCIcons.ICON_HASH_MINIPLUS));
	}

	final MenuItem create_control_class = addOnAction(new MenuItem(bundle.getString("create_control_class")), new CreateNewCustomControlAction());
	final MenuItem create_new_control = addOnAction(new MenuItem(bundle.getString("create_control")), new CreateNewControlAction());
	final MenuItem create_new_folder = addOnAction(
			new MenuItem(bundle.getString("create_folder")),
			new CreateNewFolderAction()
	);

	{
		create_new_folder.setGraphic(new ImageView(ADCIcons.ICON_FOLDER_MINIPLUS));
	}

	/*help*/
	final MenuItem help_wiki = addOnAction(new MenuItem(bundle.getString("help_wiki")), new WikiUrlAction());
	final MenuItem help_about = addOnAction(new MenuItem(bundle.getString("help_about")), event -> new AboutDialog().show());
	final MenuItem help_evaluator = addOnAction(new MenuItem(bundle.getString("help_evaluator")), event -> new ExpressionEvaluatorPopup().show());
	final MenuItem help_checkForUpdate = addOnAction(new MenuItem(bundle.getString("help_check_for_update")), new CheckForUpdateAction());

	final Menu menuFile = new Menu(
			bundle.getString("file"), null,
			/*file_open,*/
			file_save,
			file_export,
			new SeparatorMenuItem(),
			file_closeProject,
			file_restart,
			file_exit
	);
	final Menu menuEdit = new Menu(
			bundle.getString("edit"), null,
			edit_undo,
			edit_redo,
			edit_viewChanges,
			new SeparatorMenuItem(),
			edit_macros,
			edit_custom_control,
			edit_string_table,
			edit_exportConfig,
			edit_projectSettings
	);
	final Menu menuView = new Menu(
			bundle.getString("view"), null,
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
			bundle.getString("settings"), null,
			settings_configureDirs
	);
	final Menu menuCreate = new Menu(
			bundle.getString("create"), null,
			create_macro,
			create_control_class,
			create_new_control,
			create_new_folder
	);
	final Menu menuHelp = new Menu(
			bundle.getString("help"), null,
			help_evaluator,
			help_wiki,
			help_about,
			help_checkForUpdate
	);

	/*dev menu*/
	final MenuItem dev_registries = addOnAction(new MenuItem("Show Registries"), new ShowRegistries());
	final MenuItem dev_showDisplay = addOnAction(new MenuItem("Show Display"), new ShowDisplay());
	final MenuItem dev_showXml = addOnAction(new MenuItem("Show Xml As Configurable"), new ShowXmlAsConfigurable());
	final MenuItem dev_openClassicProjectSave = addOnAction(new MenuItem("Open Classic Project Save"), new OpenClassicProjectSave());
	final MenuItem dev_syncTreeView = addOnAction(new MenuItem("Sync Control Tree Views"), new SyncControlTreeViews());
	final MenuItem dev_openWorkspaceSave = addOnAction(new MenuItem("Open Workspace Save"), new OpenClassicWorkspaceSave());
	final Menu menuDev = new Menu("Dev", null,
			dev_registries,
			dev_showDisplay,
			dev_showXml,
			new Menu("Classic Saves", null,
					dev_openClassicProjectSave,
					dev_openWorkspaceSave
			),
			dev_syncTreeView
	);

	ADCMenuBar() {
		this.getMenus().addAll(menuFile, menuEdit, menuView, menuSettings, menuCreate, menuHelp);

		view_showGrid.getParentMenu().showingProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				view_showGrid.setSelected(SettingsManager.instance.getProjectSettings().ShowCanvasGridSetting.isTrue());
			}
		});

		if (ArmaDialogCreator.containsUnnamedLaunchParameter(ProgramArgument.DevMode)) {
			getMenus().add(menuDev);
		}

	}


}
