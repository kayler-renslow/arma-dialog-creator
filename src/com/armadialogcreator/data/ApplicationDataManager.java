package com.armadialogcreator.data;

import com.armadialogcreator.data.xml.ProjectSaveXmlWriter;
import com.armadialogcreator.data.xml.ResourceRegistryXmlLoader;
import com.armadialogcreator.data.xml.ResourceRegistryXmlWriter;
import com.armadialogcreator.gui.popup.StageDialog;
import com.armadialogcreator.main.ArmaDialogCreator;
import com.armadialogcreator.main.ExceptionHandler;
import com.armadialogcreator.main.Lang;
import com.armadialogcreator.util.DataContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

/**
 Manages save data

 @author Kayler
 @since 05/26/2016. */
public class ApplicationDataManager {
	private final ApplicationPropertyManager propertyManager = new ApplicationPropertyManager();
	private volatile Workspace workspace;
	private volatile ApplicationData applicationData;
	private volatile CountDownLatch waitForInitialize;

	@NotNull
	public Workspace loadWorkspace(@NotNull File workspaceDir) {
		if (!workspaceDir.exists()) {
			boolean made = workspaceDir.mkdirs();
		}
		workspace = new Workspace(workspaceDir);
		ApplicationProperty.LAST_WORKSPACE.put(ApplicationDataManager.getApplicationProperties(), workspaceDir);
		saveApplicationProperties();

		return workspace;
	}

	public void beginInitializing() {
		waitForInitialize = new CountDownLatch(1);
	}

	/** @return the newly created {@link ApplicationData} */
	@NotNull
	public ApplicationData initializeApplicationData() {
		this.applicationData = new ApplicationData();
		return applicationData;
	}

	public void initializeDone() {
		try {
			WorkspaceResourceRegistry globalResourceRegistry = workspace.getGlobalResourceRegistry();
			if (!globalResourceRegistry.getResourcesFile().exists()) {
				ResourceRegistryXmlWriter.WorkspaceResourceRegistryXmlWriter.writeAndClose(workspace);
			}
			new ResourceRegistryXmlLoader(globalResourceRegistry.getResourcesFile(), null)
					.load(globalResourceRegistry);
		} catch (Exception e) {
			ExceptionHandler.error(e);
		}

		waitForInitialize.countDown();

		new ChangeRegistrars(applicationData);
	}

	@NotNull
	public static ApplicationDataManager getInstance() {
		return ArmaDialogCreator.getApplicationDataManager();
	}

	/** Get application properties, which stores all {@link ApplicationProperty} instances */
	@NotNull
	public static DataContext getApplicationProperties() {
		return getInstance().propertyManager.getApplicationProperties();
	}

	/** Calls {@code {@link #getApplicationData()}.getCurrentProject()} */
	@NotNull
	public Project getCurrentProject() {
		return getApplicationData().getCurrentProject();
	}

	/**
	 This method is a blocking call and will not finish until the underlying {@link Workspace}
	 instance is set.

	 @return the current {@link Workspace} instance.
	 @throws IllegalStateException when trying to access this instance on the JavaFX thread while also trying to
	 initialize this value
	 */
	@NotNull
	public Workspace getWorkspace() {
		//do not make this method synchronized
		try {
			if (Thread.currentThread() == ArmaDialogCreator.getInitializingThread()) {
				if (isInitializing()) {
					throw new IllegalStateException("Trying to access the workspace on the initializing thread while " +
							"waiting to initialize");
				}
			}
			waitForInitialize.await();
		} catch (InterruptedException ignore) {
		}
		return workspace;
	}


	/**
	 This method is a blocking call and will not finish until the underlying {@link ApplicationData}
	 instance is set.

	 @return the current {@link ApplicationData} instance.
	 @throws IllegalStateException when trying to access this instance on the JavaFX thread while also trying to
	 initialize this value
	 */
	@NotNull
	public ApplicationData getApplicationData() {
		//do not make this method synchronized
		try {
			if (Thread.currentThread() == ArmaDialogCreator.getInitializingThread()) {
				if (isInitializing()) {
					throw new IllegalStateException("Trying to access the workspace on the initializing thread while " +
							"waiting to initialize");
				}
			}
			waitForInitialize.await();
		} catch (InterruptedException ignore) {
		}
		return applicationData;
	}

	/**
	 Set the arma 3 tools directory to a new one (can be null).
	 Automatically updates application properties.
	 */
	public void setArma3ToolsLocation(@Nullable File file) {
		propertyManager.setArma3ToolsLocation(file);
	}

	/**
	 Get the directory for where Arma 3 tools is saved.
	 If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null.
	 */
	@Nullable
	public File getArma3ToolsDirectory() {
		return propertyManager.getArma3ToolsDirectory();
	}

	/**
	 Saves the application properties.

	 @return true if the application properties were saved successfully, false if they weren't
	 */
	public boolean saveApplicationProperties() {
		try {
			propertyManager.saveApplicationProperties();
		} catch (Exception e) {
			ExceptionHandler.error(e);
			return false;
		}
		return true;
	}

	/** Saves the project. If exception is thrown, project wasn't successfully saved. */
	public void saveProject() throws TransformerException {
		saveProject(false);
	}

	private void saveProject(boolean rescue) throws TransformerException {
		if (applicationData == null) {
			return;
		}
		Project project = applicationData.getCurrentProject();
		if (!project.getProjectSaveDirectory().exists()) {
			boolean made = project.getProjectSaveDirectory().mkdirs();
			if (!made) {
				throw new TransformerException("Couldn't made project parent directories");
			}
		}
		new ProjectSaveXmlWriter(
				project,
				ArmaDialogCreator.getCanvasView().getMainControlsTreeStructure(),
				ArmaDialogCreator.getCanvasView().getBackgroundControlsTreeStructure()
		).write(rescue ? new File(project.getProjectSaveFile().getAbsoluteFile() + ".rescue") : null);
	}

	public void saveGlobalResources() {
		try {
			ResourceRegistryXmlWriter.WorkspaceResourceRegistryXmlWriter.writeAndClose(workspace);
		} catch (TransformerException e) {
			ExceptionHandler.error(e);
		}
	}

	/**
	 Will save everything only if user agrees to save.

	 @return <ul>
	 <li>If user responds "yes" to save, returns true</li>
	 <li>If user responds "no" to save, returns true</li>
	 <li>If user cancels the save process, returns false</li>
	 </ul>
	 */
	public boolean askSaveAll() {
		SaveProjectDialog dialog = new SaveProjectDialog();
		dialog.show();
		if (dialog.wasCancelled()) {
			return false;
		}
		if (dialog.saveProgress()) {
			saveGlobalResources();
			try {
				saveProject();
			} catch (TransformerException e) {
				ExceptionHandler.error(e);
			}
		}

		return true;
	}

	/**
	 Save the project, but in a file that isn't the project file.
	 This should be called when the application is exiting unexpectedly and the data should be saved.
	 This will not invoke {@link #saveApplicationProperties()}.

	 @return true if saved, false if it couldn't be saved
	 */
	public boolean rescueSave() {
		try {
			saveProject(true);
		} catch (TransformerException e) {
			return false;
		}
		return true;
	}

	public boolean isInitializing() {
		return waitForInitialize.getCount() > 0;
	}

	private static class SaveProjectDialog extends StageDialog<VBox> {

		private boolean saveProgress = false;

		public SaveProjectDialog() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);
			ResourceBundle bundle = Lang.ApplicationBundle();
			setTitle(bundle.getString("Popups.SaveProject.popup_title"));

			myRootElement.getChildren().add(new Label(bundle.getString("Popups.SaveProject.message")));
			btnOk.setText(bundle.getString("Confirmation.yes"));
			Button btnNo = new Button(bundle.getString("Confirmation.no"));
			btnNo.setOnAction(event -> {
				close();
			});
			btnNo.setPrefWidth(btnOk.getPrefWidth());
			getFooter().getRightContainer().getChildren().add(1, btnNo);

			myStage.setResizable(false);
		}

		@Override
		protected void ok() {
			saveProgress = true;
			super.ok();
		}

		/** @return true if the user responded yes for saving, false if no progress should be saved */
		public boolean saveProgress() {
			return saveProgress;
		}
	}
}
