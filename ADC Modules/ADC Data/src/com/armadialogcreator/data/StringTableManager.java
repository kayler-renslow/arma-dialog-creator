package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.application.Workspace;
import com.armadialogcreator.core.stringtable.StringTable;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/07/2019 */
@ApplicationSingleton
public class StringTableManager implements ApplicationStateSubscriber {
	public static final StringTableManager instance = new StringTableManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private StringTable stringTable;

	@Nullable
	public StringTable getStringTable() {
		return stringTable;
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
