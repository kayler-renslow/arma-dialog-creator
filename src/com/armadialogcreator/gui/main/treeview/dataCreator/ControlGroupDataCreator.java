package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.impl.ControlGroupControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlGroupTreeItemEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/04/2016.
 */
public class ControlGroupDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {

	public static final ControlGroupDataCreator INSTANCE = new ControlGroupDataCreator();

	private Env getEnv() {
		return ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment();
	}

	@Nullable
	@Override
	public TreeItemEntry createNew(@NotNull EditableTreeView<ArmaControl, TreeItemEntry> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.ControlsGroup, ArmaDialogCreator.getCanvasView().isBackgroundTreeView(treeView));
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData());

		return new ControlGroupTreeItemEntry(new ControlGroupControl(dialog.getClassName(), -1, resolution, getEnv(), Project.getCurrentProject()));
	}
}
