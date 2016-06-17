package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/07/2016.
 */
public class ControlTreeItemEntry implements TreeItemEntry {
	private final ArmaControl myArmaControl;

	public ControlTreeItemEntry(ArmaControl control) {
		myArmaControl = control;
	}

	@Override
	@NotNull
	public String getTreeItemText() {
		return myArmaControl.getClassName();
	}


	/** Sets the visibility and enable values. A ghost is not visible and is not enabled. */
	public void setGhost(boolean isGhost) {
		myArmaControl.getRenderer().setGhost(isGhost);
	}

	/**Sets whether or not the user can interact with the control in the editor.*/
	public void setEnabled(boolean enabled){
		myArmaControl.getRenderer().setEnabled(enabled);
	}

	public ArmaControl getMyArmaControl() {
		return myArmaControl;
	}

	@Override
	public boolean isPhantom() {
		return false;
	}

	public String getControlTypeText() {
		return myArmaControl.getType().displayName;
	}
}
