package com.armadialogcreator.gui.popup;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 A wrapper class for {@link StagePopup} that is for dialogs. The difference between a {@link StagePopup} and a {@link StageDialog} is that the {@link StageDialog} will always halt the thread when
 shown and resume the thread when it is closed.

 @author Kayler
 @since 08/20/2016. */
public class StageDialog<T extends Parent> extends StagePopup<VBox> {

	/** Root element of the dialog body. This is the element where all content should be placed. */
	protected final T myRootElement;

	private boolean cancel = false;

	public StageDialog(@Nullable Stage primaryStage, @NotNull T myRootDialogElement, @Nullable String title, boolean canCancel, boolean canOk, boolean hasHelp) {
		super(primaryStage, new VBox(5), title);
		this.myRootElement = myRootDialogElement;
		super.myRootElement.setPadding(new Insets(10));
		VBox.setVgrow(myRootElement, Priority.ALWAYS);
		super.myRootElement.getChildren().addAll(myRootDialogElement, new Separator(Orientation.HORIZONTAL), getBoundResponseFooter(canCancel, canOk, hasHelp));
		myStage.initModality(Modality.APPLICATION_MODAL);
	}

	@Override
	protected final GenericResponseFooter getBoundResponseFooter(boolean addCancel, boolean addOk, boolean addHelpButton) {
		return super.getBoundResponseFooter(addCancel, addOk, addHelpButton);
	}

	@Override
	protected void cancel() {
		super.cancel();
		this.cancel = true;
	}

	/** Return true if the dialog was cancelled (cancel button was pressed), false otherwise */
	public boolean wasCancelled() {
		return this.cancel;
	}

	/** Implementation is: {@link Stage#showAndWait()} */
	@Override
	public void show() {
		showAndWait();
	}

	@Override
	protected void onCloseRequest(WindowEvent event) {
		this.cancel = true;
		super.onCloseRequest(event);
	}
}
