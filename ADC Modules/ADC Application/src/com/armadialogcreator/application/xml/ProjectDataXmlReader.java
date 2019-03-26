package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.application.ProjectData;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/07/2019 */
public class ProjectDataXmlReader extends ADCDataConfigurableXmlReader<ProjectData> {

	public ProjectDataXmlReader(@NotNull Project p, @NotNull ADCFile xmlFile) {
		super(xmlFile, p);
	}

}
