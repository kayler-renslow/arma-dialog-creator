package com.kaylerrenslow.armaDialogCreator.gui.fx.main.actions.mainMenu;

import com.kaylerrenslow.armaDialogCreator.gui.fx.notification.Notification;
import com.kaylerrenslow.armaDialogCreator.gui.fx.notification.Notifications;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author Kayler
 Implementation varies. Used for debugging/testing specific features
 Created on 09/14/2016. */
public class TestAction implements EventHandler<ActionEvent> {
	private static int i = 0;

	@Override
	public void handle(ActionEvent event) {
		if (i % 2 == 0) {
			Notifications.showNotification(new Notification("hello" + (i++),
							"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce vestibulum nunc id ligula suscipit, nec rutrum metus lacinia. Nulla vestibulum, orci vitae dapibus elementum, leo.",
							6 * 1000
					)
			);
		} else {
			Notifications.showNotification(new Notification("hello" + (i++), "this is a test <a href=\"google.com\">anchor</a>", 6 * 1000));

		}
	}
}
