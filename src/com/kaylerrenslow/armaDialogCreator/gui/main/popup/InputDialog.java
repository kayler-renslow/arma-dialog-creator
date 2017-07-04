package com.kaylerrenslow.armaDialogCreator.gui.main.popup;

import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

/**
 A simple dialog that displays a message, has a {@link Node} below the message

 @author Kayler
 @since 12/24/2016 */
public class InputDialog<N extends Node> extends StageDialog<VBox> {
	/** The node of the input dialog */
	protected final N myNode;
	private final BooleanProperty canOkProperty = new SimpleBooleanProperty(true);
	private final Label lblMessage = new Label();

	public InputDialog(@NotNull String title, @NotNull String message, @NotNull N node) {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), title, true, true, false);
		this.myNode = node;
		myRootElement.setFillWidth(true);
		myRootElement.getChildren().add(lblMessage);
		setMessage(message);
		myRootElement.getChildren().add(node);
		myRootElement.setPrefWidth(320);
		myStage.setResizable(false);
		myNode.setFocusTraversable(false);
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
