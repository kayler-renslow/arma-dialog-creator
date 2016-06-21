package com.kaylerrenslow.armaDialogCreator.gui.fx.main.treeview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.CellType;
import com.kaylerrenslow.armaDialogCreator.gui.fx.control.treeView.TreeNodeUpdateListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 06/07/2016.
 */
public class ControlTreeItemEntry extends TreeItemEntry {
	private final ArmaControl myArmaControl;

	public ControlTreeItemEntry(@NotNull CellType cellType, @Nullable Node graphic, ArmaControl control) {
		super(control.getClassName(), cellType, graphic);
		this.myArmaControl = control;
	}

	public ControlTreeItemEntry(ArmaControl control) {
		this(CellType.LEAF, new TreeItemControlGraphic(), control);
		setUpdateListener(new TreeNodeUpdateListener() {
			@Override
			public void delete() {

			}

			@Override
			public void renamed(String newName) {
				getMyArmaControl().setClassName(newName);
			}
		});

		if (getGraphic() instanceof TreeItemControlGraphic) {
			TreeItemControlGraphic graphic = (TreeItemControlGraphic) getGraphic();
			graphic.init(this);
			control.getRenderer().getBackgroundColorObserver().addValueListener(new ValueListener<AColor>() {
				@Override
				public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
					graphic.setBoxColor(newValue.toJavaFXColor());
				}
			});
		}
	}

	/** Sets whether or not the user can interact with the control in the editor. */
	public void setEnabled(boolean enabled) {
		myArmaControl.getRenderer().setEnabled(enabled);
	}

	public ArmaControl getMyArmaControl() {
		return myArmaControl;
	}

	/** Set whether or not the control is visible or not (updates the radio button as well) */
	public void updateVisibilityFromButton(boolean visible) {
		// Sets the visibility and enable values. A ghost is not visible and is not enabled.
		myArmaControl.getRenderer().setGhost(!visible);
	}

	public String getControlTypeText() {
		return myArmaControl.getType().displayName;
	}

	public Color getPrimaryColor() {
		return myArmaControl.getRenderer().getBackgroundColor();
	}
}
