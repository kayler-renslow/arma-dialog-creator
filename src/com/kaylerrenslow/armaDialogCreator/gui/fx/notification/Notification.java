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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 11/16/2016 */
public class Notification {

	private final VBox root = new VBox();
	private final String notificationTitle;
	private final String notificationText;
	private long displayDurationMilliseconds;
	private final BooleanProperty showProperty = new SimpleBooleanProperty(true);

	public Notification(@NotNull String notificationTitle, @NotNull String notificationText) {
		this(notificationTitle, notificationText, 10 * 1000);
	}

	public Notification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds) {
		this.notificationTitle = notificationTitle;
		this.notificationText = notificationText;
		this.displayDurationMilliseconds = displayDurationMilliseconds;

		root.setStyle("-fx-background-color:-fx-control-inner-background");

		final Label lblTitle = new Label(notificationTitle);
		lblTitle.setFont(Font.font(15));
		lblTitle.setStyle("-fx-text-fill:white");
		final BorderPane borderPaneTitle = new BorderPane(null, null, null, null, lblTitle);
		borderPaneTitle.setStyle("-fx-background-color:-fx-accent;");
		borderPaneTitle.setPadding(new Insets(5));

		VBox.setVgrow(borderPaneTitle, Priority.NEVER);
		root.getChildren().add(borderPaneTitle);


		final StackPane content = new StackPane(new Label(notificationText));
		content.setPadding(borderPaneTitle.getPadding());
		root.getChildren().add(content);

		root.setMaxWidth(360);
		root.setMaxHeight(150);

	}

	public boolean isShowing() {
		return showProperty.get();
	}

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
