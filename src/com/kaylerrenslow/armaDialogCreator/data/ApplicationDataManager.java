package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.data.xml.ResourceRegistryXmlWriter;
import com.kaylerrenslow.armaDialogCreator.gui.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 Manages save data

 @author Kayler
 @since 05/26/2016. */
public class ApplicationDataManager {
	private final ApplicationPropertyManager propertyManager = new ApplicationPropertyManager();
	private Workspace workspace;
	private ApplicationData applicationData;

	public void loadWorkspace(@NotNull File workspaceDir) {
		if(!workspaceDir.exists()){
			workspaceDir.mkdirs();
		}
		workspace = new Workspace(workspaceDir);
		ApplicationProperty.LAST_WORKSPACE.put(ApplicationDataManager.getApplicationProperties(), workspaceDir);
		saveApplicationProperties();
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

	public void initializeApplicationData() {
		this.applicationData = new ApplicationData();
	}

	public void initializeChangeRegistrars() {
		ChangeRegistrars changeRegistrars = new ChangeRegistrars(applicationData);
	}

	/** Calls {@code getApplicationData().getCurrentProject()} */
	@NotNull
	public Project getCurrentProject() {
		if (applicationData == null) {
			throw new IllegalStateException("can't get current project when ApplicationData hasn't been initialized");
		}
		return getApplicationData().getCurrentProject();
	}

	@NotNull
	public ApplicationData getApplicationData() {
		if (applicationData == null) {
			throw new IllegalStateException("application data should be set before accessed");
		}
		return applicationData;
	}

	/** Set the arma 3 tools directory to a new one (can be null). Automatically updates application properties. */
	public void setArma3ToolsLocation(@Nullable File file) {
		propertyManager.setArma3ToolsLocation(file);
	}

	/** Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null. */
	@Nullable
	public File getArma3ToolsDirectory() {
		return propertyManager.getArma3ToolsDirectory();
	}

	/** Saves the application properties. Returns true if the application properties were saved successfully, false if they weren't */
	public boolean saveApplicationProperties() {
		try {
			propertyManager.saveApplicationProperties();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}

	/** Saves the project. If exception is thrown, project wasn't successfully saved. */
	public void saveProject() throws IOException {
		if (applicationData == null) {
			return;
		}
		Project project = applicationData.getCurrentProject();
		if (!project.getProjectSaveDirectory().exists()) {
			project.getProjectSaveDirectory().mkdirs();
		}
		new ProjectSaveXmlWriter(
				project,
				ArmaDialogCreator.getCanvasView().getMainControlsTreeStructure(),
				ArmaDialogCreator.getCanvasView().getBackgroundControlsTreeStructure()
		).write();
	}

	/**
	 This should be called when the application is exiting unexpectedly and the data should be saved.

	 @return true if the data could be successfully saved, false if it couldn't
	 */
	public boolean forceSave() {
		saveApplicationProperties();
		try {
			saveProject();
		} catch (IOException e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}

	public void saveGlobalResources() {
		try {
			new ResourceRegistryXmlWriter.WorkspaceResourceRegistryXmlWriter().writeAndClose();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	/** Will save everything only if user agrees to save (however, global resources will always be saved no matter the response) */
	public void askSaveAll() {
		SaveProjectDialog popup = new SaveProjectDialog();
		popup.show();
		boolean saveProgress = popup.isSaveProgress();
		if (saveProgress) {
			try {
				saveProject();
			} catch (IOException e) {
				ExceptionHandler.error(e);
			}
		}
		saveGlobalResources();
	}

	/** Get an {@link ApplicationProperty} from {@link #getApplicationProperties()} */
	public Object getApplicationProperty(@NotNull ApplicationProperty property) {
		return property.get(propertyManager.getApplicationProperties());
	}

	@NotNull
	public Workspace getWorkspace() {
		return workspace;
	}

	private static class SaveProjectDialog extends StageDialog<VBox> {

		private boolean saveProgress = false;

		public SaveProjectDialog() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), null, true, true, false);
			ResourceBundle bundle = Lang.ApplicationBundle();
			setTitle(bundle.getString("Popups.SaveProject.popup_title"));

			myRootElement.getChildren().add(new Label(bundle.getString("Popups.SaveProject.message")));
			btnOk.setText(bundle.getString("Confirmation.yes"));
			btnCancel.setText(bundle.getString("Confirmation.no"));

			myStage.setResizable(false);
		}

		@Override
		protected void ok() {
			saveProgress = true;
			super.ok();
		}

		/** Returns true if the user responded yes for saving, false if no progress should be saved */
		public boolean isSaveProgress() {
			return saveProgress;
		}
	}
}
