package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.ParseError;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader;
import com.kaylerrenslow.armaDialogCreator.data.io.xml.XmlParseException;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;

/**
 @author Kayler
 Used for setting up the initial {@link Project}. This window is shown before {@link ADCWindow}.
 Created on 07/19/2016. */
public class ADCProjectInitWindow extends StagePopup<VBox> {
	private final LinkedList<ProjectInitTab> initTabs = new LinkedList<>();
	private final TabPane tabPane = new TabPane();
	
	public ADCProjectInitWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ProjectInitWindow.WINDOW_TITLE);
		myRootElement.setPadding(new Insets(10));
		
		//header
		final Label lblProjectSetup = new Label(Lang.ProjectInitWindow.PROJECT_SETUP);
		lblProjectSetup.setFont(Font.font(18d));
		
		initTabPane();
		
		myRootElement.getChildren().addAll(lblProjectSetup, new Separator(Orientation.HORIZONTAL), tabPane);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(false, true, false));
		
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.setWidth(720d);
		myStage.setHeight(400d);
		myStage.setResizable(false);
		
		this.btnOk.setPrefWidth(130d);
	}
	
	private void initTabPane() {
		initTabs.add(new NewProjectTab());
		initTabs.add(new TabOpen());
		//		initTabs.add(new ImportTab());
		
		final ValueListener<Boolean> enabledListener = new ValueListener<Boolean>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean enabled) {
				getOkButton().setDisable(!enabled);
			}
		};
		for (ProjectInitTab initTab : initTabs) {
			tabPane.getTabs().add(initTab.getTab());
			initTab.getTab().setClosable(false);
			initTab.btnOkEnabledObserver.addValueListener(enabledListener);
			
			tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
				@Override
				public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab selectedTab) {
					for (ProjectInitTab initTab : initTabs) {
						if (initTab.getTab() == selectedTab) {
							getOkButton().setText(initTab.getOkBtnLabel());
							getOkButton().setDisable(!initTab.btnOkEnabledObserver.getValue());
							return;
						}
					}
					throw new IllegalStateException("tab should have been matched");
				}
			});
			
		}
	}
	
	@Override
	protected void ok() {
		Tab selected = tabPane.getSelectionModel().getSelectedItem();
		for (ProjectInitTab initTab : initTabs) {
			if (initTab.getTab() == selected) {
				initTab.prepareProject();
				break;
			}
		}
		super.ok();
	}
	
	private Button getOkButton() {
		return this.btnOk;
	}
	
	private static VBox getTabVbox(double spacing) {
		VBox vBox = new VBox(spacing);
		vBox.setPadding(new Insets(10));
		vBox.setMinHeight(200d);
		return vBox;
	}
	
	@Override
	protected void onCloseRequest(WindowEvent event) {
		System.exit(0);
	}
	
	public ProjectInit getProjectInit() {
		Tab selected = tabPane.getSelectionModel().getSelectedItem();
		for (ProjectInitTab initTab : initTabs) {
			if (initTab.getTab() == selected) {
				return initTab.getResult();
			}
		}
		throw new IllegalStateException("Should provide implementation for selected tab");
	}
	
	private abstract class ProjectInitTab {
		protected final ValueObserver<Boolean> btnOkEnabledObserver = new ValueObserver<>(true);
		
		abstract ProjectInit getResult();
		
		abstract String getOkBtnLabel();
		
		abstract Tab getTab();
		
		void prepareProject(){}
	}
	
	public class NewProjectTab extends ProjectInitTab {
		
		private final Tab tabNew = new Tab(Lang.ProjectInitWindow.TAB_NEW);
		
		/** TextField used for getting project name in new tab */
		private final TextField tfProjectName;
		private final TextField tfProjectDescription = new TextField();
		
		public NewProjectTab() {
			final VBox root = getTabVbox(10);
			final Label lblCreateNewProject = new Label(Lang.ProjectInitWindow.NEW_PROJECT_TITLE);
			VBox.setMargin(lblCreateNewProject, new Insets(0, 0, 10, 0));
			tfProjectName = new TextField();
			tfProjectName.setPrefWidth(200d);
			final Label lblProjectName = new Label(Lang.ProjectInitWindow.PROJECT_NAME, tfProjectName);
			lblProjectName.setContentDisplay(ContentDisplay.RIGHT);
			final Label lblProjectDescription = new Label(Lang.ProjectInitWindow.NEW_PROJECT_DESCRIPTION, tfProjectDescription);
			lblProjectDescription.setContentDisplay(ContentDisplay.RIGHT);
			tfProjectDescription.setPrefWidth(250d);
			
			root.getChildren().addAll(lblCreateNewProject, lblProjectName, lblProjectDescription);
			
			tabNew.setContent(root);
		}
		
		@Override
		public ProjectInit getResult() {
			return new ProjectInit.NewProject(tfProjectName.getText(), tfProjectDescription.getText());
		}
		
		@Override
		public String getOkBtnLabel() {
			return Lang.ProjectInitWindow.NEW_PROJECT_OK;
		}
		
		@Override
		public Tab getTab() {
			return tabNew;
		}
	}
	
	public class TabOpen extends ProjectInitTab {
		
		private final Tab tabOpen = new Tab(Lang.ProjectInitWindow.TAB_OPEN);
		private final ListView<Project> lvKnownProjects = new ListView<>();
		private LinkedList<ProjectXmlLoader.ProjectParseResult> parsedKnownProjects = new LinkedList<>();
		private Project selectedProject;
		
		public TabOpen() {
			btnOkEnabledObserver.updateValue(false);
			final VBox root = getTabVbox(10d);
			tabOpen.setContent(root);
			final Label lblOpenProject = new Label(Lang.ProjectInitWindow.OPEN_PROJECT_TITLE);
			VBox.setMargin(lblOpenProject, new Insets(0d, 0d, 10d, 0d));
			
			final Button btnLocateProject = new Button(Lang.ProjectInitWindow.OPEN_FROM_FILE);
			
			root.getChildren().addAll(lblOpenProject, initKnownProjects(), new Label(Lang.ProjectInitWindow.OPEN_FROM_FILE_TITLE), btnLocateProject);
			
			lvKnownProjects.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Project>() {
				@Override
				public void changed(ObservableValue<? extends Project> observable, Project oldValue, Project selected) {
					selectedProject = selected;
					btnOkEnabledObserver.updateValue(selected != null);
				}
			});
		}
		
		private Node initKnownProjects() {
			fetchProjects(parsedKnownProjects);
			if (parsedKnownProjects.size() == 0) {
				return new Label(Lang.ProjectInitWindow.NO_DETECTED_PROJECTS);
			} else {
				for (ProjectXmlLoader.ProjectParseResult result : parsedKnownProjects) {
					lvKnownProjects.getItems().add(result.getProject());
				}
				return new VBox(0, new Label(Lang.ProjectInitWindow.DETECTED_PROJECTS), lvKnownProjects);
			}
		}
		
		private void fetchProjects(LinkedList<ProjectXmlLoader.ProjectParseResult> knownProjects) {
			File[] files = ArmaDialogCreator.getApplicationDataManager().getAppSaveDataDirectory().listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						File[] projectFiles = f.listFiles(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.equals("project.xml");
							}
						});
						if (projectFiles == null) {
							continue;
						}
						for (File projectFile : projectFiles) {
							try {
								ProjectXmlLoader.ProjectParseResult result = ProjectXmlLoader.parse(ArmaDialogCreator.getApplicationData(), projectFile);
								knownProjects.add(result);
							} catch (XmlParseException e) {
								continue;
							}
						}
					}
				}
			}
		}
		
		@Override
		void prepareProject() {
			for (ProjectXmlLoader.ProjectParseResult parseResult : parsedKnownProjects) {
				if (parseResult.getProject() == selectedProject) {
					if (parseResult.getErrors().size() > 0) {
						new ProjectImproperResultPopup(parseResult).showAndWait();
					}
				}
			}
		}
		
		@Override
		public ProjectInit getResult() {
			return new ProjectInit.OpenProject(selectedProject);
		}
		
		@Override
		public String getOkBtnLabel() {
			return Lang.ProjectInitWindow.OPEN_PROJECT_OK;
		}
		
		@Override
		public Tab getTab() {
			return tabOpen;
		}
		
		private class ProjectImproperResultPopup extends StagePopup<ScrollPane> {
			
			public ProjectImproperResultPopup(ProjectXmlLoader.ProjectParseResult result) {
				super(ArmaDialogCreator.getPrimaryStage(), new ScrollPane(new VBox(15)), Lang.ProjectInitWindow.ProjectResultErrorPopup.POPUP_TITLE);
				myRootElement.setFitToWidth(true);
				myRootElement.setFitToHeight(true);
				VBox root = (VBox) myRootElement.getContent();
				root.getChildren().add(new Label(Lang.ProjectInitWindow.ProjectResultErrorPopup.ERRORS_TITLE));
				root.getChildren().add(new Separator(Orientation.HORIZONTAL));
				for (ParseError error : result.getErrors()) {
					VBox vbErrorMsg = new VBox(5);
					vbErrorMsg.getChildren().addAll(
							getLabel(Lang.ProjectInitWindow.ProjectResultErrorPopup.ERROR_MESSAGE + " " + error.getMessage(), null),
							getLabel(Lang.ProjectInitWindow.ProjectResultErrorPopup.RECOVERED, getCheckbox("", error.recovered()))
					);
					if (error.recovered()) {
						vbErrorMsg.getChildren().add(getLabel(Lang.ProjectInitWindow.ProjectResultErrorPopup.RECOVER_MESSAGE + " " + error.getRecoverMessage(), null));
					}
					
					root.getChildren().add(vbErrorMsg);
				}
				root.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(false, true, false));
				
				myRootElement.setPadding(new Insets(10d));
				myStage.setWidth(340d);
			}
			
			private Label getLabel(String text, Node graphic) {
				Label label = new Label(text, graphic);
				label.setContentDisplay(ContentDisplay.RIGHT);
				return label;
			}
			
			private CheckBox getCheckbox(String text, boolean checked) {
				CheckBox checkBox = new CheckBox(text);
				checkBox.setSelected(checked);
				checkBox.setDisable(true);
				return checkBox;
			}
		}
		
	}
	
	public class ImportTab extends ProjectInitTab {
		
		private final Tab tabImport = new Tab(Lang.ProjectInitWindow.TAB_IMPORT);
		
		public ImportTab() {
			tabImport.setUserData(Lang.ProjectInitWindow.IMPORT_PROJECT_OK);
			final VBox root = getTabVbox(20);
			final Label lblOpenProject = new Label(Lang.ProjectInitWindow.IMPORT_PROJECT_TITLE);
			
			root.getChildren().addAll(lblOpenProject);
			
			tabImport.setContent(root);
		}
		
		@Override
		public ProjectInit getResult() {
			return null;
		}
		
		@Override
		public String getOkBtnLabel() {
			return Lang.ProjectInitWindow.IMPORT_PROJECT_OK;
		}
		
		@Override
		public Tab getTab() {
			return tabImport;
		}
	}
	
	public interface ProjectInit {
		
		class NewProject implements ProjectInit {
			private final String projectName;
			private final String projectDescription;
			
			public NewProject(String projectName, String projectDescription) {
				this.projectName = projectName;
				this.projectDescription = projectDescription;
			}
			
			public String getProjectDescription() {
				return projectDescription;
			}
			
			public String getProjectName() {
				return projectName;
			}
		}
		
		class OpenProject implements ProjectInit {
			private final Project project;
			
			public OpenProject(Project project) {
				this.project = project;
			}
			
			public Project getProject() {
				return project;
			}
		}
		
		class ImportProject implements ProjectInit {
			private final File descriptionExt;
			
			public ImportProject(File descriptionExt) {
				this.descriptionExt = descriptionExt;
			}
			
			public File getDescriptionExt() {
				return descriptionExt;
			}
		}
	}
}
