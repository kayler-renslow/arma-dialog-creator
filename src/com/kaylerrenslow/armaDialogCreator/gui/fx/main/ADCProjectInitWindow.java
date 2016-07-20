package com.kaylerrenslow.armaDialogCreator.gui.fx.main;

import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.popup.StagePopup;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 @author Kayler
 Used for setting up the initial {@link Project}. This window is shown before {@link ADCWindow}.
 Created on 07/19/2016. */
public class ADCProjectInitWindow extends StagePopup<VBox> {
	
	public ADCProjectInitWindow() {
		super(ArmaDialogCreator.getPrimaryStage(), new VBox(5), Lang.ProjectInitWindow.WINDOW_TITLE);
		
		//header
		final Label lblProjectSetup = new Label(Lang.ProjectInitWindow.PROJECT_SETUP);
		lblProjectSetup.setFont(Font.font(18d));
		myRootElement.getChildren().addAll(lblProjectSetup, new Separator(Orientation.HORIZONTAL));
		
		initTabPane();
	}
	
	private void initTabPane() {
		final Tab tabNew = new Tab(Lang.ProjectInitWindow.TAB_NEW);
		final Tab tabOpen = new Tab(Lang.ProjectInitWindow.TAB_OPEN);
		final Tab tabImport = new Tab(Lang.ProjectInitWindow.TAB_IMPORT);
		tabNew.setClosable(false);
		tabOpen.setClosable(false);
		tabImport.setClosable(false);
		
		final TabPane tabPane = new TabPane(tabNew, tabOpen, tabImport);
		
		initTabNew(tabNew);
		
	}
	
	private void initTabNew(Tab tabNew) {
		final VBox root = new VBox(20);
		final Label lblCreateNewProject = new Label(Lang.ProjectInitWindow.CREATE_NEW_PROJECT);
		
	}
}
