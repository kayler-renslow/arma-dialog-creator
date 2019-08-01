package com.armadialogcreator.layout;

import com.armadialogcreator.util.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 @author Kayler
 @since 7/30/19. */
public class ScreenPositionLayout implements Layout {

	public static final ScreenPositionLayout SHARED = new ScreenPositionLayout();

	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());

	public ScreenPositionLayout() {
		children.addListener(new LayoutChildrenListener(this));
	}

	@Override
	public void recomputePositions() {
		// do nothing
	}

	public void setX(@NotNull LayoutNode node, double x) {
		node.getBounds().setX(x);
	}

	public void setY(@NotNull LayoutNode node, double y) {
		node.getBounds().setY(y);
	}

	public void setWidth(@NotNull LayoutNode node, double width) {
		node.getBounds().setWidth(width);
	}

	public void setHeight(@NotNull LayoutNode node, double height) {
		Bounds bounds = node.getBounds();
		bounds.setHeight(height);
	}

	@Override
	@NotNull
	public ListObserver<LayoutNode> getChildren() {
		return children;
	}

}
