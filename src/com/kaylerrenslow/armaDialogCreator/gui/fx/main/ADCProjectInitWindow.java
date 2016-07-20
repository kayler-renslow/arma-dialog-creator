package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

import java.io.File;

/**
 @author Kayler
 Used for setting up the initial {@link Project}. This window is shown before {@link ADCWindow}.
 Created on 07/19/2016. */
public class ADCProjectInitWindow extends StagePopup<VBox> {
	
	private final Tab tabNew = new Tab(Lang.ProjectInitWindow.TAB_NEW);
	private final Tab tabOpen = new Tab(Lang.ProjectInitWindow.TAB_OPEN);
	private final Tab tabImport = new Tab(Lang.ProjectInitWindow.TAB_IMPORT);
	private final TabPane tabPane = new TabPane(tabNew, tabOpen, tabImport);
	
	/** TextField used for getting project name in new tab */
	private TextField tfProjectName;
	private final Insets padding = new Insets(10);
	
	
	public ADCProjectInitWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ProjectInitWindow.WINDOW_TITLE);
		myRootElement.setPadding(padding);
		
		//header
		final Label lblProjectSetup = new Label(Lang.ProjectInitWindow.PROJECT_SETUP);
		lblProjectSetup.setFont(Font.font(18d));
		
		initTabPane();
		
		myRootElement.getChildren().addAll(lblProjectSetup, new Separator(Orientation.HORIZONTAL), tabPane);
		myRootElement.getChildren().addAll(new Separator(Orientation.HORIZONTAL), getResponseFooter(false, true, false));
		
		myStage.initModality(Modality.APPLICATION_MODAL);
		myStage.setWidth(720d);
		myStage.setHeight(360d);
		myStage.setResizable(false);
		
		tabPane.getSelectionModel().select(tabOpen); //force a change to notify tabPane listener
		tabPane.getSelectionModel().select(tabNew);
		tfProjectName.requestFocus();
		
		this.btnOk.setPrefWidth(130d);
	}
	
	private void initTabPane() {
		tabNew.setClosable(false);
		tabOpen.setClosable(false);
		tabImport.setClosable(false);
		
		tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				getOkButton().setText(newValue.getUserData().toString());
			}
		});
		
		initTabNew(tabNew);
		initTabOpen(tabOpen);
		initTabImport(tabImport);
	}
	
	private Button getOkButton(){
		return this.btnOk;
	}
	
	private void initTabImport(Tab tabImport) {
		tabImport.setUserData(Lang.ProjectInitWindow.IMPORT_PROJECT_OK);
		final VBox root = getTabVbox(20);
		final Label lblOpenProject = new Label(Lang.ProjectInitWindow.IMPORT_PROJECT_TITLE);
		
		root.getChildren().addAll(lblOpenProject);
		
		tabImport.setContent(root);
	}
	
	private void initTabOpen(Tab tabOpen) {
		tabOpen.setUserData(Lang.ProjectInitWindow.OPEN_PROJECT_OK);
		final VBox root = getTabVbox(20);
		final Label lblOpenProject = new Label(Lang.ProjectInitWindow.OPEN_PROJECT_TITLE);
		
		root.getChildren().addAll(lblOpenProject);
		tabOpen.setContent(root);
	}
	
	private void initTabNew(Tab tabNew) {
		tabNew.setUserData(Lang.ProjectInitWindow.NEW_PROJECT_OK);
		final VBox root = getTabVbox(20);
		final Label lblCreateNewProject = new Label(Lang.ProjectInitWindow.NEW_PROJECT_TITLE);
		tfProjectName = new TextField();
		tfProjectName.setPrefWidth(200d);
		final Label lblProjectName = new Label(Lang.ProjectInitWindow.PROJECT_NAME, tfProjectName);
		lblProjectName.setContentDisplay(ContentDisplay.RIGHT);
		
		root.getChildren().addAll(lblCreateNewProject, lblProjectName);
		
		tabNew.setContent(root);
	}
	
	private VBox getTabVbox(double spacing){
		VBox vBox = new VBox(spacing);
		vBox.setPadding(padding);
		vBox.setMinHeight(200d);
		return vBox;
	}
	
	@Override
	protected void onCloseRequest(WindowEvent event) {
		System.exit(0);
	}
	
	public ProjectInit getProjectInit() {
		Tab selected = tabPane.getSelectionModel().getSelectedItem();
		if (selected == tabNew) {
			return new ProjectInit.NewProject(tfProjectName.getText());
		} else if (selected == tabOpen) {
			
		} else if (selected == tabImport) {
			
		}
		throw new IllegalStateException("Should provide implementation for selected tab");
	}
		
	public interface ProjectInit {
		
		
		class NewProject implements ProjectInit {
			private final String projectName;
			
			public NewProject(String projectName) {
				this.projectName = projectName;
			}
			
			public String getProjectName() {
				return projectName;
			}
		}
		
		class OpenProject implements ProjectInit {
			private final File projectFile;
			
			public OpenProject(File projectFile) {
				this.projectFile = projectFile;
			}
			
			public File getProjectFile() {
				return projectFile;
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
