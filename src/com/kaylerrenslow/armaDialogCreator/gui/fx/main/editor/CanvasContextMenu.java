/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.main.editor;

import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.ToggleViewportSnappingAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create.CreateMacroAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create.CreateNewControlAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.create.CreateNewCustomControlAction;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu.view.ViewShowGridAction;
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
