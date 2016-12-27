package com.kaylerrenslow.armaDialogCreator.gui.notification;

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
 A {@link BoxNotification} is displayed by {@link Notifications}.

 @author Kayler
 @since 12/26/2016 */
public class BoxNotification extends Notification {

	private final VBox root = new VBox();

	public BoxNotification(@NotNull String notificationTitle, @NotNull String notificationText) {
		super(notificationTitle, notificationText);
	}

	public BoxNotification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds) {
		super(notificationTitle, notificationText, displayDurationMilliseconds);
	}

	public BoxNotification(@NotNull String notificationTitle, @NotNull String notificationText, long displayDurationMilliseconds, boolean isErrorNotification) {
		super(notificationTitle, notificationText, displayDurationMilliseconds, isErrorNotification);
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

	/** Get the JavaFX node that represents notification */
	@NotNull
	public Region getContentRoot() {
		return root;
	}

}
