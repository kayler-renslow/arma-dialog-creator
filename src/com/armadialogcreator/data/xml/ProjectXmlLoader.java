package com.armadialogcreator.data.xml;

import com.armadialogcreator.application.ProjectDescriptor;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.data.olddata.ApplicationData;
import com.armadialogcreator.data.olddata.DataKeys;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.data.tree.TreeStructure;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.lang.Lang;
import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 Loads a project from a .xml save file.
 When the xml is loaded, a {@link ProjectVersionLoader} is designated to do the rest of the xml loading.

 @author Kayler
 @since 07/28/2016. */
public class ProjectXmlLoader extends XmlLoader {

	private final String saveVersion;
	protected final ApplicationData applicationData;
	/** Saved from constructor. This is used for {@link super#XmlLoader(File, DataContext, Key[])} */
	protected final Key<?>[] keys;

	protected ProjectXmlLoader(@NotNull File xmlFile, @NotNull ApplicationData data, Key<?>... keys) throws XmlParseException {
		super(xmlFile, data, keys);
		this.applicationData = data;
		this.keys = keys;
		saveVersion = document.getDocumentElement().getAttribute("save-version").trim();
	}

	/**
	 Parses the given file and returns the result with the Project instance.

	 @param info project information
	 @param data instance to parse with
	 @return result
	 @throws XmlParseException when the file could not be properly parsed
	 */
	@NotNull
	public static ProjectParseResult parseProjectXmlFile(@NotNull ProjectDescriptor info, @NotNull ApplicationData data) throws XmlParseException {
		ProjectXmlLoader loader = new ProjectXmlLoader(info.getProjectXmlFile(), data, Env.ENV, DataKeys.ARMA_RESOLUTION);
		ProjectVersionLoader versionLoader = getVersionLoader(info, loader);
		versionLoader.readDocument();
		return new ProjectParseResult(versionLoader.project, versionLoader.treeStructureMain, versionLoader.treeStructureBg, loader.getErrors());
	}

	/**
	 Parses the given file and returns the result with the project information.

	 @param projectSaveXml file that contains the project save xml
	 @return result
	 @throws XmlParseException when the file could not be properly parsed
	 */
	@NotNull
	public static ProjectPreviewParseResult previewParseProjectXmlFile(@NotNull File projectSaveXml) throws XmlParseException {
		ProjectPreviewLoaderVersion1 versionLoader = new ProjectPreviewLoaderVersion1(projectSaveXml);
		versionLoader.parseDocument();
		return new ProjectPreviewParseResult(
				new ProjectDescriptor(versionLoader.getProjectName(),
						projectSaveXml.getParentFile().getName(),
						new Workspace(projectSaveXml.getParentFile().getParentFile())
				),
				versionLoader.getErrors()
		);
	}


	private static ProjectVersionLoader getVersionLoader(@NotNull ProjectDescriptor info, @NotNull ProjectXmlLoader loader) throws XmlParseException {
		switch (loader.saveVersion) {
			case "1":
				return new ProjectLoaderVersion1(info, loader);
			default:
				throw new XmlParseException(XmlParseException.Reason.Other, Lang.getBundle("ProjectXmlParseBundle").getString("ProjectLoad.not_a_project_save"));
		}
	}

	public static class ProjectParseResult extends ParseResult {

		private final Project project;
		private final TreeStructure<ArmaControl> treeStructureMain;
		private final TreeStructure<ArmaControl> treeStructureBg;

		private ProjectParseResult(Project project, TreeStructure<ArmaControl> treeStructureMain,
								   TreeStructure<ArmaControl> treeStructureBg, List<ParseError> errors) {
			super(errors);
			this.project = project;
			this.treeStructureMain = treeStructureMain;
			this.treeStructureBg = treeStructureBg;
		}

		@NotNull
		public TreeStructure<ArmaControl> getTreeStructureBg() {
			return treeStructureBg;
		}

		@NotNull
		public TreeStructure<ArmaControl> getTreeStructureMain() {
			return treeStructureMain;
		}

		@NotNull
		public Project getProject() {
			return project;
		}

	}

	public static class ProjectPreviewParseResult {
		private final List<ParseError> errors;
		private final ProjectDescriptor projectDescriptor;

		public ProjectPreviewParseResult(@NotNull ProjectDescriptor projectDescriptor, @NotNull List<ParseError> errors) {
			this.projectDescriptor = projectDescriptor;
			this.errors = errors;
		}

		@NotNull
		public List<ParseError> getErrors() {
			return errors;
		}

		@NotNull
		public ProjectDescriptor getProjectDescriptor() {
			return projectDescriptor;
		}

	}
}
