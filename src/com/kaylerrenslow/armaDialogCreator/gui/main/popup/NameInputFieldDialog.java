package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputField;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.inputfield.InputFieldDataChecker;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A simple dialog that displays a message,
 has a {@link InputField} below the message, and the input text can be fetched via {@link #getInputText()}

 @author Kayler
 @since 7/2/2017 */
public class NameInputFieldDialog<C extends InputFieldDataChecker<V>, V> extends StageDialog<VBox> {
	protected final InputField<C, V> inputField;
	private final BooleanProperty canOkProperty = new SimpleBooleanProperty(true);
	private final Label lblMessage = new Label();

	public NameInputFieldDialog(@NotNull String title, @NotNull String message, @NotNull C checker) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), title, true, true, false);
		inputField = new InputField<>(checker);
		myRootElement.setFillWidth(true);
		myRootElement.getChildren().add(lblMessage);
		setMessage(message);
		myRootElement.getChildren().add(inputField);
		myRootElement.setPrefWidth(320);
		myStage.setResizable(false);
		inputField.setFocusTraversable(false);
	}


	public void setInputText(@Nullable String text) {
		inputField.setText(text);
	}

	@Nullable
	public String getInputText() {
		return inputField.getText();
	}

	@NotNull
	public InputField<C, V> getInputField() {
		return inputField;
	}

	@NotNull
	public BooleanProperty getCanOkProperty() {
		return canOkProperty;
	}

	public void setMessage(@NotNull String msg) {
		lblMessage.setText(msg);
	}

	@Override
	protected void ok() {
		if (!canOkProperty.get()) {
			beepFocus();
			return;
		}
		super.ok();
	}
}
