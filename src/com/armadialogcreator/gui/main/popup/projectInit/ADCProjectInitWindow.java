
package com.armadialogcreator.gui.main.popup.projectInit;

import com.armadialogcreator.ADCStatic;
import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.HelpUrls;
import com.armadialogcreator.LocaleDescriptor;
import com.armadialogcreator.application.ProjectDescriptor;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.data.olddata.ApplicationProperty;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.data.oldprojectloader.ProjectInit;
import com.armadialogcreator.gui.WizardStageDialog;
import com.armadialogcreator.gui.WizardStep;
import com.armadialogcreator.gui.fxcontrol.FileChooserPane;
import com.armadialogcreator.gui.main.ADCWindow;
import com.armadialogcreator.gui.main.BrowserUtil;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.ReadOnlyList;
import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import com.armadialogcreator.util.XmlParseException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 Used for setting up the initial {@link Project}. This window is shown before {@link ADCWindow}.

 @author Kayler
 @since 07/19/2016. */
public class ADCProjectInitWindow extends WizardStageDialog {

	private static final double STAGE_WIDTH = 720d;
	private static final double STAGE_HEIGHT = 400d;

	private final ProjectInitWizardStep initWizardStep;
	private final WorkspaceSelectionStep workspaceSelectionStep;

	private static final ResourceBundle bundle = Lang.getBundle("ProjectInitWindowBundle");

	private final ValueObserver<File> workspaceDirectoryObserver = new ValueObserver<>(null);

	public ADCProjectInitWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), bundle.getString("window_title"), true);

		workspaceSelectionStep = new WorkspaceSelectionStep(this);
		initWizardStep = new ProjectInitWizardStep(this);

		addWizardStep(workspaceSelectionStep);
		addWizardStep(initWizardStep);

		btnCancel.setText(Lang.ApplicationBundle().getString("Popups.btn_exit"));
		myStage.setWidth(STAGE_WIDTH);
		myStage.setHeight(STAGE_HEIGHT);
		myStage.setResizable(false);
	}

	@NotNull
	public ProjectInit getProjectInit() {
		return initWizardStep.getProjectInit();
	}

	@NotNull
	public File getWorkspaceDirectory() {
		return workspaceDirectoryObserver.getValue();
	}

	@Override
	protected void cancel() {
		System.exit(0);
	}

	@Override
	protected void help() {
		BrowserUtil.browse(HelpUrls.PROJECT_INIT_WINDOW);
	}

	@Override
	protected void onCloseRequest(WindowEvent event) {
		cancel();
	}

	private static class WorkspaceSelectionStep extends WizardStep<VBox> {
		private final ADCProjectInitWindow adcProjectInitWindow;
		private @NotNull File workspaceDirectory;

		public WorkspaceSelectionStep(@NotNull ADCProjectInitWindow adcProjectInitWindow) {
			super(new VBox(20));
			this.adcProjectInitWindow = adcProjectInitWindow;
			workspaceDirectory = ApplicationProperty.LAST_WORKSPACE.getValue();
			if (workspaceDirectory == null) {
				workspaceDirectory = Workspace.DEFAULT_WORKSPACE_DIRECTORY;
			}

			content.setAlignment(Pos.CENTER);
			content.setMaxWidth(STAGE_WIDTH / 4 * 3);
			final Label lblChooseWorkspace = new Label(bundle.getString("choose_workspace"));
			lblChooseWorkspace.setFont(Font.font(20));
			content.getChildren().add(new VBox(5, lblChooseWorkspace, new Label(bundle.getString("workspace_about"))));

			final FileChooserPane chooserPane = new FileChooserPane(
					ArmaDialogCreator.getPrimaryStage(), FileChooserPane.ChooserType.DIRECTORY,
					lblChooseWorkspace.getText(),
					workspaceDirectory
			);
			chooserPane.setChosenFile(workspaceDirectory);
			chooserPane.getChosenFileObserver().addListener(new ValueListener<File>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<File> observer, File oldValue, File newValue) {
					if (newValue != null) {
						workspaceDirectory = newValue;
					}
				}
			});
			content.getChildren().add(chooserPane);

		}

		@Override
		protected void stepLeft(boolean movingForward) {
			adcProjectInitWindow.workspaceDirectoryObserver.updateValue(workspaceDirectory);
		}
	}

	private static class ProjectInitWizardStep extends WizardStep<VBox> {
		private final LinkedList<ProjectInitTab> initTabs = new LinkedList<>();
		private final TabPane tabPane = new TabPane();
		private final ADCProjectInitWindow projectInitWindow;
		private OpenTab openTab;
		private NewProjectTab tabNew;
		private ImportTab tabImport;

		public ProjectInitWizardStep(@NotNull ADCProjectInitWindow projectInitWindow) {
			super(new VBox(5));
			this.projectInitWindow = projectInitWindow;

			stepIsCompleteProperty.set(false);

			//header
			final Label lblProjectSetup = new Label(bundle.getString("project_setup"));
			lblProjectSetup.setFont(Font.font(18d));

			initTabPane();

			getContent().getChildren().addAll(lblProjectSetup, new Separator(Orientation.HORIZONTAL), tabPane);

			final ComboBox<LocaleDescriptor> comboBoxLanguage = new ComboBox<>();

			for (Locale locale : Lang.SUPPORTED_LOCALES) {
				comboBoxLanguage.getItems().add(new LocaleDescriptor(locale));
			}
			comboBoxLanguage.getSelectionModel().select(new LocaleDescriptor(Locale.US));
			comboBoxLanguage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocaleDescriptor>() {
				@Override
				public void changed(ObservableValue<? extends LocaleDescriptor> observable, LocaleDescriptor oldValue, LocaleDescriptor newValue) {
					if (newValue == null) {
						return;
					}
					//					ApplicationProperty.LOCALE.put(ApplicationDataManager.getApplicationProperties(), newValue.getLocale());
					//					ApplicationDataManager.getInstance().saveApplicationProperties();
					//					boolean restart = ADCMustRestartDialog.getResponse();
					//					if (restart) {
					//						ArmaDialogCreator.restartApplication(false);
					//					}
				}
			});

		}


		public ProjectInit getProjectInit() {
			Tab selected = tabPane.getSelectionModel().getSelectedItem();
			for (ProjectInitWizardStep.ProjectInitTab initTab : initTabs) {
				if (initTab.getTab() == selected) {
					return initTab.getResult();
				}
			}
			throw new IllegalStateException("Should provide implementation for selected tab");
		}

		private void initTabPane() {
			tabNew = new NewProjectTab();
			initTabs.add(tabNew);
			openTab = new OpenTab(projectInitWindow);
			initTabs.add(openTab);
			tabImport = new ImportTab();
			initTabs.add(new ImportTab());

			ValueListener<Boolean> enabledListener = new ValueListener<Boolean>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean enabled) {
					stepIsCompleteProperty.set(enabled);
				}
			};
			for (ProjectInitTab initTab : initTabs) {
				tabPane.getTabs().add(initTab.getTab());
				initTab.getTab().setClosable(false);
				initTab.projectConfigSet.addListener(enabledListener);
			}
			tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
				@Override
				public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab selected) {
					updateStepIsCompleteProperty(selected);
				}
			});

			updateStepIsCompleteProperty(tabPane.getSelectionModel().getSelectedItem());
		}

		private void updateStepIsCompleteProperty(@NotNull Tab selected) {
			boolean found = false;
			for (ProjectInitTab initTab : initTabs) {
				if (initTab.getTab() == selected) {
					found = true;
					stepIsCompleteProperty.set(initTab.projectConfigSet.getValue());
					break;
				}
			}
			if (!found) {
				throw new IllegalStateException("didn't find init tab");
			}
		}

		private static VBox getTabVbox(double spacing) {
			VBox vBox = new VBox(spacing);
			vBox.setPadding(new Insets(10));
			vBox.setMinHeight(200d);
			return vBox;
		}

		@Override
		protected void stepPresented() {
			super.stepPresented();
			if (getPresentCount() == 1) {
				if (openTab.getParsedKnownProjects().size() > 0) {
					tabPane.getSelectionModel().select(openTab.getTab());
				}
			}
		}

		private abstract class ProjectInitTab {
			protected final ValueObserver<Boolean> projectConfigSet = new ValueObserver<>(true);

			abstract ProjectInit getResult();

			abstract Tab getTab();

		}

		public class NewProjectTab extends ProjectInitTab {

			private final Tab tabNew = new Tab(bundle.getString("tab_new"));

			/** TextField used for getting project name in new tab */
			private final TextField tfProjectName = new TextField();
			private final TextArea tfProjectDescription = new TextArea();

			public NewProjectTab() {
				tfProjectName.setPrefWidth(200d);
				tfProjectName.setPromptText("untitled");
				tfProjectDescription.setPrefRowCount(6);

				final VBox root = getTabVbox(10);

				final Label lblCreateNewProject = new Label(bundle.getString("new_project_title"));
				VBox.setMargin(lblCreateNewProject, new Insets(0, 0, 10, 0));
				final Label lblProjectName = new Label(bundle.getString("project_name"), tfProjectName);
				lblProjectName.setContentDisplay(ContentDisplay.RIGHT);
				final HBox hboxProjectDescription = new HBox(5, new Label(bundle.getString("new_project_description")), tfProjectDescription);

				root.getChildren().addAll(lblCreateNewProject, lblProjectName, hboxProjectDescription);

				tabNew.setContent(root);
			}

			@Override
			public ProjectInit getResult() {
				return new ProjectInit.NewProject(tfProjectName.getText(), tfProjectDescription.getText());
			}

			@Override
			public Tab getTab() {
				return tabNew;
			}
		}

		public class OpenTab extends ProjectInitTab {

			private final Tab tabOpen = new Tab(bundle.getString("tab_open"));
			private final ListView<ProjectDescriptor> lvKnownProjects = new ListView<>();
			private final ADCProjectInitWindow projectInitWindow;
			private LinkedList<ProjectXmlReader.ProjectPreviewParseResult> parsedKnownProjects = new LinkedList<>();
			private ProjectXmlReader.ProjectPreviewParseResult selectedParsedProject;
			private ReadOnlyList<ProjectXmlReader.ProjectPreviewParseResult> parsedKnownProjectsRo = new ReadOnlyList<>(parsedKnownProjects);

			public OpenTab(ADCProjectInitWindow projectInitWindow) {
				this.projectInitWindow = projectInitWindow;
				projectConfigSet.updateValue(false);
				final VBox root = getTabVbox(10d);
				tabOpen.setContent(root);
				final Label lblOpenProject = new Label(bundle.getString("open_project_title"));
				VBox.setMargin(lblOpenProject, new Insets(0d, 0d, 10d, 0d));

				final Button btnLocateProject = new Button(bundle.getString("open_from_file"));
				btnLocateProject.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						FileChooser fc = new FileChooser();
						fc.setInitialDirectory(projectInitWindow.getWorkspaceDirectory());
						fc.setTitle(bundle.getString("fc_locate_project_title"));
						fc.getExtensionFilters().add(ADCStatic.PROJECT_XML_FC_FILTER);

						File chosen = fc.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
						if (chosen == null) {
							return;
						}
						ProjectXmlReader.ProjectPreviewParseResult result;
						try {
							result = ProjectXmlReader.previewParseProjectXmlFile(chosen);
						} catch (XmlParseException e) {
							return;
						}
						if (!lvKnownProjects.getItems().contains(result.getProjectDescriptor())) {
							parsedKnownProjects.add(result);
							lvKnownProjects.getItems().add(result.getProjectDescriptor());
						}
						lvKnownProjects.getSelectionModel().select(result.getProjectDescriptor());
						lvKnownProjects.requestFocus();
					}
				});

				root.getChildren().addAll(
						lblOpenProject,
						new VBox(0, new Label(bundle.getString("detected_projects")), lvKnownProjects),
						new Label(bundle.getString("open_from_file_title")),
						btnLocateProject
				);

				lvKnownProjects.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProjectDescriptor>() {
					@Override
					public void changed(ObservableValue<? extends ProjectDescriptor> observable, ProjectDescriptor oldValue, ProjectDescriptor selected) {
						boolean matched = false;
						for (ProjectXmlReader.ProjectPreviewParseResult result : parsedKnownProjects) {
							if (result.getProjectDescriptor().equals(selected)) {
								selectedParsedProject = result;
								matched = true;
								break;
							}
						}
						if (!matched) {
							throw new IllegalStateException("parsed project should have been matched");
						}
						projectConfigSet.updateValue(selected != null);
					}
				});
				projectInitWindow.workspaceDirectoryObserver.addListener((observer, oldValue, newValue) -> {
					reloadKnownProjects();
				});
			}

			private void reloadKnownProjects() {
				lvKnownProjects.getItems().clear();
				parsedKnownProjects.clear();

				File[] files = projectInitWindow.getWorkspaceDirectory().listFiles();
				if (files == null) {
					return;
				}
				for (File f : files) {
					if (f.isDirectory()) {
						File[] projectFiles = f.listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.equals(Project.PROJECT_SAVE_FILE_NAME);
							}
						});
						if (projectFiles == null) {
							continue;
						}
						for (File projectFile : projectFiles) {
							try {
								ProjectXmlReader.ProjectPreviewParseResult result = ProjectXmlReader.previewParseProjectXmlFile(projectFile);
								parsedKnownProjects.add(result);
							} catch (XmlParseException e) {
								e.printStackTrace();
								continue;
							}
						}
					}

				}

				for (ProjectXmlReader.ProjectPreviewParseResult result : parsedKnownProjects) {
					lvKnownProjects.getItems().add(result.getProjectDescriptor());
				}
			}


			@Override
			public ProjectInit getResult() {
				return new ProjectInit.OpenProject(selectedParsedProject);
			}

			@Override
			public Tab getTab() {
				return tabOpen;
			}

			@NotNull
			public ReadOnlyList<ProjectXmlReader.ProjectPreviewParseResult> getParsedKnownProjects() {
				return parsedKnownProjectsRo;
			}
		}

		public class ImportTab extends ProjectInitTab {

			private final Tab tabImport = new Tab(bundle.getString("tab_import"));
			private ProjectInit projectInit;

			public ImportTab() {
				VBox root = getTabVbox(20);
				Label lblImportProject = new Label(bundle.getString("import_project_title"));
				Label lblLocateDesc = new Label(bundle.getString("locate_description_ext_tip"));
				lblLocateDesc.setWrapText(true);

				Button btnLocate = new Button(bundle.getString("locate_description_ext"));
				btnLocate.setOnAction(event -> {
					FileChooser fc = new FileChooser();
					fc.getExtensionFilters().add(ADCStatic.DESCIRPTION_EXT);
					File descExt = fc.showOpenDialog(projectInitWindow.myStage);
					if (descExt == null) {
						return;
					}
					ImportDialogsDialog d = new ImportDialogsDialog(projectInitWindow, projectInitWindow.getWorkspaceDirectory(), descExt);
					d.show();
					if (d.wasCancelled()) {
						return;
					}
					projectInit = d.getProjectInit();
					projectInitWindow.forceOkProperty.set(true);
					projectInitWindow.ok();

				});
				ScrollPane scrollPaneDesc = new ScrollPane(lblLocateDesc);
				scrollPaneDesc.setFitToWidth(true);
				root.getChildren().addAll(
						lblImportProject,
						new Label(bundle.getString("locate_description_ext_pretip")),
						scrollPaneDesc,
						btnLocate
				);

				tabImport.setContent(root);

				projectConfigSet.updateValue(false);
			}


			@Override
			public ProjectInit getResult() {
				return projectInit;
			}


			@Override
			public Tab getTab() {
				return tabImport;
			}
		}

	}

}
