/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectSaveXmlWriter;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.ResourceRegistryXmlWriter;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StageDialog;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.lang.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 @author Kayler
 Manages save data
 Created on 05/26/2016. */
public class ApplicationDataManager {
	private final ApplicationPropertyManager propertyManager = new ApplicationPropertyManager();
	private ApplicationData applicationData;

	public static ApplicationDataManager getInstance(){
		return ArmaDialogCreator.getApplicationDataManager();
	}

	public void setApplicationData(@NotNull ApplicationData applicationData) {
		this.applicationData = applicationData;
		ChangeRegistrars changeRegistrars = new ChangeRegistrars(applicationData);
	}

	/**Calls {@code getApplicationData().getCurrentProject()}*/
	@NotNull
	public Project getCurrentProject(){
		return getApplicationData().getCurrentProject();
	}

	@NotNull
	public ApplicationData getApplicationData() {
		if (applicationData == null) {
			throw new IllegalStateException("application data should be set before accessed");
		}
		return applicationData;
	}
	
	/** Set the application save data directory to a new one. Automatically updates application properties. */
	public void setAppSaveDataLocation(@NotNull File file) {
		propertyManager.setAppSaveDataLocation(file);
	}
	
	/** Set the arma 3 tools directory to a new one (can be null). Automatically updates application properties. */
	public void setArma3ToolsLocation(@Nullable File file) {
		propertyManager.setArma3ToolsLocation(file);
	}
	
	/** Get where application save files should be saved to. */
	@NotNull
	public File getAppSaveDataDirectory() {
		return propertyManager.getAppSaveDataDirectory();
	}
	
	/** Get the directory for where Arma 3 tools is saved. If the directory hasn't been set or doesn't exist or the file that is set isn't a directory, will return null. */
	@Nullable
	public File getArma3ToolsDirectory() {
		return propertyManager.getArma3ToolsDirectory();
	}
	
	/** Get application properties */
	public @NotNull DataContext getApplicationProperties() {
		return propertyManager.getApplicationProperties();
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
			project.getProjectSaveDirectory().mkdir();
		}
		File projectSaveXml = project.getFileForName("project.xml");
		new ProjectSaveXmlWriter(project, ArmaDialogCreator.getCanvasView().getMainControlsTreeStructure(), ArmaDialogCreator.getCanvasView().getBackgroundControlsTreeStructure(),
				projectSaveXml).write();
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

	public void saveGlobalResources(){
		try {
			new ResourceRegistryXmlWriter.GlobalResourceRegistryXmlWriter().writeAndClose();
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	public void applicationExitSave() {
		SaveProjectDialog popup = new SaveProjectDialog();
		popup.show();
		boolean saveProgress = popup.isSaveProgress();
		if (saveProgress) {
			try {
				saveProject();
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}
		}
		saveGlobalResources();

	}

	private static class SaveProjectDialog extends StageDialog<VBox>{

		private boolean saveProgress = false;

		public SaveProjectDialog() {
			super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.Popups.SaveProject.POPUP_TITLE, true, true, false);
			myRootElement.getChildren().add(new Label(Lang.Popups.SaveProject.MESSAGE));
			btnOk.setText(Lang.Confirmation.YES);
			btnCancel.setText(Lang.Confirmation.NO);
		}

		@Override
		protected void ok() {
			saveProgress = true;
			super.ok();
		}

		public boolean isSaveProgress() {
			return saveProgress;
		}
	}
}
