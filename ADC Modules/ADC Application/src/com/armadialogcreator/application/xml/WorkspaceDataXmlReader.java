package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.application.WorkspaceData;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author K
 @since 01/07/2019 */
public class WorkspaceDataXmlReader extends ConfigurableXmlReader<WorkspaceData> {

	public WorkspaceDataXmlReader(@NotNull Workspace w, @NotNull File xmlFile) {
		super(xmlFile, w);
	}

}
