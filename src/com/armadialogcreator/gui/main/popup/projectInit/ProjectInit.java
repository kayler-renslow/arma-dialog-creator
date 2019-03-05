package com.armadialogcreator.gui.main.popup.projectInit;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ProjectDescriptor;
import com.armadialogcreator.application.ProjectPreview;
import com.armadialogcreator.application.Workspace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

/**
 @author K
 @since 02/09/2019 */
public interface ProjectInit {
	enum Type {
		Open, New, Import, OpenLegacyProject
	}

	@NotNull
	Type getType();

	@NotNull Workspace getWorkspace();

	@NotNull ProjectDescriptor getDescriptor();

	class OpenProject implements ProjectInit {
		private final ProjectPreview project;

		public OpenProject(@NotNull ProjectPreview project) {
			this.project = project;
		}

		@NotNull
		public ProjectPreview getProject() {
			return project;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.Open;
		}

		@Override
		@NotNull
		public Workspace getWorkspace() {
			return project.getWorkspace();
		}

		@Override
		@NotNull
		public ProjectDescriptor getDescriptor() {
			return project.toDescriptor();
		}
	}

	class NewProject implements ProjectInit {
		private final Workspace workspace;
		private final ProjectDescriptor descriptor;

		public NewProject(@NotNull Workspace workspace, @Nullable String projectName, @NotNull String description) {
			this.workspace = workspace;
			projectName = projectName == null ? "" : projectName.trim();
			if (projectName.length() == 0) {
				projectName = getTemplateProjectName();
			}

			descriptor = new ProjectDescriptor(projectName, description, ApplicationManager.getProjectSaveFile(workspace, projectName), workspace);
		}


		private String getTemplateProjectName() {
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH) + 1; //month starts at 0
			int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int hour = Calendar.getInstance().get(Calendar.HOUR);
			int minute = Calendar.getInstance().get(Calendar.MINUTE);
			int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
			String date = String.format("%d-%d-%d %d-%d%s", year, month, day, hour, minute, am_pm == Calendar.AM ? "am" : "pm");
			return "untitled " + date;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.New;
		}

		@Override
		@NotNull
		public Workspace getWorkspace() {
			return workspace;
		}

		@Override
		@NotNull
		public ProjectDescriptor getDescriptor() {
			return descriptor;
		}
	}
}
