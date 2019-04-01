package com.armadialogcreator.gui.main.actions;

import com.armadialogcreator.ADCGuiManager;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.impl.ArmaControlLookup;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.data.ControlDefaultValueProvider;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 3/31/19 */
public class NewControlAction {
	private ControlType forcedType;
	private boolean isBackground;
	private final boolean addToTreeView;

	public NewControlAction(boolean addToTreeView) {
		this.addToTreeView = addToTreeView;
	}

	public NewControlAction(@Nullable ControlType forcedType, boolean isBackground, boolean addToTreeView) {
		this.forcedType = forcedType;
		this.isBackground = isBackground;
		this.addToTreeView = addToTreeView;
	}

	@Nullable
	public ArmaControl doAction() {
		NewControlDialog dialog = new NewControlDialog(forcedType, isBackground);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		EditorManager editorManager = EditorManager.instance;

		String className = dialog.getClassName();
		ControlType controlType = dialog.getControlType();
		boolean backgroundControl = dialog.isBackgroundControl();
		ArmaControlLookup lookup = ArmaControlLookup.findByControlType(controlType);
		ArmaDisplay display = editorManager.getEditingDisplay();

		ArmaControl control = ArmaControl.createControl(className, lookup, editorManager.getResolution(),
				ExpressionEnvManager.instance.getEnv(), display);

		ControlDefaultValueProvider.addDefaultValuesFromSystemSheet(control);

		if (backgroundControl) {
			editorManager.getEditingDisplay().getBackgroundControlNodes().addChild(control);
		} else {
			editorManager.getEditingDisplay().getControlNodes().addChild(control);
		}

		if (addToTreeView) {
			ADCGuiManager.instance.addControlToTreeView(control, backgroundControl);
		}

		ConfigClassRegistry.instance.getProjectClasses().addClass(control);

		return control;
	}
}
