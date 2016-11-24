/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.gui.fx.popup;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

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

	public WizardStageDialog(Stage primaryStage, String title, boolean hasHelp, @NotNull WizardStep... wizardSteps) {
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

	protected void goForwardStep() {
		if (!getCurrentStep().stepIsComplete()) {
			beepFocus();
			return;
		}
		getCurrentStep().stepLeft();
		wizardStepInd++;
		if (wizardStepInd >= wizardSteps.size() - 1) {
			wizardStepInd = wizardSteps.size() - 1;
		}
		updateButtons();

		updateContent();
		getCurrentStep().stepPresented();
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
		getCurrentStep().stepLeft();
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
