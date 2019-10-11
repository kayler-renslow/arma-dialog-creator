package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 A type of {@link Layout} where every {@link LayoutNode} manages it's own position. Margin and padding inside a {@link Bounds} has
 no effect. Each position is basically static and is completely unmanaged by the layout.

 @author Kayler
 @since 7/30/19. */
public class StaticPositionLayout implements Layout {

	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());
	private final Bounds layoutBounds = new SimpleBounds();
	private final NotNullValueObserver<Alignment> alignment = new NotNullValueObserver<>(Alignment.Center);

	public StaticPositionLayout() {
		children.addListener(new LayoutChildrenListener(this));
		layoutBounds.setWidth(100);
		layoutBounds.setMinWidth(100);
		layoutBounds.setMaxWidth(100);
		layoutBounds.setHeight(100);
		layoutBounds.setMaxHeight(100);
		layoutBounds.setMinHeight(100);
		layoutBounds.setX(0);
		layoutBounds.setY(0);
	}

	@Override
	public void recomputePositions() {
		// do nothing
	}

	public void setX(@NotNull LayoutNode node, double x) {
		node.getBounds().x = x;
	}

	public void setY(@NotNull LayoutNode node, double y) {
		node.getBounds().y = y;
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
	@NotNull
	public String getName() {
		return "Static Position Layout";
	}

	@Override
	@NotNull
	public Bounds getLayoutBounds() {
		return layoutBounds;
	}

	@Override
	@NotNull
	public NotNullValueObserver<Alignment> getAlignment() {
		return alignment;
	}

	@Override
	public void invalidate() {
		children.invalidate();
	}
}
