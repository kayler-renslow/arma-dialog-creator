package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeItemData;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeNodeUpdateListener;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.ControlTreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview.entry.TreeItemEntry;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/08/2016.
 */
public class ControlTreeItemData extends TreeItemData<TreeItemEntry> {
	private TreeItemControlGraphic graphic = (TreeItemControlGraphic) getGraphic();
	private ControlTreeItemEntry controlTreeItemEntry;

	public ControlTreeItemData(ControlTreeItemEntry controlTreeItemEntry) {
		super(controlTreeItemEntry.getTreeItemText(), CellType.LEAF, controlTreeItemEntry, new TreeItemControlGraphic());
		setUpdateListener(new TreeNodeUpdateListener() {
			@Override
			public void delete() {

			}

			@Override
			public void renamed(String newName) {
				controlTreeItemEntry.getMyArmaControl().setClassName(newName);
			}
		});
		this.controlTreeItemEntry = controlTreeItemEntry;
		graphic.init(this);
		controlTreeItemEntry.getMyArmaControl().getRenderer().getBackgroundColorObserver().addValueListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
				graphic.setBoxColor(newValue.toJavaFXColor());
			}
		});
	}

	/** Set whether or not the control is visible or not (updates the radio button as well) */
	public void setControlIsVisible(boolean visible) {
		controlTreeItemEntry.setGhost(!visible);
		graphic.updateVisibilityRadioButton(visible);
	}

	void updateVisibilityFromButton(boolean visible) {
		controlTreeItemEntry.setGhost(!visible);
	}

	String getControlTypeText() {
		return controlTreeItemEntry.getControlTypeText();
	}

	Color getPrimaryColor(){
		return controlTreeItemEntry.getMyArmaControl().getRenderer().getBackgroundColor();
	}
}
