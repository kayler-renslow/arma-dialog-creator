package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.Project;
import com.armadialogcreator.application.ProjectData;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author K
 @since 01/07/2019 */
public class ProjectDataXmlReader extends ConfigurableXmlReader<ProjectData> {

	public ProjectDataXmlReader(@NotNull Project p, @NotNull File xmlFile) {
		super(xmlFile, p);
	}

}
