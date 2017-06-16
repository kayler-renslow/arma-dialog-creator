

/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */
package com.kaylerrenslow.armaDialogCreator.gui.notification;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @see Notifications
 @since 11/16/2016 */
public class NotificationPane {
	private final Pane pane;

	/**
	 Constructs a new {@link NotificationPane} that holds notifications.

	 @param pane the pane to add notifications to
	 */
	public NotificationPane(@NotNull Pane pane) {
		this.pane = pane;
	}

	/** Get the {@link Pane} instance that will store all of the notifications */
	@NotNull
	public Pane getContentPane() {
		return pane;
	}

	public void addNotification(@NotNull Notification notification) {
		pane.getChildren().add(notification.getContentRoot());
		notification.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean showing) {
				if (!showing) {
					pane.getChildren().remove(notification.getContentRoot());
				} else {
					pane.getChildren().add(notification.getContentRoot());
				}
			}
		});
	}
}
