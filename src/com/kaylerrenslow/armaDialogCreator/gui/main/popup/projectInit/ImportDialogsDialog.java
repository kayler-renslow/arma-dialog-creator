package com.kaylerrenslow.armaDialogCreator.gui.main.popup.projectInit;

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
		CloseReaderThread, SelectClassesToSave, ChooseProjectToLoad
	}

	private final ResourceBundle bundle = Lang.getBundle("ProjectInitWindowBundle");
	/** Middleman queue for sending messages from converter to JavaFX */
	private final ArrayBlockingQueue<KeyValue<Message, Object>> messageQFromTask = new ArrayBlockingQueue<>(1);
	/** Queue for sending messages from JavaFX to converter */
	private final ArrayBlockingQueue<KeyValue<Message, Object>> messageQToTask = new ArrayBlockingQueue<>(1);

	/** The converter task. */
	private final ConvertTask convertTask;

	private File projectFileToOpen;

	public ImportDialogsDialog(@NotNull File initialWorkspaceDir, @NotNull File descExt) {
		super(ArmaDialogCreator.getPrimaryStage(), null, true);

		convertTask = new ConvertTask(initialWorkspaceDir, descExt);

		setTitle(bundle.getString("ImportDialogs.window_title"));

		addWizardStep(new SelectDialogsToImportStep());


		//start threads
		MessageReaderThread readerThread = new MessageReaderThread();
		readerThread.setDaemon(false);
		readerThread.start();

		Thread convertThread = new Thread(convertTask);
		convertThread.setDaemon(false);
		convertThread.start();
	}

	private void selectDialogsEvent(@NotNull List<String> dialogNames) {
		//todo
	}

	private void chooseProjectToLoadEvent() {
		//projectFileToOpen =

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

			messageQFromTask.add(new KeyValue<>(Message.ChooseProjectToLoad, ""));

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
	}

	private class SelectDialogsToImportStep extends WizardStep<VBox> {
		public SelectDialogsToImportStep() {
			super(new VBox(10));
			CheckboxSelectionPane<String> pane = new CheckboxSelectionPane<>();
			pane.addItem("Hello World");
			pane.addItem("Hello World Again");
			myRootElement.getChildren().add(pane);
		}

		@Override
		protected boolean stepIsComplete() {
			return false;
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
						case ChooseProjectToLoad: {
							r = new Runnable() {
								@Override
								public void run() {
									chooseProjectToLoadEvent();
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
