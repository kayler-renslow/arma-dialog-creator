package com.armadialogcreator.gui.main.popup;

import com.armadialogcreator.gui.fxcontrol.inputfield.InputField;
import com.armadialogcreator.gui.fxcontrol.inputfield.InputFieldDataChecker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A simple dialog that displays a message,
 has a {@link InputField} below the message, and the input text can be fetched via {@link #getInputText()}

 @author Kayler
 @since 7/2/2017 */
public class NameInputFieldDialog<C extends InputFieldDataChecker<V>, V> extends InputDialog<InputField<C, V>> {

	public NameInputFieldDialog(@NotNull String title, @NotNull String message, @NotNull C checker) {
		super(title, message, new InputField<>(checker));
	}

	public void setInputText(@Nullable String text) {
		myNode.setText(text);
	}

	@Nullable
	public String getInputText() {
		return myNode.getText();
	}

	@NotNull
	public InputField<C, V> getInputField() {
		return myNode;
	}
}
