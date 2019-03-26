package com.armadialogcreator.application.xml;

import com.armadialogcreator.application.ADCFile;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.application.WorkspaceData;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/07/2019 */
public class WorkspaceDataXmlReader extends ADCDataConfigurableXmlReader<WorkspaceData> {

	public WorkspaceDataXmlReader(@NotNull Workspace w, @NotNull ADCFile xmlFile) {
		super(xmlFile, w);
	}

}
