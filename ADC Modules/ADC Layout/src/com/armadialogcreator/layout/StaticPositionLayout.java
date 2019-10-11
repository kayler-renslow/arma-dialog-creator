package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 A type of {@link Layout} where every {@link LayoutNode} manages it's own position. Margin and padding inside a {@link Bounds} has
 no effect. Each position is basically static and is completely unmanaged by the layout.

 @author Kayler
 @since 7/30/19. */
public class StaticPositionLayout implements Layout {

	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());

	public StaticPositionLayout() {
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
		node.getBounds().setHeight(height);
	}

	@Override
	@NotNull
	public ListObserver<LayoutNode> getChildren() {
		return children;
	}

	@Override
	public @NotNull String getName() {
		return "Static Position Layout";
	}

	@Override
	public void invalidate() {
		children.invalidate();
	}
}
