package com.kaylerrenslow.armaDialogCreator.gui.popup;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
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
	private final ChangeListener<Boolean> stepCompletedListener = (observable, oldValue, completed) -> {
		footer.btnOk.setDisable(!completed);
	};

	/**
	 If set to true, {@link #ok} will invoke {@link super#ok()} (and thus close the dialog) regardless if the wizard is on the last step or not/
	 Default value is false
	 */
	protected final BooleanProperty forceOkProperty = new SimpleBooleanProperty(false);

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
			step.stepIsCompleteProperty.addListener(stepCompletedListener);
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
		if (wizardStepInd == wizardSteps.size() - 1 || forceOkProperty.get()) {
			if (forceOkProperty.get()) {
				stepsComplete();
			}
			super.ok();
			return;
		}
		goForwardStep();
	}

	protected boolean goForwardStep() {
		WizardStep lastStep = getCurrentStep();
		if (!lastStep.stepIsComplete()) {
			beepFocus();
			return false;
		}
		lastStep.stepIsCompleteProperty.removeListener(stepCompletedListener);
		lastStep.stepLeft(true);
		wizardStepInd++;
		if (wizardStepInd >= wizardSteps.size()) {
			wizardStepInd = wizardSteps.size() - 1;
		}
		getCurrentStep().stepIsCompleteProperty.addListener(stepCompletedListener);
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
		btnOk.setDisable(!getCurrentStep().stepIsCompleteProperty.get());
	}

	protected void goBackwardStep() {
		WizardStep currentStep = getCurrentStep();
		currentStep.stepLeft(false);
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

	/**
	 Invoked when wizard has reached last step and dialog is closing.
	 If {@link #forceOkProperty} is set to true and the wizard is not on the last step, this will not be invoked.
	 */
	public void stepsComplete() {

	}
}
