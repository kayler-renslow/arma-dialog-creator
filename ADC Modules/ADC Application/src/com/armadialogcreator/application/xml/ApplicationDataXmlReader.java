package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.application.ApplicationData;
import com.armadialogcreator.application.ApplicationDataManager;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/07/2019 */
public class ApplicationDataXmlReader extends ADCDataConfigurableXmlReader<ApplicationData> {

	public ApplicationDataXmlReader(@NotNull ADCFile xmlFile) {
		super(xmlFile, ApplicationDataManager.getInstance());
	}

}
