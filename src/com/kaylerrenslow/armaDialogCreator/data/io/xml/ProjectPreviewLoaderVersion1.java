/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data.io.xml;

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
