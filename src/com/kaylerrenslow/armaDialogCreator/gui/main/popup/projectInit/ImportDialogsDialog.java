package com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.data.HeaderConversionException;
import com.kaylerrenslow.armaDialogCreator.data.HeaderToProject;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectInit;
import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectXmlLoader;
import com.kaylerrenslow.armaDialogCreator.data.xml.XmlParseException;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CheckboxSelectionPane;
import com.kaylerrenslow.armaDialogCreator.gui.popup.WizardStageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.WizardStep;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

/**
 @author Kayler
 @since 05/05/2017 */
public class ImportDialogsDialog extends WizardStageDialog {

	/*
	* Order of operations for dialog:
	* 1.
	* 2. todo write this stuff in
	* 3.
	* 4.
	*
	* */

	private final ResourceBundle bundle = Lang.getBundle("ProjectInitWindowBundle");
	/** Queue for sending messages from JavaFX to converter */
	private final ArrayBlockingQueue<Object> messageQToTask = new ArrayBlockingQueue<>(1);

	private final SelectDialogsToImportStep importStep;
	private final ParsingStep parsingStep;
	private final ConvertingDialogsStep convertingStep;
	private final SelectDialogToLoadStep selectDialogToLoadStep;

	private ProjectInit projectInit;

	public ImportDialogsDialog(@NotNull ADCProjectInitWindow initWindow, @NotNull File initialWorkspaceDir, @NotNull File descExt) {
		super(initWindow.getStage(), null, true);

		setTitle(bundle.getString("ImportDialogs.window_title"));

		parsingStep = new ParsingStep();
		importStep = new SelectDialogsToImportStep();
		convertingStep = new ConvertingDialogsStep();
		selectDialogToLoadStep = new SelectDialogToLoadStep();
		addWizardStep(parsingStep);
		addWizardStep(importStep);
		addWizardStep(convertingStep);
		addWizardStep(selectDialogToLoadStep);

		//start convert thread
		ConvertTask convertTask = new ConvertTask(initialWorkspaceDir, descExt);
		convertTask.messageProperty().addListener((observable, oldValue, newValue) -> {
			MyWizardStep step = (MyWizardStep) getCurrentStep();
			step.message(newValue);
		});
		convertTask.progressProperty().addListener((observable, oldValue, newValue) -> {
			MyWizardStep step = (MyWizardStep) getCurrentStep();
			step.progressUpdate(newValue.doubleValue());
		});
		convertTask.exceptionProperty().addListener((observable, oldValue, newValue) -> newValue.printStackTrace());
		Thread convertThread = new Thread(convertTask);
		convertThread.setName("ADC - Convert description.ext task thread");
		convertThread.setDaemon(false);
		convertThread.start();

		setStageSize(initWindow.getStageWidth(), initWindow.getStageHeight());
	}

	@Override
	protected boolean goForwardStep() {
		WizardStep before = getCurrentStep();
		boolean moved = super.goForwardStep();
		if (!moved) {
			return false;
		}

		//allow using to see what was entered, but don't let them modify anything
		before.getContent().setDisable(true);
		return true;
	}

	private void selectDialogsEvent(@NotNull List<String> dialogNames) {
		importStep.displayDialogNames(dialogNames);
		parsingStep.complete();
		goForwardStep(); //go to the select dialogs step
	}

	private void setProjectToLoad(@NotNull ProjectInit projectInit) {
		this.projectInit = projectInit;
	}

	private void parseCompleteEvent() {

	}

	private void dialogConversionFailedEvent(@NotNull String dialogClassName, @NotNull Exception e) {
		//todo
		e.printStackTrace(System.out);
	}

	@Nullable
	public ProjectInit getProjectInit() {
		return projectInit;
	}

	private class ConvertTask extends Task<Boolean> implements HeaderToProject.ConversionCallback {

		private File workspaceDir;
		private File descExt;

		public ConvertTask(@NotNull File workspaceDir, @NotNull File descExt) {
			this.workspaceDir = workspaceDir;
			this.descExt = descExt;
		}

		@Override
		protected Boolean call() throws Exception {
			HeaderToProject.convertAndSaveToWorkspace(workspaceDir, descExt, this);

			//wait for the JavaFX thread to tell what project file to load
			File projectFile = (File) messageQToTask.take();

			ProjectXmlLoader.ProjectPreviewParseResult parseResult;
			try {
				parseResult = ProjectXmlLoader.previewParseProjectXmlFile(projectFile);
			} catch (XmlParseException e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						new CouldNotLoadProjectDialog(e).show();
					}
				});
				return false;
			}


			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					setProjectToLoad(new ProjectInit.OpenProject(parseResult));
				}
			});
			return true;
		}

		@NotNull
		@Override
		public List<String> selectClassesToSave(@NotNull List<String> classesDiscovered) {
			List<String> ret;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					selectDialogsEvent(classesDiscovered);
				}
			});

			try {
				ret = (List<String>) messageQToTask.take();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return ret;
		}

		@Override
		public void message(@NotNull String msg) {
			updateMessage(msg);
		}

		@Override
		public void progressUpdate(int stepsCompleted, int totalSteps) {
			updateProgress(stepsCompleted, totalSteps);
		}

		@Override
		public void finishedParse() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					parseCompleteEvent();
				}
			});
		}

		@Override
		public void conversionFailed(@NotNull String dialogClassName, @NotNull HeaderConversionException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					dialogConversionFailedEvent(dialogClassName, e);
				}
			});
		}
	}

	private interface MyWizardStep {

		void message(@NotNull String msg);
		void progressUpdate(double progress);

	}

	private class ParsingStep extends WizardStep<StackPane> implements MyWizardStep {

		private boolean complete = false;
		private final Label lblMessage = new Label();

		public ParsingStep() {
			super(new StackPane());
			VBox vboxBody = new VBox(10);
			content.getChildren().add(vboxBody);
			Label lblHeader = new Label(bundle.getString("ImportDialogs.Step.Parsing.body"));
			lblHeader.setFont(Font.font(15));
			vboxBody.getChildren().add(lblHeader);
			vboxBody.getChildren().add(lblMessage);
			content.setAlignment(Pos.CENTER);
		}

		public void complete() {
			this.complete = true;
		}

		@Override
		protected boolean stepIsComplete() {
			return complete;
		}

		@Override
		public void message(@NotNull String msg) {
			lblMessage.setText(msg);
		}

		@Override
		public void progressUpdate(double progress) {
		}
	}

	private class SelectDialogsToImportStep extends WizardStep<VBox> implements MyWizardStep {
		private final CheckboxSelectionPane<String> pane = new CheckboxSelectionPane<>();
		private boolean done = false;

		public SelectDialogsToImportStep() {
			super(new VBox(10));
			content.getChildren().add(new Label(bundle.getString("ImportDialogs.Step.SelectDialogs.body")));
			content.getChildren().add(pane);
		}

		@Override
		protected boolean stepIsComplete() {
			return pane.getSelected().size() > 0;
		}

		public void displayDialogNames(@NotNull List<String> dialogNames) {
			for (String s : dialogNames) {
				pane.addItem(s);
			}
		}

		@Override
		public void message(@NotNull String msg) {

		}

		@Override
		public void progressUpdate(double progress) {

		}


		@Override
		protected void stepLeft(boolean movingForward) {
			if (movingForward) {
				if (!done) {
					messageQToTask.add(pane.getSelected());
				}
				done = true;
			}
		}

	}

	private class ConvertingDialogsStep extends WizardStep<StackPane> implements MyWizardStep {

		private final Label lblMessage = new Label("......");
		private final ProgressBar progressBar = new ProgressBar();

		public ConvertingDialogsStep() {
			super(new StackPane());
			content.setAlignment(Pos.CENTER);
			Label lblHeader = new Label(bundle.getString("ImportDialogs.Step.Converting.title"));
			lblHeader.setFont(Font.font(15));
			content.getChildren().add(new VBox(10, lblHeader, lblMessage, progressBar));
			progressBar.minWidthProperty().bind(content.widthProperty().multiply(.75));
		}

		@Override
		protected boolean stepIsComplete() {
			return false;
		}

		@Override
		public void message(@NotNull String msg) {
			lblMessage.setText(msg);
		}

		@Override
		public void progressUpdate(double progress) {
			progressBar.setProgress(progress);
		}
	}

	private class SelectDialogToLoadStep extends WizardStep<VBox> implements MyWizardStep {

		public SelectDialogToLoadStep() {
			super(new VBox(10));
		}

		@Override
		protected boolean stepIsComplete() {
			return false;
		}

		@Override
		public void message(@NotNull String msg) {

		}

		@Override
		public void progressUpdate(double progress) {

		}
	}
}
