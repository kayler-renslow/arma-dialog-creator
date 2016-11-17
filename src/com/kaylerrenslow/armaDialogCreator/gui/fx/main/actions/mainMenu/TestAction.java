/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

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
