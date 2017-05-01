package com.kaylerrenslow.armaDialogCreator.gui.main.treeview.dataCreator;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ButtonControl;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.data.DataKeys;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.EditableTreeView;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeItemDataCreator;
import com.kaylerrenslow.armaDialogCreator.gui.main.popup.newControl.NewControlDialog;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.main.treeview.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 11/21/2016.
 */
public class ButtonDataCreator implements TreeItemDataCreator<ArmaControl, TreeItemEntry> {
	public static final ButtonDataCreator INSTANCE = new ButtonDataCreator();

	@NotNull
	@Override
	public TreeItemEntry createNew(@NotNull EditableTreeView<ArmaControl, TreeItemEntry> treeView) {
		NewControlDialog dialog = new NewControlDialog(ControlType.BUTTON, ArmaDialogCreator.getMainWindow().getCanvasView().getBackgroundControlTreeView() == treeView);
		dialog.show();
		if (dialog.wasCancelled()) {
			return null;
		}

		ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(ArmaDialogCreator.getApplicationData());
		ButtonControl control = new ButtonControl(dialog.getClassName(), 0, resolution, getEnv(), Project.getCurrentProject());
		return new ControlTreeItemEntry(control);
	}

	private Env getEnv() {
		return ArmaDialogCreator.getApplicationData().getGlobalExpressionEnvironment();
	}
}
