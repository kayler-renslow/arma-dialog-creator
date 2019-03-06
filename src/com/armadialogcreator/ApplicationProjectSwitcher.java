package com.armadialogcreator;

import com.armadialogcreator.application.ADCDataLoadException;
import com.armadialogcreator.application.ADCDataWriteException;
import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.gui.main.ADCMainWindow;
import com.armadialogcreator.gui.main.AskSaveProjectDialog;
import com.armadialogcreator.gui.main.popup.projectInit.ADCProjectInitWindow;
import com.armadialogcreator.gui.main.popup.projectInit.CouldNotLoadFileDialog;
import com.armadialogcreator.gui.main.popup.projectInit.ProjectInit;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 3/3/19 */
public class ApplicationProjectSwitcher {
	public static final ApplicationProjectSwitcher instance = new ApplicationProjectSwitcher();

	private enum State {
		/** The user is selecting a project and workspace */
		ProjectSelection,
		/** A project is actively being loaded */
		LoadProject,
		/** A project is being imported and converted to ADC's save format */
		ImportingProject,
		/** A project was imported, converted to ADC's save format, and is now being loaded */
		LoadImportedProject,
		/** Loading a pre-2019.1.0 version save format (project.xml rather than project.adc) */
		LoadLegacyProject,
		/** The user is now in a project and making dialogs or whatever */
		ProjectActive
	}

	@NotNull
	private State currentState = State.ProjectSelection;
	private boolean loadedAtLeastOneProject = false;

	private ApplicationProjectSwitcher() {
	}

	void firstProjectSelection() {
		loadNewProject(false);
	}

	public void loadNewProject(boolean askSave) {
		ApplicationManager applicationManager = ApplicationManager.instance;
		ADCMainWindow mainWindow = ArmaDialogCreator.getMainWindow();
		switch (currentState) {
			case ProjectActive: {
				if (askSave) {
					AskSaveProjectDialog dialog = new AskSaveProjectDialog();
					dialog.show();
					if (dialog.wasCancelled()) {
						return;
					}
					if (dialog.saveProgress()) {
						try {
							applicationManager.saveProject();
						} catch (ADCDataWriteException e) {
							throw new RuntimeException(e);
						}
					}
				}
				mainWindow.hide();
				break;
			}
			case ProjectSelection: {
				break;
			}
			default: {
				throw new IllegalStateException();
			}
		}

		currentState = State.ProjectSelection;

		ADCProjectInitWindow initWindow = new ADCProjectInitWindow();
		initWindow.show();
		ProjectInit projectInit = initWindow.getProjectInit();
		switch (projectInit.getType()) {
			case New: // fall
			case Open: {
				try {
					applicationManager.loadWorkspace(projectInit.getWorkspace());
				} catch (ADCDataLoadException e) {
					new CouldNotLoadFileDialog(e, ApplicationManager.getWorkspaceDataSaveFile(projectInit.getWorkspace()))
							.show();
				}
				try {
					applicationManager.loadProject(projectInit.getDescriptor());
				} catch (ADCDataLoadException e) {
					new CouldNotLoadFileDialog(e, projectInit.getDescriptor().getProjectSaveFile())
							.show();
				}
				initWindow.hide();
				if (!loadedAtLeastOneProject) {
					loadedAtLeastOneProject = true;
					mainWindow.initialize();
				}
				mainWindow.show();
				currentState = State.ProjectActive;
				break;
			}
			case Import: {
				break;
			}
			case OpenLegacyProject: {
				break;
			}
			default: {
				throw new IllegalStateException();
			}
		}

	}
}
