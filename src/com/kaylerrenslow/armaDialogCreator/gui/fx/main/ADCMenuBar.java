package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.gui.fx.control.PopupColorPicker;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.PresetCheckMenuItem;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.edit.EditRedoAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.edit.EditUndoAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.edit.EditViewChangesAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file.FileNewAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file.FileOpenAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file.FileSaveAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.file.FileSaveAsAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view.*;
import com.kaylerrenslow.armaDialogCreator.main.Lang.MainMenuBar;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.*;

import static com.kaylerrenslow.armaDialogCreator.gui.fx.control.MenuUtil.addOnAction;

/**
 Created by Kayler on 05/15/2016.
 */
class ADCMenuBar extends MenuBar {

	/*File*/
	private final MenuItem file_new = addOnAction(new MenuItem(MainMenuBar.FILE_NEW), new FileNewAction());
	private final MenuItem file_open = addOnAction(new MenuItem(MainMenuBar.FILE_OPEN), new FileOpenAction());
	private final MenuItem file_save = addOnAction(new MenuItem(MainMenuBar.FILE_SAVE), new FileSaveAction());
	private final MenuItem file_saveAs = addOnAction(new MenuItem(MainMenuBar.FILE_SAVE_AS), new FileSaveAsAction());

	/*Edit*/
	private final MenuItem edit_viewChanges = addOnAction(new MenuItem(MainMenuBar.EDIT_CHANGES), new EditViewChangesAction());
	private final MenuItem edit_undo = addOnAction(new MenuItem(MainMenuBar.EDIT_UNDO), new EditUndoAction());
	private final MenuItem edit_redo = addOnAction(new MenuItem(MainMenuBar.EDIT_REDO), new EditRedoAction());

	/*View*/
	private final PresetCheckMenuItem view_showGrid = (PresetCheckMenuItem) addOnAction(new PresetCheckMenuItem(MainMenuBar.VIEW_SHOW_GRID, true), new ViewShowGridAction());
	private final MenuItem view_colors = addOnAction(new MenuItem(MainMenuBar.VIEW_COLORS), new ViewColorsAction());
	/*abs region*/
	private final PresetCheckMenuItem view_absRegion_show = (PresetCheckMenuItem) addOnAction(new PresetCheckMenuItem(MainMenuBar.VIEW_ABS_REGION_SHOW, true), new ViewAbsRegionShowAction());
	private final PresetCheckMenuItem view_absRegion_alwaysFront = (PresetCheckMenuItem) addOnAction(new PresetCheckMenuItem(MainMenuBar.VIEW_ABS_REGION_ALWAYS_FRONT, true), new ViewAbsRegionAlwaysFrontAction());
	private final Menu view_absRegionAll = new Menu(MainMenuBar.VIEW_ABS_REGION, null, view_absRegion_show, view_absRegion_alwaysFront);
	/*background*/
	private final RadioMenuItem view_bg_img1 = (RadioMenuItem) addOnAction(new RadioMenuItem(MainMenuBar.VIEW_CHANGE_BACKGROUND_IMAGE1), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_1));
	private final RadioMenuItem view_bg_img2 = (RadioMenuItem) addOnAction(new RadioMenuItem(MainMenuBar.VIEW_CHANGE_BACKGROUND_IMAGE2), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_2));
	private final RadioMenuItem view_bg_img3 = (RadioMenuItem) addOnAction(new RadioMenuItem(MainMenuBar.VIEW_CHANGE_BACKGROUND_IMAGE3), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_3));
	private final RadioMenuItem view_bg_custom = (RadioMenuItem) addOnAction(new RadioMenuItem(MainMenuBar.VIEW_CHANGE_BACKGROUND_IMAGE_CUSTOM), new ViewBackgroundAction(ViewBackgroundAction.IMAGE_CUSTOM));
	private final RadioMenuItem view_bg_noImage = (RadioMenuItem) addOnAction(new RadioMenuItem(MainMenuBar.VIEW_CHANGE_BACKGROUND_NONE), new ViewBackgroundAction(ViewBackgroundAction.NO_IMAGE));
	private final ToggleGroup view_bg_toggleGroup = new ToggleGroup();
	{
		view_bg_img1.setToggleGroup(view_bg_toggleGroup);
		view_bg_img2.setToggleGroup(view_bg_toggleGroup);
		view_bg_img3.setToggleGroup(view_bg_toggleGroup);
		view_bg_custom.setToggleGroup(view_bg_toggleGroup);
		view_bg_noImage.setToggleGroup(view_bg_toggleGroup);
		view_bg_toggleGroup.selectToggle(view_bg_noImage);
	}

	private final Menu backgroundAll = new Menu(MainMenuBar.VIEW_BACKGROUND_IMAGE, null, view_bg_img1, view_bg_img2, view_bg_img3, view_bg_custom, view_bg_noImage);

	private final Menu menuFile = new Menu(MainMenuBar.FILE, null, file_new, file_open, file_save, file_saveAs);
	private final Menu menuEdit = new Menu(MainMenuBar.EDIT, null, edit_viewChanges, edit_undo, edit_redo);
	private final Menu menuView = new Menu(MainMenuBar.VIEW, null, view_showGrid, view_colors, view_absRegionAll, backgroundAll);

	ADCMenuBar() {
		this.getMenus().addAll(menuFile, menuEdit, menuView);
	}

	private class ColorPickerButtonEvent implements EventHandler<ActionEvent> {

		private final ColorPicker picker;
		private final CanvasView view;
		private final MenuItem menuItem;

		public ColorPickerButtonEvent(CanvasView view, ColorPicker picker, MenuItem menuItem) {
			this.picker = picker;
			this.view = view;
			this.menuItem = menuItem;
		}

		@Override
		public void handle(ActionEvent event) {
			PopupColorPicker popup = new PopupColorPicker(picker);
			Point2D p = view.getUiCanvasEditor().localToScreen(0, 0);
			popup.show(view.getUiCanvasEditor(), p.getX(), p.getY());
			popup.getPicker().setOnHidden(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					popup.hide();
					menuItem.fire();
				}
			});
		}
	}

}
