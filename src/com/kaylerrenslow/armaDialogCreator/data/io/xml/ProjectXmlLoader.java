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

import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeStructure;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

/**
 @author Kayler
 Loads a project from a .xml save file. When the xml is loaded, a {@link ProjectVersionLoader} is designated to do the rest of the xml loading.
 Created on 07/28/2016. */
public class ProjectXmlLoader extends XmlLoader {
	
	private final String saveVersion;
	
	
	protected ProjectXmlLoader(@NotNull File xmlFile, @Nullable DataContext context, Key<?>... keys) throws XmlParseException {
		super(xmlFile, context, keys);
		saveVersion = document.getDocumentElement().getAttribute("save-version").trim();
	}
	
	/**
	 Parses the given file and returns the result with the Project instance.
	 
	 @param context must contain keys: {@link DataKeys#ARMA_RESOLUTION}, {@link DataKeys#ENV}
	 @param projectSaveXml file that contains the project save xml
	 @return result
	 @throws XmlParseException when the file could not be properly parsed
	 */
	@NotNull
	public static ProjectParseResult parse(@NotNull DataContext context, @NotNull File projectSaveXml) throws XmlParseException {
		ProjectXmlLoader loader = new ProjectXmlLoader(projectSaveXml, context, DataKeys.ENV, DataKeys.ARMA_RESOLUTION);
		ProjectVersionLoader versionLoader = getVersionLoader(loader);
		versionLoader.parseDocument();
		return new ProjectParseResult(versionLoader.project, versionLoader.treeStructureMain, versionLoader.treeStructureBg, loader.getErrors());
	}
	
	private static ProjectVersionLoader getVersionLoader(ProjectXmlLoader loader) throws XmlParseException {
		switch (loader.saveVersion) {
			case "1":
				return new ProjectLoaderVersion1(loader);
			default:
				throw new XmlParseException(Lang.ApplicationBundle.getString("XmlParse.ProjectLoad.not_a_project_save"));
		}
	}
	
	public static class ProjectParseResult extends XmlLoader.ParseResult {
		
		public final Project project;
		public final TreeStructure<TreeItemEntry> treeStructureMain;
		public final TreeStructure<TreeItemEntry> treeStructureBg;
		
		private ProjectParseResult(Project project, TreeStructure<TreeItemEntry> treeStructureMain, TreeStructure<TreeItemEntry> treeStructureBg, ArrayList<ParseError> errors) {
			super(errors);
			this.project = project;
			this.treeStructureMain = treeStructureMain;
			this.treeStructureBg = treeStructureBg;
		}
		
		public TreeStructure<TreeItemEntry> getTreeStructureBg() {
			return treeStructureBg;
		}
		
		public TreeStructure<TreeItemEntry> getTreeStructureMain() {
			return treeStructureMain;
		}
		
		public Project getProject() {
			return project;
		}
		
	}
	
	
	static class ControlLoadConfig {
		public final ControlPropertyLookup lookup;
		public final SerializableValue value;
		public final String macroKey;
		
		public ControlLoadConfig(ControlPropertyLookup lookup, SerializableValue value, String macroKey) {
			this.lookup = lookup;
			this.value = value;
			if (macroKey.trim().length() == 0) {
				this.macroKey = null;
			} else {
				this.macroKey = macroKey.trim();
			}
		}
	}
}
