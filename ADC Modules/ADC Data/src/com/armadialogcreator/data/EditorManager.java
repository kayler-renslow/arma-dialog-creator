package com.armadialogcreator.data;

import com.armadialogcreator.application.*;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.control.*;
import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ApplicationSingleton;
import com.armadialogcreator.util.ScreenDimension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
		project.getDataList().add(new EditorProjectData());
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		for (ProjectData pd : project.getDataList()) {
			if (pd instanceof EditorProjectData) {
				((EditorProjectData) pd).doJobs();
			}
		}
	}

	@Override
	public void projectClosed(@NotNull Project project) {

	}

	public void immediatelyLoadDisplayFromConfigurable(@NotNull Configurable c) {
		EditorProjectData data = new EditorProjectData();
		data.loadFromConfigurable(c);
		data.doJobs();
	}

	private static class EditorProjectData implements ProjectData {
		private List<ConfigClassJob> jobs;

		@Override
		@NotNull
		public String getDataID() {
			return "EditorManagerData";
		}

		@Override
		public void loadFromConfigurable(@NotNull Configurable config) {
			jobs = new ArrayList<>();
			Configurable ccConf = config.getConfigurable("config-class");
			if (ccConf != null) {
				ArmaDisplay armaDisplay = new ArmaDisplay("");
				ConfigClassConfigurable.fromConfigurable(ccConf, armaDisplay, configClassJob -> {
					jobs.add(configClassJob);
				});
				EditorManager.instance.editingDisplay = armaDisplay;
			}
			Configurable configClasses = config.getConfigurable("control-config-classes");
			if (configClasses != null) {
				ArmaDisplay display = EditorManager.instance.editingDisplay;
				ArmaResolution resolution = EditorManager.instance.resolution;
				Env env = ExpressionEnvManager.instance.getEnv();
				ConfigClassRegistry.ProjectClasses projectClasses = ConfigClassRegistry.instance.getProjectClasses();
				for (Configurable c : configClasses.getNestedConfigurables()) {
					String controlId = c.getAttributeValueNotNull("control-type");
					ControlType type = ControlType.findById(Integer.parseInt(controlId));
					ArmaControl control = ArmaControl.createControl(
							type,
							"",
							resolution,
							env,
							display
					);
					projectClasses.addClass(control);
					ConfigClassConfigurable.fromConfigurable(c, control, configClassJob -> {
						jobs.add(configClassJob);
					});
				}
			}
			Configurable nodes = config.getConfigurableNotNull("nodes");
			ArmaDisplay d = EditorManager.instance.editingDisplay;
			Configurable bgConf = nodes.getConfigurable("background");
			if (bgConf != null) {
				loadNodes(bgConf, d.getBackgroundControlNodes());
			}
			Configurable mainConf = nodes.getConfigurable("main");
			if (mainConf != null) {
				loadNodes(mainConf, d.getControlNodes());
			}
		}

		private void loadNodes(@NotNull Configurable conf, @NotNull UINode parent) {
			ConfigClassRegistry.ProjectClasses projectClasses = ConfigClassRegistry.instance.getProjectClasses();

			for (Configurable nested : conf.getNestedConfigurables()) {
				String nodeType = nested.getAttributeValueNotNull("nt");
				String nodeName = UINodeConfigurable.getUINodeNameFromConfigurable(nested);
				switch (nodeType) {
					case "control": {
						if (nodeName == null) {
							throw new IllegalStateException();
						}
						ConfigClass cc = projectClasses.findConfigClassByName(nodeName);
						if (!(cc instanceof ArmaControl)) {
							throw new IllegalStateException(nodeName);
						}
						ArmaControl control = (ArmaControl) cc;
						parent.addChild(control);
						loadNodes(nested, control);
						break;
					}
					case "folder": {
						if (nodeName == null) {
							throw new IllegalStateException();
						}
						FolderUINode folder = new FolderUINode(nodeName);
						parent.addChild(folder);
						loadNodes(nested, folder);
						break;
					}
					case "collection": {
						if (nodeName == null) {
							throw new IllegalStateException();
						}
						CollectionUINode collection = new CollectionUINode(nodeName);
						parent.addChild(collection);
						loadNodes(nested, collection);
						break;
					}
					default: {
						continue;
					}
				}
			}
		}

		@Override
		public void exportToConfigurable(@NotNull Configurable configurable) {
			// todo
		}

		public void doJobs() {
			if (jobs == null) {
				return; //nothing was loaded
			}
			for (ConfigClassJob job : jobs) {
				job.doWork();
			}
			jobs = null; //help GC
		}
	}
}
