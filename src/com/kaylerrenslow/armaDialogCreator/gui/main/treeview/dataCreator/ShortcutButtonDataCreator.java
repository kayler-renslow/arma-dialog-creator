package com.kaylerrenslow.armaDialogCreator.gui.main.treeview.dataCreator;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ShortcutButtonControl;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.data.ApplicationData;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.newControl.NewControlDialog;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 7/6/2017 */
public class ShortcutButtonDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {
	public static final ShortcutButtonDataCreator INSTANCE = new ShortcutButtonDataCreator();

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
