package com.armadialogcreator.gui;

import com.armadialogcreator.gui.fxcontrol.MenuItemEventHandler;
import com.armadialogcreator.util.TextUtil;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import org.jetbrains.annotations.NotNull;

/**
 Misc utilities for JavaFX

 @author Kayler
 @since 05/31/2016 */
public class FXUtil {

	public static <E extends MenuItem> E addOnAction(@NotNull E item, @NotNull MenuItemEventHandler<E> eventHandler) {
		item.setOnAction(eventHandler);
		eventHandler.setMenuItem(item);
		return item;
	}

	public static <E extends MenuItem> E addOnAction(@NotNull E item, @NotNull EventHandler<ActionEvent> eventHandler) {
		item.setOnAction(eventHandler);
		return item;
	}

	/**
	 Makes a tooltip that inserts newline characters when the tooltip text becomes to long

	 @param tooltip the text
	 @return the tooltip
	 */
	@NotNull
	public static Tooltip getMultilineTooltip(@NotNull String tooltip) {
		return new Tooltip(TextUtil.getMultilineText(tooltip, 60));
	}

	/**
	 Wait until <code>parent.isVisible()</code> is true and then run the given Runnable

	 @param parent parent to check visibility of
	 @param runnable runnable to run (on JavaFX thread)
	 */
	public static void runWhenVisible(@NotNull Parent parent, @NotNull Runnable runnable) {
		Task<Boolean> loadTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws Exception {
				while (!parent.isVisible()) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException ignore) {

					}
				}
				Platform.runLater(runnable);
				return true;
			}
		};
		Thread thread = new Thread(loadTask);
		thread.setDaemon(false);
		thread.start();
	}

}
