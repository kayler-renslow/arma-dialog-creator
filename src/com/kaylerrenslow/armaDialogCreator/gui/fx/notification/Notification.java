/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.notification;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 A {@link Notification} is displayed by {@link Notifications}.

 @author Kayler
 @since 11/16/2016 */
public class Notification {

	private final VBox root = new VBox();
	private final String notificationTitle;
	private final String notificationText;
	private final BooleanProperty showProperty = new SimpleBooleanProperty(true);
	private long displayDurationMilliseconds;

	/**
	 A non-error notification that will last 10 seconds

	 @param notificationTitle title of notification
	 @param notificationText body text of notification
	 */
	public Notification(@NotNull String notificationTitle, @NotNull String notificationText) {
		this(notificationTitle, notificationText, 10 * 1000, false);
	}

	/**
	 A non-error notification that will be displayed for the specified amount of milliseconds.

	 @param notificationTitle title of notification
	 @param notificationText body text of notification
	 @param displayDurationMilliseconds how many milliseconds the notification should be displayed
	 */
	public Notification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds) {
		this(notificationTitle, notificationText, displayDurationMilliseconds, false);
	}

	/**
	 A notification that will be displayed for the specified amount of milliseconds. If the notification is an error notification, the header of the notification will be red.

	 @param notificationTitle title of notification
	 @param notificationText body text of notification
	 @param displayDurationMilliseconds how many milliseconds the notification should be displayed
	 @param isErrorNotification true if the notification should have a red header, otherwise the header will be equal to JavaFX css attribute "-fx-accent"
	 */
	public Notification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds, boolean isErrorNotification) {
		this.notificationTitle = notificationTitle;
		this.notificationText = notificationText;
		this.displayDurationMilliseconds = displayDurationMilliseconds;

		root.setStyle("-fx-background-color:-fx-control-inner-background;-fx-border-color:black;-fx-border-width:1px;");

		final Button btnClose = new Button("x");
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				setShowing(false);
			}
		});
		final Label lblTitle = new Label(notificationTitle);
		lblTitle.setFont(Font.font(15));
		lblTitle.setStyle("-fx-text-fill:white");
		final BorderPane borderPaneTitle = new BorderPane(null, null, btnClose, null, lblTitle);
		if (isErrorNotification) {
			borderPaneTitle.setStyle("-fx-background-color:red;");
		} else {
			borderPaneTitle.setStyle("-fx-background-color:-fx-accent;");
		}
		borderPaneTitle.setPadding(new Insets(5));

		VBox.setVgrow(borderPaneTitle, Priority.NEVER);
		root.getChildren().add(borderPaneTitle);


		final Label lblText = new Label(notificationText);
		lblText.setWrapText(true);
		final StackPane stackPaneContent = new StackPane(lblText);
		stackPaneContent.setAlignment(Pos.TOP_LEFT);
		stackPaneContent.setPadding(borderPaneTitle.getPadding());
		root.getChildren().add(stackPaneContent);

		final double width = 360;

		root.setPrefWidth(width);
		root.setMaxWidth(width);

	}

	/**
	 @return true if the notification is showing, false if it isn't
	 */
	public boolean isShowing() {
		return showProperty.get();
	}

	/**
	 Set whether or not the notification is showing

	 @param showing true if showing, false if not showing
	 */
	public void setShowing(boolean showing) {
		showProperty.setValue(showing);
	}

	@NotNull
	public BooleanProperty showingProperty() {
		return showProperty;
	}

	/**
	 Set how long the notification should be shown in milliseconds

	 @param displayDurationMilliseconds how many milliseconds to show notification
	 */
	public void setDisplayDuration(long displayDurationMilliseconds) {
		this.displayDurationMilliseconds = displayDurationMilliseconds;
	}

	public long getDisplayDurationMilliseconds() {
		return displayDurationMilliseconds;
	}

	@NotNull
	public String getNotificationTitle() {
		return notificationTitle;
	}

	@NotNull
	public String getNotificationText() {
		return notificationText;
	}

	/** Get the JavaFX node that represents notification */
	@NotNull
	public Region getContentRoot() {
		return root;
	}


	@Override
	public String toString() {
		return "Notification{" +
				"root=" + root +
				", notificationTitle='" + notificationTitle + '\'' +
				", notificationText='" + notificationText + '\'' +
				", displayDurationMilliseconds=" + displayDurationMilliseconds +
				", showProperty=" + showProperty +
				'}';
	}
}
