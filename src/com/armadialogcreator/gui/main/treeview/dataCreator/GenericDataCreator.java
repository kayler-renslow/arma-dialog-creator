package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.ControlType;
import com.armadialogcreator.data.EditorManager;
import com.armadialogcreator.data.ExpressionEnvManager;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.gui.fxcontrol.treeView.EditableTreeView;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.armadialogcreator.gui.main.popup.newControl.NewControlDialog;
import com.armadialogcreator.gui.main.treeview.ControlTreeItemEntry;
import com.armadialogcreator.gui.main.treeview.UINodeTreeItemData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 7/27/2017 */
public abstract class GenericDataCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {
	private final ControlType type;

	public GenericDataCreator(@NotNull ControlType type) {
		this.type = type;
	}

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlDialog dialog = new NewControlDialog(type, true);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = EditorManager.instance.getResolution();
		return new ControlTreeItemEntry(create(dialog.getClassName(), resolution, ExpressionEnvManager.instance.getEnv()));
	}

	@NotNull
	protected abstract ArmaControl create(@NotNull String className, @NotNull ArmaResolution resolution, @NotNull Env env);
}
