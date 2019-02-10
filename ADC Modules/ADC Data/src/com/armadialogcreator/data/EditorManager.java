package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.ScreenDimension;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 02/09/2019 */
@ApplicationSingleton
public class EditorManager implements ApplicationStateSubscriber {
	public static final EditorManager instance = new EditorManager();

	static {
		ApplicationManager.getInstance().addStateSubscriber(instance);
	}

	private ArmaDisplay editingDisplay = new ArmaDisplay();
	private final ArmaResolution resolution = new ArmaResolution(ScreenDimension.D848);

	@NotNull
	public ArmaDisplay getEditingDisplay() {
		return editingDisplay;
	}

	@NotNull
	public ArmaResolution getResolution() {
		return resolution;
	}

	@Override
	public void projectInitializing(@NotNull Project project) {
		editingDisplay = new ArmaDisplay();
	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}
}
