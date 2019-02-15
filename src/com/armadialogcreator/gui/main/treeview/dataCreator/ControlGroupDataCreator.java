package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.ControlGroupControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlGroupTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupDataCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {

	public static final ControlGroupDataCreator INSTANCE = new ControlGroupDataCreator();

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.ControlsGroup, true);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = EditorManager.instance.getResolution();
		Env env = ExpressionEnvManager.instance.getEnv();

		return new ControlGroupTreeItemEntry(new ControlGroupControl(dialog.getClassName(), -1, resolution, env));
	}
}
