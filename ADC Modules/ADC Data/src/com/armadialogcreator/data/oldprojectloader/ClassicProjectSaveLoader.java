package com.armadialogcreator.data.oldprojectloader;

import com.armadialogcreator.application.Configurable;
import org.jetbrains.annotations.NotNull;

/**
 @author K
 @since 4/11/19 */
public class ClassicProjectSaveLoader {
	private final Configurable root;

	public ClassicProjectSaveLoader(@NotNull Configurable root) {
		this.root = root;
	}

	private void loadDisplay() {
		Configurable display = root.getConfigurable("display");
		if (display == null) {
			return;
		}
		for (Configurable nested : display.getNestedConfigurables()) {
			switch (nested.getConfigurableName()) {
				case "display-property": {
					String sid = nested.getAttributeValue("id");
					if (sid == null) {
						// todo
						continue;
					}
					int id;
					try {
						id = Integer.parseInt(sid);
					} catch (NumberFormatException e) {
						// todo
						continue;
					}

					break;
				}
				case "display-controls": {
					String type = nested.getAttributeValue("type");
					if (type == null) {
						// todo
						continue;
					}

					if (type.equals("background")) {

					} else {

					}
					break;
				}
				default: {
					// todo
				}
			}
		}
	}
}
