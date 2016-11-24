/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */


package com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.projectInit;

import com.kaylerrenslow.armaDialogCreator.data.*;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.XmlParseException;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.FileChooserPane;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.ADCWindow;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.popup.ADCMustRestartDialog;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.WizardStageDialog;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.WizardStep;
import com.kaylerrenslow.armaDialogCreator.main.*;
import com.kaylerrenslow.armaDialogCreator.util.BrowserUtil;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.Locale;

/**
 Used for setting up the initial {@link Project}. This window is shown before {@link ADCWindow}.

 @author Kayler
 @since 07/19/2016. */
public class ADCProjectInitWindow extends WizardStageDialog {

	private static final double STAGE_WIDTH = 720d;
	private static final double STAGE_HEIGHT = 400d;

	private final ProjectInitWizardStep initWizardStep;
	private final WorkspaceSelectionStep workspaceSelectionStep = new WorkspaceSelectionStep();

	public ADCProjectInitWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), Lang.ApplicationBundle().getString("ProjectInitWindow.window_title"), true);

		initWizardStep = new ProjectInitWizardStep(this);

		addWizardStep(workspaceSelectionStep);
		addWizardStep(initWizardStep);

		btnCancel.setText(Lang.ApplicationBundle().getString("Popups.btn_exit"));
		myStage.setWidth(720d);
		myStage.setHeight(400d);
		myStage.setResizable(false);
	}


	public ProjectInit getProjectInit() {
		return initWizardStep.getProjectInit();
	}

	@NotNull
	public File getWorkspaceDirectory() {
		return workspaceSelectionStep.getWorkspaceDirectory();
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
		private File workspaceDirectory;

		public WorkspaceSelectionStep() {
			super(new VBox(5));

			workspaceDirectory = ApplicationProperty.LAST_WORKSPACE.get(ApplicationDataManager.getApplicationProperties());
			if (workspaceDirectory == null) {
				workspaceDirectory = Workspace.DEFAULT_WORKSPACE_DIRECTORY;
			}

			content.setAlignment(Pos.CENTER);
			content.setMaxWidth(STAGE_WIDTH / 4 * 3);
			final Label lblChooseWorkspace = new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.choose_workspace"));
			lblChooseWorkspace.setFont(Font.font(20));
			content.getChildren().add(lblChooseWorkspace);

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

		@NotNull
		public File getWorkspaceDirectory() {
			return workspaceDirectory;
		}

		@Override
		protected boolean stepIsComplete() {
			return true;
		}
	}

	private static class ProjectInitWizardStep extends WizardStep<VBox> {
		private final LinkedList<ProjectInitTab> initTabs = new LinkedList<>();
		private final TabPane tabPane = new TabPane();
		private final ADCProjectInitWindow projectInitWindow;

		public ProjectInitWizardStep(@NotNull ADCProjectInitWindow projectInitWindow) {
			super(new VBox(5));
			this.projectInitWindow = projectInitWindow;

			//header
			final Label lblProjectSetup = new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.project_setup"));
			lblProjectSetup.setFont(Font.font(18d));

			initTabPane();

			getContent().getChildren().addAll(lblProjectSetup, new Separator(Orientation.HORIZONTAL), tabPane);

			final ComboBox<LocaleDescriptor> comboBoxLanguage = new ComboBox<>();

			for (Locale locale : Lang.SUPPORTED_LOCALES) {
				comboBoxLanguage.getItems().add(new LocaleDescriptor(locale));
			}
			comboBoxLanguage.getSelectionModel().select(new LocaleDescriptor(ArmaDialogCreator.getCurrentLocale()));
			comboBoxLanguage.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LocaleDescriptor>() {
				@Override
				public void changed(ObservableValue<? extends LocaleDescriptor> observable, LocaleDescriptor oldValue, LocaleDescriptor newValue) {
					boolean restart = ADCMustRestartDialog.getResponse();
					if (restart) {
						ArmaDialogCreator.setLocale(newValue.getLocale());
						ArmaDialogCreator.restartApplication(false);
					} else {
						comboBoxLanguage.getSelectionModel().selectedItemProperty().removeListener(this);
						comboBoxLanguage.getSelectionModel().select(oldValue);
						comboBoxLanguage.getSelectionModel().selectedItemProperty().addListener(this);
					}
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
			initTabs.add(new NewProjectTab());
			initTabs.add(new TabOpen(projectInitWindow));
			//				initTabs.add(new ImportTab(this));

			final ValueListener<Boolean> enabledListener = new ValueListener<Boolean>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean enabled) {
					projectInitWindow.getFooter().getBtnOk().setDisable(!enabled);
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
					stepPresented();
				}
			});
		}

		private static VBox getTabVbox(double spacing) {
			VBox vBox = new VBox(spacing);
			vBox.setPadding(new Insets(10));
			vBox.setMinHeight(200d);
			return vBox;
		}

		@Override
		protected void stepPresented() {
			projectInitWindow.getFooter().getBtnOk().setDisable(!stepIsComplete());
		}

		@Override
		protected boolean stepIsComplete() {
			for (ProjectInitTab initTab : initTabs) {
				if (initTab.getTab() == tabPane.getSelectionModel().getSelectedItem()) {
					return initTab.projectConfigSet.getValue();
				}
			}
			throw new IllegalStateException("unknown tab is selected");
		}


		private abstract class ProjectInitTab {
			protected final ValueObserver<Boolean> projectConfigSet = new ValueObserver<>(true);

			abstract ProjectInit getResult();

			abstract Tab getTab();

		}

		public class NewProjectTab extends ProjectInitTab {

			private final Tab tabNew = new Tab(Lang.ApplicationBundle().getString("ProjectInitWindow.tab_new"));

			/** TextField used for getting project name in new tab */
			private final TextField tfProjectName = new TextField();
			private final TextArea tfProjectDescription = new TextArea();

			public NewProjectTab() {
				tfProjectName.setPrefWidth(200d);
				tfProjectName.setPromptText("untitled");
				tfProjectDescription.setPrefRowCount(6);

				final VBox root = getTabVbox(10);

				final Label lblCreateNewProject = new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.new_project_title"));
				VBox.setMargin(lblCreateNewProject, new Insets(0, 0, 10, 0));
				final Label lblProjectName = new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.project_name"), tfProjectName);
				lblProjectName.setContentDisplay(ContentDisplay.RIGHT);
				final HBox hboxProjectDescription = new HBox(5, new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.new_project_description")), tfProjectDescription);

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

		public class TabOpen extends ProjectInitTab {

			private final Tab tabOpen = new Tab(Lang.ApplicationBundle().getString("ProjectInitWindow.tab_open"));
			private final ListView<ProjectInfo> lvKnownProjects = new ListView<>();
			private final ADCProjectInitWindow projectInitWindow;
			private LinkedList<ProjectXmlLoader.ProjectPreviewParseResult> parsedKnownProjects = new LinkedList<>();
			private ProjectXmlLoader.ProjectPreviewParseResult selectedParsedProject;

			public TabOpen(ADCProjectInitWindow projectInitWindow) {
				this.projectInitWindow = projectInitWindow;
				projectConfigSet.updateValue(false);
				final VBox root = getTabVbox(10d);
				tabOpen.setContent(root);
				final Label lblOpenProject = new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.open_project_title"));
				VBox.setMargin(lblOpenProject, new Insets(0d, 0d, 10d, 0d));

				final Button btnLocateProject = new Button(Lang.ApplicationBundle().getString("ProjectInitWindow.open_from_file"));
				btnLocateProject.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						FileChooser fc = new FileChooser();
						fc.setInitialDirectory(projectInitWindow.getWorkspaceDirectory());
						fc.setTitle(Lang.ApplicationBundle().getString("ProjectInitWindow.fc_locate_project_title"));
						fc.getExtensionFilters().add(ADCStatic.PROJECT_XML_FC_FILTER);

						File chosen = fc.showOpenDialog(ArmaDialogCreator.getPrimaryStage());
						if (chosen == null) {
							return;
						}
						ProjectXmlLoader.ProjectPreviewParseResult result;
						try {
							result = ProjectXmlLoader.previewParseProjectXmlFile(chosen);
						} catch (XmlParseException e) {
							return;
						}
						if (!lvKnownProjects.getItems().contains(result.getProjectInfo())) {
							parsedKnownProjects.add(result);
							lvKnownProjects.getItems().add(result.getProjectInfo());
						}
						lvKnownProjects.getSelectionModel().select(result.getProjectInfo());
						lvKnownProjects.requestFocus();
					}
				});

				root.getChildren().addAll(lblOpenProject, initKnownProjects(), new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.open_from_file_title")), btnLocateProject);

				lvKnownProjects.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProjectInfo>() {
					@Override
					public void changed(ObservableValue<? extends ProjectInfo> observable, ProjectInfo oldValue, ProjectInfo selected) {
						boolean matched = false;
						for (ProjectXmlLoader.ProjectPreviewParseResult result : parsedKnownProjects) {
							if (result.getProjectInfo().equals(selected)) {
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
			}

			private Node initKnownProjects() {
				fetchProjects();
				for (ProjectXmlLoader.ProjectPreviewParseResult result : parsedKnownProjects) {
					lvKnownProjects.getItems().add(result.getProjectInfo());
				}
				return new VBox(0, new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.detected_projects")), lvKnownProjects);
			}

			private void fetchProjects() {
				File[] files = projectInitWindow.getWorkspaceDirectory().listFiles();
				if (files != null) {
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
									ProjectXmlLoader.ProjectPreviewParseResult result = ProjectXmlLoader.previewParseProjectXmlFile(projectFile);
									parsedKnownProjects.add(result);
								} catch (XmlParseException e) {
									continue;
								}
							}
						}
					}
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

		}

		public class ImportTab extends ProjectInitTab {

			private final Tab tabImport = new Tab(Lang.ApplicationBundle().getString("ProjectInitWindow.tab_import"));

			public ImportTab(ADCProjectInitWindow adcProjectInitWindow) {
				tabImport.setUserData(Lang.ApplicationBundle().getString("ProjectInitWindow.import_project_ok"));
				final VBox root = getTabVbox(20);
				final Label lblOpenProject = new Label(Lang.ApplicationBundle().getString("ProjectInitWindow.import_project_title"));

				root.getChildren().addAll(lblOpenProject);

				tabImport.setContent(root);
			}

			@Override
			public ProjectInit getResult() {
				return null;
			}


			@Override
			public Tab getTab() {
				return tabImport;
			}
		}

	}

	public interface ProjectInit {

		class NewProject implements ProjectInit {
			private final String projectName;
			private final String projectDescription;

			public NewProject(@Nullable String projectName, @NotNull String projectDescription) {
				this.projectName = projectName;
				this.projectDescription = projectDescription;
			}

			@NotNull
			public String getProjectDescription() {
				return projectDescription;
			}

			@Nullable
			public String getProjectName() {
				return projectName;
			}
		}

		class OpenProject implements ProjectInit {
			private final ProjectXmlLoader.ProjectPreviewParseResult parseResult;

			public OpenProject(@NotNull ProjectXmlLoader.ProjectPreviewParseResult parseResult) {
				this.parseResult = parseResult;
			}

			@NotNull
			public ProjectXmlLoader.ProjectPreviewParseResult getParseResult() {
				return parseResult;
			}

			@NotNull
			public ProjectInfo getProjectXmlInfo() {
				return parseResult.getProjectInfo();
			}
		}

		class ImportProject implements ProjectInit {
			private final File descriptionExt;

			public ImportProject(@NotNull File descriptionExt) {
				this.descriptionExt = descriptionExt;
			}

			@NotNull
			public File getDescriptionExt() {
				return descriptionExt;
			}
		}
	}
}
