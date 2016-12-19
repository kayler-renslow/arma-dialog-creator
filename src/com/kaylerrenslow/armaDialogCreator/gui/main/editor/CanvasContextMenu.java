package com.kaylerrenslow.armaDialogCreator.gui.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.main.actions.ToggleViewportSnappingAction;
import com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.create.CreateMacroAction;
import com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.create.CreateNewControlAction;
import com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.create.CreateNewCustomControlAction;
import com.kaylerrenslow.armaDialogCreator.gui.main.actions.mainMenu.view.ViewShowGridAction;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 Used with {@link UICanvasEditor#setCanvasContextMenu(ContextMenu)}.

 @author Kayler
 @since 11/15/2016 */
public class CanvasContextMenu extends ContextMenu {
	public CanvasContextMenu() {
		Menu menuCreate = new Menu(
				Lang.ApplicationBundle().getString("MainMenuBar.create"), null,
				addOnAction(Lang.ApplicationBundle().getString("MainMenuBar.create_control"), new CreateNewControlAction()),
				addOnAction(Lang.ApplicationBundle().getString("MainMenuBar.create_macro"), new CreateMacroAction()),
				addOnAction(Lang.ApplicationBundle().getString("MainMenuBar.create_control_class"), new CreateNewCustomControlAction())
		);
		getItems().add(menuCreate);

		CheckMenuItem menuItemViewportSnap = addOnAction(new CheckMenuItem(Lang.ApplicationBundle().getString("CanvasControls.viewport_snapping")), new ToggleViewportSnappingAction());
		CheckMenuItem menuItemShowGrid = addOnAction(new CheckMenuItem(Lang.ApplicationBundle().getString("MainMenuBar.view_show_grid")), new ViewShowGridAction());
		getItems().add(menuItemViewportSnap);
		getItems().add(menuItemShowGrid);

		showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				menuItemViewportSnap.setSelected(ArmaDialogCreator.getCanvasView().getConfiguration().viewportSnapEnabled());
			}
		});
		showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				menuItemShowGrid.setSelected(ArmaDialogCreator.getCanvasView().getConfiguration().showGrid());
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
