package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.impl.ShortcutButtonControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.ControlType;
import com.armadialogcreator.data.ApplicationData;
import com.armadialogcreator.data.DataKeys;
import com.armadialogcreator.data.Project;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.TreeItemEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 7/6/2017 */
public class ShortcutButtonDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {
	public static final ShortcutButtonDataCreator INSTANCE = new ShortcutButtonDataCreator();

	@Nullable
	@Override
	public TreeItemEntry createNew(@NotNull EditableTreeView<ArmaControl, TreeItemEntry> treeView) {
		NewControlDialog dialog = new NewControlDialog(
				ControlType.ShortcutButton,
				ArmaDialogCreator.getCanvasView().isBackgroundTreeView(treeView)
		);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}
		ApplicationData data = ApplicationData.getManagerInstance();
		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(data);
		ArmaControl control = new ShortcutButtonControl(
				dialog.getClassName(), resolution, data.getGlobalExpressionEnvironment(), Project.getCurrentProject()
		);
		return new ControlTreeItemEntry(control);
	}

}
