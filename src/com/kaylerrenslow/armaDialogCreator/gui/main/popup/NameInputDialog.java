package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A simple dialog that displays a message, has a text field below the message, and the input text can be fetched via {@link #getInputText()}

 @author Kayler
 @since 12/24/2016 */
public class NameInputDialog extends StageDialog<VBox> {
	protected final TextField tf = new TextField();

	public NameInputDialog(@NotNull String title, @NotNull String message) {
		this(title, message, null);
	}

	public NameInputDialog(@NotNull String title, @NotNull String message, @Nullable String promptText) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), title, true, true, false);
		myRootElement.setFillWidth(true);
		myRootElement.getChildren().add(new Label(message));
		myRootElement.getChildren().add(tf);
		myRootElement.setPrefWidth(320);
		myStage.setResizable(false);
		tf.setPromptText(promptText);
		tf.setFocusTraversable(false);
	}

	@Nullable
	public String getInputText() {
		return tf.getText();
	}
}
