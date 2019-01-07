package com.armadialogcreator.data.xml;

import com.armadialogcreator.util.XmlLoader;
import com.armadialogcreator.util.XmlParseException;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 A project preview loader for save-version='1'. This loader will only get the name of the project.

 @author Kayler
 @since 08/07/2016. */
public class ProjectPreviewLoaderVersion1 extends XmlLoader {

	private String projectName;


	protected ProjectPreviewLoaderVersion1(@NotNull File xmlFile) throws XmlParseException {
		super(xmlFile, null);
	}


	public void parseDocument() throws XmlParseException {
		try {
			projectName = document.getDocumentElement().getAttribute("name");

		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new XmlParseException(e.getMessage());
		}
	}

	@NotNull
	public String getProjectName() {
		return projectName;
	}
}
