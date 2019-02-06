package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.application.Workspace;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 01/07/2019 */
public class StringTableManager implements ApplicationStateSubscriber {
	private static final StringTableManager instance = new StringTableManager();

	static {
		ApplicationManager.getInstance().addStateSubscriber(instance);
	}

	@NotNull
	public static StringTableManager getInstance() {
		return instance;
	}

	@Override
	public void projectInitializing(@NotNull Project project) {

	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	@Override
	public void workspaceInitializing(@NotNull Workspace workspace) {

	}

	@Override
	public void workspaceClosed(@NotNull Workspace workspace) {

	}
}
