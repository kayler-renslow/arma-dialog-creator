package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationDataManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author K
 @since 01/06/2019 */
public class ApplicationStateChangeLogger implements ApplicationStateSubscriber {
	private static final ApplicationStateChangeLogger instance = new ApplicationStateChangeLogger();

	static {
		ApplicationDataManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static ApplicationStateChangeLogger getInstance() {
		return instance;
	}

	private static final int LOG_SIZE_CAP = 30;

	private final ListObserver<LogEntry> applicationInfoLog = new ListObserver<>(new ArrayList<>());
	private final ListObserver<ErrorLogEntry> applicationErrorLog = new ListObserver<>(new ArrayList<>());
	private final ListObserver<LogEntry> workspaceInfoLog = new ListObserver<>(new ArrayList<>());
	private final ListObserver<ErrorLogEntry> workspaceErrorLog = new ListObserver<>(new ArrayList<>());
	private final ListObserver<LogEntry> projectInfoLog = new ListObserver<>(new ArrayList<>());
	private final ListObserver<ErrorLogEntry> projectErrorLog = new ListObserver<>(new ArrayList<>());

	public ApplicationStateChangeLogger() {
		applicationInfoLog.addListener((list, change) -> {
			if (applicationInfoLog.size() > LOG_SIZE_CAP) {
				System.out.println("ApplicationStateChangeLogger.ApplicationStateChangeLogger applicationInfoLog.size() > LOG_SIZE_CAP");
				applicationInfoLog.clear();
			}
		});
		applicationErrorLog.addListener((list, change) -> {
			if (applicationErrorLog.size() > LOG_SIZE_CAP) {
				System.out.println("ApplicationStateChangeLogger.ApplicationStateChangeLogger applicationErrorLog.size() > LOG_SIZE_CAP");
				applicationErrorLog.clear();
			}
		});
		workspaceInfoLog.addListener((list, change) -> {
			if (workspaceInfoLog.size() > LOG_SIZE_CAP) {
				System.out.println("ApplicationStateChangeLogger.ApplicationStateChangeLogger workspaceInfoLog.size() > LOG_SIZE_CAP");
				workspaceInfoLog.clear();
			}
		});
		workspaceErrorLog.addListener((list, change) -> {
			if (workspaceErrorLog.size() > LOG_SIZE_CAP) {
				System.out.println("ApplicationStateChangeLogger.ApplicationStateChangeLogger workspaceErrorLog.size() > LOG_SIZE_CAP");
				workspaceErrorLog.clear();
			}
		});
		projectInfoLog.addListener((list, change) -> {
			if (projectInfoLog.size() > LOG_SIZE_CAP) {
				System.out.println("ApplicationStateChangeLogger.ApplicationStateChangeLogger projectInfoLog.size() > LOG_SIZE_CAP");
				projectInfoLog.clear();
			}
		});
		projectErrorLog.addListener((list, change) -> {
			if (projectErrorLog.size() > LOG_SIZE_CAP) {
				System.out.println("ApplicationStateChangeLogger.ApplicationStateChangeLogger projectErrorLog.size() > LOG_SIZE_CAP");
				projectErrorLog.clear();
			}
		});
	}

	@NotNull
	public ListObserver<LogEntry> getApplicationInfoLog() {
		return applicationInfoLog;
	}

	@NotNull
	public ListObserver<ErrorLogEntry> getApplicationErrorLog() {
		return applicationErrorLog;
	}

	@NotNull
	public ListObserver<LogEntry> getWorkspaceInfoLog() {
		return workspaceInfoLog;
	}

	@NotNull
	public ListObserver<ErrorLogEntry> getWorkspaceErrorLog() {
		return workspaceErrorLog;
	}

	@NotNull
	public ListObserver<LogEntry> getProjectInfoLog() {
		return projectInfoLog;
	}

	@NotNull
	public ListObserver<ErrorLogEntry> getProjectErrorLog() {
		return projectErrorLog;
	}

	@Override
	public void projectClosed(@NotNull Project project) {
		projectInfoLog.clear();
		projectErrorLog.clear();
	}

	@Override
	public void workspaceClosed(@NotNull Workspace workspace) {
		workspaceInfoLog.clear();
		workspaceErrorLog.clear();
	}

	public interface LogEntry {
		@Nls
		@NotNull String getTitle();

		@Nls
		@NotNull String getBody();
	}

	public interface ErrorLogEntry extends LogEntry {
		@Nullable Throwable getThrowable();

		/** @return true if error didn't result in a bad program state */
		boolean didRecover();

		/** @return message saying how the error was recovered, or null if didn't recover */
		@Nls
		@Nullable String getRecoveredMessage();
	}
}
