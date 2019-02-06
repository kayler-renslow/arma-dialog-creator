package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.ApplicationData;
import com.armadialogcreator.application.ApplicationDataManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author K
 @since 01/07/2019 */
public class ApplicationDataXmlReader extends ConfigurableXmlReader<ApplicationData> {

	public ApplicationDataXmlReader(@NotNull File xmlFile) {
		super(xmlFile, ApplicationDataManager.getInstance());
	}

}
