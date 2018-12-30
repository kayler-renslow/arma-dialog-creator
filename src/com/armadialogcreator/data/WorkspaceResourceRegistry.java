package com.armadialogcreator.data;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 A {@link WorkspaceResourceRegistry} is a workspace level {@link ResourceRegistry}.
 This {@link ResourceRegistry} has the option to share resources between projects.

 @author Kayler
 @since 11/23/2016 */
public class WorkspaceResourceRegistry extends ResourceRegistry {

	protected WorkspaceResourceRegistry(@NotNull Workspace workspace) {
		super(workspace.getFileInAdcDirectory("resources" + File.separator + "global-resources.xml"));
	}

	/**
	 Get the {@link WorkspaceResourceRegistry} for the instance returned by {@link Workspace#getWorkspace()}

	 @return registry
	 */
	@NotNull
	public static WorkspaceResourceRegistry getInstance() {
		return Workspace.getWorkspace().getGlobalResourceRegistry();
	}
}
