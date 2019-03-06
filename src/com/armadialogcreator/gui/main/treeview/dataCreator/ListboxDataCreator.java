package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.ListboxControl;
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
 @author Kayler
 @since July 23, 2017 */
public class ListboxDataCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {
	public static final ListboxDataCreator INSTANCE = new ListboxDataCreator();

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.ListBox, true);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = EditorManager.instance.getResolution();
		ArmaDisplay display = EditorManager.instance.getEditingDisplay();
		ArmaControl control =
				new ListboxControl(dialog.getClassName(), resolution, ExpressionEnvManager.instance.getEnv(), display);
		return new ControlTreeItemEntry(control);
	}
}
