package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.application.ProjectDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 05/09/2017 */
public interface ProjectInit {

	class NewProject implements ProjectInit {
		private final String projectName;
		private final String projectDescription;

		public NewProject(@Nullable String projectName, @NotNull String projectDescription) {
			this.projectName = projectName;
			this.projectDescription = projectDescription;
		}

		@NotNull
		public String getProjectDescription() {
			return projectDescription;
		}

		@Nullable
		public String getProjectName() {
			return projectName;
		}
	}

	class OpenProject implements ProjectInit {
		private final ProjectXmlReader.ProjectPreviewParseResult parseResult;

		public OpenProject(@NotNull ProjectXmlReader.ProjectPreviewParseResult parseResult) {
			this.parseResult = parseResult;
		}

		@NotNull
		public ProjectXmlReader.ProjectPreviewParseResult getParseResult() {
			return parseResult;
		}

		@NotNull
		public ProjectDescriptor getProjectXmlDescriptor() {
			return parseResult.getProjectDescriptor();
		}
	}
}
