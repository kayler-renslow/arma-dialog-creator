package com.armadialogcreator.data;

import com.armadialogcreator.application.Configurable;
import com.armadialogcreator.expression.Env;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/17/19 */
public class ClassicWorkspaceSaveLoader {
	private final Configurable root;

	public ClassicWorkspaceSaveLoader(@NotNull Configurable root) {
		this.root = root;
	}

	public void load() {
		for (Configurable c : root.getNestedConfigurables()) {
			if (c.getConfigurableName().equals("custom-controls")) {
				loadCustomControlClasses(c);
			}
		}
	}

	private void loadCustomControlClasses(@NotNull Configurable customControlsConf) {
		Env env = ExpressionEnvManager.instance.getEnv();
		Configurable configClassesConf = new Configurable.Simple("");
		for (Configurable c : customControlsConf.getNestedConfigurables()) {
			if (c.getConfigurableName().equals("custom-control")) {
				Configurable configClassConf = ClassicSaveLoaderUtil.convertCustomControlClassConf(c, env);
				configClassesConf.addNestedConfigurable(configClassConf);
			}
		}
		ConfigClassRegistry.instance.getWorkspaceClasses().loadFromConfigurable(configClassesConf);
		ConfigClassRegistry.instance.getWorkspaceClasses().doJobs();
	}
}
