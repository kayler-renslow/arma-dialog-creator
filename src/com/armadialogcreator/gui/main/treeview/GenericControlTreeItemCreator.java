package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.ADCGuiManager;
import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.actions.NewControlAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 7/27/2017 */
public class GenericControlTreeItemCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {
	private final ControlType type;

	public GenericControlTreeItemCreator(@NotNull ControlType type) {
		this.type = type;
	}

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlAction action = new NewControlAction(type, ADCGuiManager.instance.isBackgroundControlTreeView(treeView), false);
		ArmaControl control = action.doAction();
		if (control == null) {
			return null;
		}

		return new ControlTreeItemEntry(control);
	}

}
