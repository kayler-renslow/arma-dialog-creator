package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.StaticControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/19/2016.
 */
public class StaticDataCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {
	public static final StaticDataCreator INSTANCE = new StaticDataCreator();

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.Static, true);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		EditorManager editorManager = EditorManager.instance;
		ArmaResolution resolution = editorManager.getResolution();
		ArmaDisplay display = editorManager.getEditingDisplay();

		StaticControl control = new StaticControl(dialog.getClassName(), 0, resolution,
				ExpressionEnvManager.instance.getEnv(), display);
		return new ControlTreeItemEntry(control);
	}
}
