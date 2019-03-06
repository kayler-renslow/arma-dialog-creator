package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaDisplay;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.ShortcutButtonControl;
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
 @since 7/6/2017 */
public class ShortcutButtonDataCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {
	public static final ShortcutButtonDataCreator INSTANCE = new ShortcutButtonDataCreator();

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlDialog dialog = new NewControlDialog(
				ControlType.ShortcutButton,
				true
		);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}
		EditorManager editorManager = EditorManager.instance;
		ArmaResolution resolution = editorManager.getResolution();
		ArmaDisplay display = editorManager.getEditingDisplay();
		ArmaControl control = new ShortcutButtonControl(
				dialog.getClassName(), resolution, ExpressionEnvManager.instance.getEnv(),
				display
		);
		return new ControlTreeItemEntry(control);
	}

}
