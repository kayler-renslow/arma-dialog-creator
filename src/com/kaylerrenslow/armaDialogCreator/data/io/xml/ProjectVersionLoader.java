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

import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.w3c.dom.Document;

/**
 @author Kayler
 A wrapper for a loader. Since the loader is the one that loads the xml document, this will wrap the XmlLoader. This class is meant to be extended so that each save version of projects has its own
 implementation.
 Created on 08/07/2016. */
public abstract class ProjectVersionLoader {
	
	protected final DataContext dataContext;
	protected Project project;
	protected Document document;
	public final ProjectXmlLoader loader;
	protected TreeStructure<TreeItemEntry> treeStructureMain = new TreeStructure<>(new TreeStructure.TreeNode<>(null));
	protected TreeStructure<TreeItemEntry> treeStructureBg = new TreeStructure<>(new TreeStructure.TreeNode<>(null));
	
	
	protected ProjectVersionLoader(ProjectXmlLoader loader) throws XmlParseException {
		this.document = loader.document;
		this.loader = loader;
		this.dataContext = loader.dataContext;
	}
	
	public void addError(ParseError error) {
		loader.addError(error);
	}
	
	public void parseDocument() throws XmlParseException {
		
	}
}
