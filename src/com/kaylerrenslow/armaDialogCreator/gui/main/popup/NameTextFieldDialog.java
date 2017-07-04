package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A simple dialog that displays a message, has a {@link TextField} below the message,
 and the input text can be fetched via {@link #getInputText()}

 @author Kayler
 @since 12/24/2016 */
public class NameTextFieldDialog extends InputDialog<TextField> {

	public NameTextFieldDialog(@NotNull String title, @NotNull String message) {
		this(title, message, null);
	}

	public NameTextFieldDialog(@NotNull String title, @NotNull String message, @Nullable String promptText) {
		super(title, message, new TextField());
		myNode.setPromptText(promptText);
	}

	public void setInputText(@Nullable String text) {
		myNode.setText(text);
	}

	@Nullable
	public String getInputText() {
		return myNode.getText();
	}

	@NotNull
	public TextField getTextField() {
		return myNode;
	}

	@NotNull
	public StringProperty inputTextProperty() {
		return myNode.textProperty();
	}

}
