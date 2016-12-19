package com.kaylerrenslow.armaDialogCreator.data.xml;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 A project loader for save-version='1'

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
