package com.armadialogcreator.gui.main.treeview;

import com.armadialogcreator.control.ArmaControl;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.data.ConfigClassRegistry;
import com.armadialogcreator.gui.fxcontrol.treeView.CellType;
import com.armadialogcreator.gui.fxcontrol.treeView.TreeNodeUpdateListener;
import com.armadialogcreator.util.ValueListener;
import com.armadialogcreator.util.ValueObserver;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public class ControlTreeItemEntry extends UINodeTreeItemData {
	private final ArmaControl myArmaControl;

	protected ControlTreeItemEntry(@NotNull CellType cellType, @Nullable Node graphic, @NotNull ArmaControl control) {
		super(control.getClassName(), cellType, graphic, control);
		this.myArmaControl = control;
		myArmaControl.getClassNameObserver().addListener((observer, oldValue, newValue) -> {
			setText(newValue);
		});
		myArmaControl.getRenderer().getEnabledObserver().addListener(new ValueListener<>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<Boolean> observer, Boolean oldValue, Boolean newValue) {
				setEnabledFromListener(newValue);
			}
		});
		if (getGraphic() instanceof DefaultControlTreeItemGraphic) {
			DefaultControlTreeItemGraphic graphic1 = (DefaultControlTreeItemGraphic) getGraphic();
			graphic1.init(this);
		}
	}

	public ControlTreeItemEntry(@NotNull ArmaControl control) {
		this(CellType.LEAF, new DefaultControlTreeItemGraphic(), control);
		setUpdateListener(new TreeNodeUpdateListener() {
			@Override
			public void delete() {

			}

			@Override
			public void renamed(String newName) {
				getMyArmaControl().setClassName(newName);
			}
		});
	}

	/** Sets whether or not the user can interact with the control in the editor. */
	public void setEnabled(boolean enabled) {
		myArmaControl.getRenderer().setEnabled(enabled);
	}

	private void setEnabledFromListener(boolean enabled) {
		if (myArmaControl.getRenderer().isGhost()) { //only adjusted the visibility, so don't disable the graphic
			return;
		}
		if (getGraphic() instanceof DefaultControlTreeItemGraphic) {
			DefaultControlTreeItemGraphic graphic1 = (DefaultControlTreeItemGraphic) getGraphic();
			graphic1.setGraphicIsEnabled(enabled);
		} else {
			getGraphic().setDisable(!enabled);
		}
	}

	@NotNull
	public ArmaControl getMyArmaControl() {
		return myArmaControl;
	}

	/** Set whether or not the control is visible or not (updates the radio button as well) */
	public void setVisible(boolean visible) {
		// Sets the visibility and enable values. A ghost is not visible and is not enabled.
		myArmaControl.getRenderer().setGhost(!visible);
	}

	@NotNull
	public Color getPrimaryColor() {
		return myArmaControl.getRenderer().getBackgroundColor();
	}

	/**
	 @return the control's enabled state that this tree item entry represents
	 @see ArmaControlRenderer#isEnabled()
	 */
	public boolean isEnabled() {
		return myArmaControl.getRenderer().isEnabled();
	}

	/**
	 @return the control's ghost state that this tree item entry represents
	 @see ArmaControlRenderer#isGhost()
	 */
	public boolean isGhost() {
		return myArmaControl.getRenderer().isGhost();
	}

	@Override
	public void duplicate(@NotNull TreeView<? extends UINodeTreeItemData> treeView) {
		String newName = myArmaControl.getClassName() + "_copy1";
		int i = 2;
		ConfigClassRegistry registry = ConfigClassRegistry.instance;
		while (registry.findConfigClassByName(newName) != null) {
			newName = myArmaControl.getClassName() + "_copy" + i;
			i++;
		}
		ArmaControl dup = myArmaControl.duplicate(newName);
		dup.setParentNode(myArmaControl.getParentNode());
		myArmaControl.getParentNode().addChild(dup);
	}
}
