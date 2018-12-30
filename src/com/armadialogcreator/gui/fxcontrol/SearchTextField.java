package com.armadialogcreator.gui.fxcontrol;

import com.armadialogcreator.main.Lang;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A simple search text field with a search icon

 @author Kayler
 @since 12/14/2016 */
public class SearchTextField extends StackPane {
	private static final String STYLE_CLASS = "search-text-field";
	private static final String STYLE_CLASS_FOCUSED = "search-text-field-focused";

	public static final Image SEARCH_ICON = new Image("/com/armadialogcreator/gui/img/icons/search.png");

	private final TextField tf;

	/**
	 Create a search text field with a provided {@link #textProperty()} change listener

	 @param textPropertyListener change listener
	 */
	public SearchTextField(@Nullable ChangeListener<String> textPropertyListener) {
		setAlignment(Pos.CENTER_LEFT);

		tf = new TextField();
		tf.setStyle("-fx-background-color:transparent;-fx-background-radius: 0;");
		tf.setPadding(new Insets(5));

		tf.setPromptText(Lang.FxControlBundle().getString("SearchTextField.search"));
		ImageView imageView = new ImageView(SEARCH_ICON);
		StackPane stackPane = new StackPane(imageView);
		stackPane.setPadding(new Insets(5, 0, 5, 5));
		HBox hBox = new HBox(0, stackPane, tf);
		HBox.setHgrow(tf, Priority.ALWAYS);
		hBox.setCursor(Cursor.TEXT);
		hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tf.requestFocus();
				tf.selectEnd();
				tf.deselect();
			}
		});
		getStyleClass().add(STYLE_CLASS);
		tf.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focused) {
				if (focused) {
					if (!getStyleClass().contains(STYLE_CLASS_FOCUSED)) {
						getStyleClass().add(STYLE_CLASS_FOCUSED);
					}
				} else {
					getStyleClass().remove(STYLE_CLASS_FOCUSED);
				}
			}
		});
		getChildren().add(hBox);

		if (textPropertyListener != null) {
			textProperty().addListener(textPropertyListener);
		}
	}

	/**
	 Create a search text field with no default text property listener

	 @see SearchTextField#SearchTextField(ChangeListener)
	 */
	public SearchTextField() {
		this(null);
	}

	public void setText(@NotNull String text) {
		tf.setText(text);
	}

	@NotNull
	public String getText() {
		return tf.getText() == null ? "" : tf.getText();
	}

	@NotNull
	public StringProperty textProperty() {
		return tf.textProperty();
	}
}
