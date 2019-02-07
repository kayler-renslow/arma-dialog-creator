package com.armadialogcreator.control;

import com.armadialogcreator.canvas.CanvasComponent;
import com.armadialogcreator.canvas.UINode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/06/2019 */
public abstract class StructureUINode extends SimpleBaseUINode {

	public StructureUINode(@NotNull UINode rootNode) {
		super(rootNode);
	}

	@Override
	@Nullable
	public CanvasComponent getComponent() {
		return null;
	}
}
