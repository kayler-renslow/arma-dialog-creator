package com.armadialogcreator.gui.main.popup.projectInit;

import com.armadialogcreator.application.ProjectPreview;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Calendar;

/**
 @author K
 @since 02/09/2019 */
public interface ProjectInit {
	class OpenProject implements ProjectInit {
		private final ProjectPreview project;

		public OpenProject(@NotNull ProjectPreview project) {
			this.project = project;
		}

		@NotNull
		public ProjectPreview getProject() {
			return project;
		}
	}

	class NewProject implements ProjectInit {
		private final String projectName;
		private final String description;

		public NewProject(@Nullable String projectName, @NotNull String description) {
			projectName = projectName == null ? "" : projectName.trim();
			if (projectName.length() == 0) {
				this.projectName = getTemplateProjectName();
			} else {
				this.projectName = projectName;
			}
			this.description = description;
		}

		@NotNull
		public String getProjectName() {
			return projectName;
		}

		@NotNull
		public String getDescription() {
			return description;
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
	}
}
