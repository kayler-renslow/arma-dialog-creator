package com.armadialogcreator.control;

import com.armadialogcreator.canvas.CanvasComponent;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/06/2019 */
public abstract class StructureUINode extends SimpleBaseUINode {

	public StructureUINode() {
	}

	@Override
	@Nullable
	public CanvasComponent getComponent() {
		return null;
	}
}
