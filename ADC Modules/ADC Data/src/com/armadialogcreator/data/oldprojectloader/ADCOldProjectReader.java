package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.application.Project;
import com.armadialogcreator.util.UTF8FileReader;
import com.armadialogcreator.util.XmlParseException;
import com.armadialogcreator.util.XmlReader;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

/**
 @author K
 @since 01/07/2019 */
public class ADCOldProjectReader {
	private final Project project;

	public ADCOldProjectReader(@NotNull Project p) {
		this.project = p;
	}

	public void read() throws XmlParseException {
		XmlReader reader;
		try {
			reader = new XmlReader(new UTF8FileReader(project.getProjectSaveFile()));
		} catch (FileNotFoundException e) {
			throw new XmlParseException(XmlParseException.Reason.FileNotFound, e);
		}


	}
}
