package com.kaylerrenslow.armaDialogCreator.main;

import com.kaylerrenslow.armaDialogCreator.data.Project;
import javafx.stage.FileChooser;

/**
 Created by Kayler on 10/15/2016.
 */
public interface ADCStatic {
	FileChooser.ExtensionFilter IMAGE_FILE_EXTENSIONS = new FileChooser.ExtensionFilter(Lang.ApplicationBundle().getString("ValueEditors.ImageValueEditor.image_files"), "*.jpg", "*.png", "*.paa");
	FileChooser.ExtensionFilter PROJECT_XML_FC_FILTER = new FileChooser.ExtensionFilter(Project.PROJECT_SAVE_FILE_NAME, "*.xml");

}
