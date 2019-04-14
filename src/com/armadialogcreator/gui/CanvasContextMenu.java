package com.armadialogcreator.gui;

import com.armadialogcreator.ADCGuiManager;
import com.armadialogcreator.canvas.UICanvasConfiguration;
import com.armadialogcreator.canvas.UICanvasEditor;
import com.armadialogcreator.gui.main.actions.ToggleViewportSnappingAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateMacroAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateNewControlAction;
import com.armadialogcreator.gui.main.actions.mainMenu.create.CreateNewCustomControlAction;
import com.armadialogcreator.gui.main.actions.mainMenu.view.ViewShowGridAction;
import com.armadialogcreator.img.icons.ADCIcons;
import com.armadialogcreator.lang.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;


/**
 Used with {@link UICanvasEditor#setCanvasContextMenu(ContextMenu)}.

 @author Kayler
 @since 11/15/2016 */
public class CanvasContextMenu extends ContextMenu {
	public CanvasContextMenu() {

	ResourceBundle bundle = Lang.getBundle("MainMenuBarBundle");
		MenuItem create_control = addOnAction(bundle.getString("create_control"), new CreateNewControlAction());
		MenuItem create_macro = addOnAction(bundle.getString("create_macro"), new CreateMacroAction());
		create_macro.setGraphic(new ImageView(ADCIcons.ICON_HASH_MINIPLUS));
		MenuItem create_control_class = addOnAction(bundle.getString("create_control_class"), new CreateNewCustomControlAction());
		Menu menuCreate = new Menu(
				bundle.getString("create"), null,
				create_control,
				create_macro,
				create_control_class
		);
		getItems().add(menuCreate);

		ResourceBundle bundleCanvasControls = Lang.ApplicationBundle();

		CheckMenuItem menuItemViewportSnap = addOnAction(new CheckMenuItem(bundleCanvasControls.getString("CanvasControls.viewport_snapping")), new ToggleViewportSnappingAction());
		CheckMenuItem menuItemShowGrid = addOnAction(new CheckMenuItem(bundle.getString("view_show_grid")), new ViewShowGridAction());
		getItems().add(menuItemViewportSnap);
		getItems().add(menuItemShowGrid);

		showingProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				UICanvasConfiguration conf = ADCGuiManager.instance.getCanvasConfiguration();
				menuItemViewportSnap.setSelected(conf.viewportSnapEnabled());
			}
		});
		showingProperty().addListener(new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				UICanvasConfiguration conf = ADCGuiManager.instance.getCanvasConfiguration();
				menuItemShowGrid.setSelected(conf.showGrid());
			}
		});
	}

	private static <E extends MenuItem> E addOnAction(E menuItem, EventHandler<ActionEvent> event) {
		menuItem.setOnAction(event);
		return menuItem;
	}

	private MenuItem addOnAction(String text, EventHandler<ActionEvent> event) {
		MenuItem menuItem = new MenuItem(text);
		menuItem.setOnAction(event);
		return menuItem;
	}

}
