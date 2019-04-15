package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
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
		ApplicationManager.instance.addStateSubscriber(instance);
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

	public int getBackgroundImage() {
		return 0;
	}


	@Override
	public void projectInitializing(@NotNull Project project) {
		editingDisplay = new ArmaDisplay();
		project.getDataList().add(new EditorProjectData());
	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	public void loadDisplayFromConfigurable(@NotNull Configurable c) {

	}

	private static class EditorProjectData implements ProjectData {

		@Override
		public @NotNull String getDataID() {
			return "EditorManagerData";
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			EditorManager.instance.loadDisplayFromConfigurable(config);
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			// todo
		}
	}
}
