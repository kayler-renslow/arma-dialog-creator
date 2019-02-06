package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.control.impl.ButtonControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.core.old.ControlType;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.TreeItemEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 11/21/2016.
 */
public class ButtonDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {
	public static final ButtonDataCreator INSTANCE = new ButtonDataCreator();

	@Nullable
	@Override
	public TreeItemEntry createNew(@NotNull EditableTreeView<ArmaControl, TreeItemEntry> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.Button, ArmaDialogCreator.getCanvasView().isBackgroundTreeView(treeView));
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData());
		ButtonControl control = new ButtonControl(dialog.getClassName(), resolution, getEnv(), Project.getCurrentProject());
		return new ControlTreeItemEntry(control);
	}

	private Env getEnv() {
		return ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment();
	}
}
