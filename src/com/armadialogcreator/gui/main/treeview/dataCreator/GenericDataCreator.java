package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.control.ArmaControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.control.ControlType;
import com.armadialogcreator.control.SpecificationRegistry;
import com.armadialogcreator.data.DataKeys;
import com.armadialogcreator.data.Project;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.TreeItemEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 7/27/2017 */
public abstract class GenericDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {
	private final ControlType type;

	public GenericDataCreator(@NotNull ControlType type) {
		this.type = type;
	}

	@Nullable
	@Override
	public TreeItemEntry createNew(@NotNull EditableTreeView<ArmaControl, TreeItemEntry> treeView) {
		NewControlDialog dialog = new NewControlDialog(type, ArmaDialogCreator.getCanvasView().isBackgroundTreeView(treeView));
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData());
		return new ControlTreeItemEntry(create(dialog.getClassName(), resolution, getEnv(), Project.getCurrentProject()));
	}

	private Env getEnv() {
		return ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment();
	}

	@NotNull
	protected abstract ArmaControl create(@NotNull String className, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry);
}
