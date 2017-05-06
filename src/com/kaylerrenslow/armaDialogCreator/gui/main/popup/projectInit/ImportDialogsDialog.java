package com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.data.HeaderConversionException;
import com.kaylerrenslow.armaDialogCreator.data.HeaderToProject;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.CheckboxSelectionPane;
import com.kaylerrenslow.armaDialogCreator.gui.popup.WizardStageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.popup.WizardStep;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.KeyValue;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;

/**
 @author Kayler
 @since 05/05/2017 */
public class ImportDialogsDialog extends WizardStageDialog {
	private enum Message {
		/** Used for closing the thread that forwards the convert task messages to JavaFX */
		CloseReaderThread,
		/** Used for invoking {@link #selectDialogsEvent(List)} */
		SelectClassesToSave,
		/** Used for when the conversion and saving of the conversion is complete */
		ConversionAndSaveComplete,
		/** Used for when the converter converted the description.ext file and is ready to build dialogs. */
		ParseComplete,
		/** Used for when the converter couldn't convert a dialog */
		ConversionFailed
	}

	private final ResourceBundle bundle = Lang.getBundle("ProjectInitWindowBundle");
	/** Middleman queue for sending messages from converter to JavaFX */
	private final ArrayBlockingQueue<KeyValue<Message, Object>> messageQFromTask = new ArrayBlockingQueue<>(1);
	/** Queue for sending messages from JavaFX to converter */
	private final ArrayBlockingQueue<KeyValue<Message, Object>> messageQToTask = new ArrayBlockingQueue<>(1);

	private final SelectDialogsToImportStep importStep;

	private File projectFileToOpen;

	public ImportDialogsDialog(@NotNull File initialWorkspaceDir, @NotNull File descExt) {
		super(ArmaDialogCreator.getPrimaryStage(), null, true);

		setTitle(bundle.getString("ImportDialogs.window_title"));

		addWizardStep(new ParsingStep());
		importStep = new SelectDialogsToImportStep();
		addWizardStep(importStep);

		//start threads
		MessageReaderThread readerThread = new MessageReaderThread();
		readerThread.setDaemon(false);
		readerThread.start();

		ConvertTask convertTask = new ConvertTask(initialWorkspaceDir, descExt);
		convertTask.exceptionProperty().addListener((observable, oldValue, newValue) -> newValue.printStackTrace());
		Thread convertThread = new Thread(convertTask);
		convertThread.setDaemon(false);
		convertThread.start();
	}

	@Override
	protected void goForwardStep() {
		WizardStep before = getCurrentStep();
		super.goForwardStep();

		if (before == getCurrentStep()) {
			return;
		}

		//allow using to see what was entered, but don't let them modify anything
		before.getContent().setDisable(true);
	}

	private void selectDialogsEvent(@NotNull List<String> dialogNames) {
		importStep.displayDialogNames(dialogNames);
		goForwardStep(); //go to the select dialogs step

	}

	private void chooseProjectToLoadEvent() {
		//projectFileToOpen =

	}

	private void dialogConversionFailed(@NotNull String dialogClassName, @NotNull Exception e) {
		e.printStackTrace(System.out);
	}

	@NotNull
	public File getProjectFileToOpen() {
		return projectFileToOpen;
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

			messageQFromTask.add(new KeyValue<>(Message.ConversionAndSaveComplete, ""));

			return true;
		}

		@NotNull
		@Override
		public List<String> selectClassesToSave(@NotNull List<String> classesDiscovered) {
			List<String> ret;
			messageQFromTask.add(new KeyValue<>(Message.SelectClassesToSave, classesDiscovered));
			try {
				KeyValue<Message, Object> result = messageQToTask.take();
				ret = (List<String>) result.getValue();
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
			messageQFromTask.add(new KeyValue<>(Message.ParseComplete, ""));
		}

		@Override
		public void conversionFailed(@NotNull String dialogClassName, @NotNull HeaderConversionException e) {
			messageQFromTask.add(new KeyValue<>(Message.ConversionFailed, new Object[]{dialogClassName, e}));
		}
	}

	private class ParsingStep extends WizardStep<VBox> {

		private boolean complete = false;

		public ParsingStep() {
			super(new VBox(10));
		}

		public void complete() {
			this.complete = true;
		}

		@Override
		protected boolean stepIsComplete() {
			return complete;
		}
	}

	private class SelectDialogsToImportStep extends WizardStep<VBox> {
		private final CheckboxSelectionPane<String> pane = new CheckboxSelectionPane<>();
		public SelectDialogsToImportStep() {
			super(new VBox(10));
			myRootElement.getChildren().add(pane);
		}

		@Override
		protected boolean stepIsComplete() {
			return false;
		}

		public void displayDialogNames(@NotNull List<String> dialogNames) {
			for (String s : dialogNames) {
				pane.addItem(s);
			}
		}
	}

	/** Takes messages sent from task thread and then invokes the relevant method on the JavaFX thread to handle the message body */
	private class MessageReaderThread extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					KeyValue<Message, Object> msg = messageQFromTask.take();

					Runnable r;

					switch (msg.getKey()) {
						case SelectClassesToSave: {
							r = new Runnable() {
								@Override
								public void run() {
									selectDialogsEvent((List<String>) msg.getValue());
								}
							};
							break;
						}
						case ConversionAndSaveComplete: {
							r = new Runnable() {
								@Override
								public void run() {
									chooseProjectToLoadEvent();
								}
							};
							break;
						}
						case ConversionFailed: {
							r = new Runnable() {
								@Override
								public void run() {

								}
							};
							break;
						}
						case CloseReaderThread: {
							return;
						}
						default: {
							throw new RuntimeException("unknown message type:" + msg.getKey());
						}
					}
					Platform.runLater(r);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
}
