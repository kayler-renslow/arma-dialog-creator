package com.kaylerrenslow.armaDialogCreator.gui.popup;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 A simple wizard interface

 @author Kayler
 @since 11/23/2016 */
public class WizardStageDialog extends StageDialog<StackPane> {
	private final List<WizardStep> wizardSteps = new LinkedList<>();
	private final ReadOnlyList<WizardStep> wizardStepsReadOnly;
	private final Button btnPrevious;
	private int wizardStepInd = 0;

	public WizardStageDialog(@Nullable Stage primaryStage, @Nullable String title, boolean hasHelp, @NotNull WizardStep... wizardSteps) {
		super(primaryStage, new StackPane(), title, true, true, hasHelp);


		wizardStepsReadOnly = new ReadOnlyList<>(wizardSteps);

		btnPrevious = new Button(Lang.ApplicationBundle().getString("Wizards.previous"));
		btnPrevious.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				goBackwardStep();
			}
		});

		btnPrevious.setPrefWidth(GenericResponseFooter.PREFFERED_BUTTON_OK_WIDTH);
		footer.getRightContainer().getChildren().add(1, btnPrevious);
		footer.btnOk.setText(Lang.ApplicationBundle().getString("Wizards.next"));

		Collections.addAll(this.wizardSteps, wizardSteps);

		for (WizardStep step : wizardSteps) {
			addWizardStep(step);
		}
		btnPrevious.setDisable(true);
	}

	protected void addWizardStep(@NotNull WizardStep step) {
		if (myRootElement.getChildren().size() == 0) {
			myRootElement.getChildren().add(step.getContent());
		}
		wizardSteps.add(step);
	}

	@NotNull
	public WizardStep getCurrentStep() {
		return wizardSteps.get(wizardStepInd);
	}

	@NotNull
	public ReadOnlyList<WizardStep> getWizardStepsReadOnly() {
		return wizardStepsReadOnly;
	}

	@Override
	protected final void ok() {
		if (wizardStepInd == wizardSteps.size() - 1) {
			stepsComplete();
			super.ok();
		}
		goForwardStep();
	}

	protected boolean goForwardStep() {
		if (!getCurrentStep().stepIsComplete()) {
			beepFocus();
			return false;
		}
		getCurrentStep().stepLeft(true);
		wizardStepInd++;
		if (wizardStepInd >= wizardSteps.size()) {
			wizardStepInd = wizardSteps.size() - 1;
		}
		updateButtons();

		updateContent();
		getCurrentStep().stepPresented();

		return true;
	}

	private void updateButtons() {
		if (wizardStepInd == wizardSteps.size() - 1) {
			footer.btnOk.setText(Lang.ApplicationBundle().getString("Wizards.finish"));
		} else {
			footer.btnOk.setText(Lang.ApplicationBundle().getString("Wizards.next"));
		}
		btnPrevious.setDisable(wizardStepInd <= 0);
	}

	protected void goBackwardStep() {
		getCurrentStep().stepLeft(false);
		wizardStepInd--;
		if (wizardStepInd <= 0) {
			wizardStepInd = 0;
		}
		updateButtons();
		updateContent();

	}

	private void updateContent() {
		myRootElement.getChildren().clear();
		myRootElement.getChildren().add(getCurrentStep().getContent());

	}

	/** Invoked when wizard has reached last step and dialog is closing */
	public void stepsComplete() {

	}
}
