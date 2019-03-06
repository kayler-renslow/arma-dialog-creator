package com.armadialogcreator.gui.main.actions.mainMenu.create;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.impl.ArmaControlLookup;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 @author Kayler
 @since 11/15/2016 */
public class CreateNewControlAction implements EventHandler<ActionEvent> {
	@Override
	public void handle(ActionEvent event) {
		NewControlDialog dialog = new NewControlDialog(null, false);
		dialog.show();
		if (dialog.wasCancelled()) {
			return;
		}
		String className = dialog.getClassName();
		ControlType controlType = dialog.getControlType();
		boolean backgroundControl = dialog.isBackgroundControl();
		ArmaControlLookup lookup = ArmaControlLookup.findByControlType(controlType);
		EditorManager editorManager = EditorManager.instance;
		ArmaDisplay display = editorManager.getEditingDisplay();
		ArmaControl control = ArmaControl.createControl(className, lookup, editorManager.getResolution(),
				ExpressionEnvManager.instance.getEnv(), display);
		if (backgroundControl) {
			editorManager.getEditingDisplay().getBackgroundControlNodes().addChild(control);
		} else {
			editorManager.getEditingDisplay().getControlNodes().addChild(control);
		}
		ConfigClassRegistry.instance.getProjectClasses().addClass(control);
	}
}
