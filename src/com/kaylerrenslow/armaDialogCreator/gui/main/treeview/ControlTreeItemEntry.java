package com.kaylerrenslow.armaDialogCreator.gui.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.data.Project;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fxcontrol.treeView.TreeNodeUpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public class ControlTreeItemEntry extends TreeItemEntry {
	private final ArmaControl myArmaControl;

	protected ControlTreeItemEntry(@NotNull CellType cellType, @Nullable Node graphic, @NotNull ArmaControl control) {
		super(control.getClassName(), cellType, graphic);
		this.myArmaControl = control;
		myArmaControl.getClassNameObserver().addListener((observer, oldValue, newValue) -> {
			setText(newValue);
		});
		myArmaControl.getRenderer().getEnabledObserver().addListener(new ValueListener<Boolean>() {
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
	public void duplicate(@NotNull TreeView<? extends TreeItemEntry> treeView) {
		String newName = myArmaControl.getClassName() + "_copy1";
		int i = 2;
		Project p = Project.getCurrentProject();
		while (p.findControlClassByName(newName) != null) {
			newName = myArmaControl.getClassName() + "_copy" + i;
			i++;
		}
		ArmaControl dup = myArmaControl.duplicate(newName, p);
		myArmaControl.getHolder().getControls().add(dup);
	}
}
