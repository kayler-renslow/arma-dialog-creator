package com.armadialogcreator.gui.main.treeview.dataCreator;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.control.impl.XSliderControl;
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
 @since 7/21/2017 */
public class XSliderDataCreator implements TreeItemDataCreator<ArmaControl, UINodeTreeItemData> {
	public static final XSliderDataCreator INSTANCE = new XSliderDataCreator();

	@Nullable
	@Override
	public UINodeTreeItemData createNew(@NotNull EditableTreeView<ArmaControl, UINodeTreeItemData> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.XSlider, true);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = EditorManager.instance.getResolution();
		ArmaControl control = new XSliderControl(dialog.getClassName(), resolution, ExpressionEnvManager.instance.getEnv());
		return new ControlTreeItemEntry(control);
	}

}
