package com.armadialogcreator.gui.fxcontrol;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import org.jetbrains.annotations.NotNull;


/**
 @author K
 @since 3/4/19 */
public interface MenuItemEventHandler<Mi extends MenuItem> extends EventHandler<ActionEvent> {
	void setMenuItem(@NotNull Mi menuItem);
}
