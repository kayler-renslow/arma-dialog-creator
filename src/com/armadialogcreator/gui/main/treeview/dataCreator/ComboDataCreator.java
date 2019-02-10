package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.impl.ComboControl;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.olddata.Project;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since July 23, 2017 */
public class ComboDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {
	public static final ComboDataCreator INSTANCE = new ComboDataCreator();

	@Nullable
	@Override
	public TreeItemEntry createNew(@NotNull EditableTreeView<ArmaControl, TreeItemEntry> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.Combo, ArmaDialogCreator.getCanvasView().isBackgroundTreeView(treeView));
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData());
		ArmaControl control =
				new ComboControl(dialog.getClassName(), resolution, getEnv(), Project.getCurrentProject());
		return new ControlTreeItemEntry(control);
	}

	private Env getEnv() {
		return ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment();
	}
}
