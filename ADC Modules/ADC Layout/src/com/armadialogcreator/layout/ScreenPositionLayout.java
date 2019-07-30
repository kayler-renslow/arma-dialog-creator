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
	private final Map<LayoutNode, Shape> shapes = new HashMap<>();

	public ScreenPositionLayout() {
		children.addListener((list, change) -> {
			switch (change.getChangeType()) {
				case Add: {
					ListObserverChangeAdd<LayoutNode> added = change.getAdded();
					shapes.putIfAbsent(added.getAdded(), new Shape());
					break;
				}
				case Set: {
					ListObserverChangeSet<LayoutNode> set = change.getSet();
					Shape remove = shapes.remove(set.getOld());
					shapes.put(set.getNew(), remove == null ? new Shape() : remove);
					break;
				}
				case Move: {
					ListObserverChangeMove<LayoutNode> moved = change.getMoved();
					if (moved.isSourceListChange()) {
						shapes.remove(moved.getMoved());
					} else {
						shapes.put(moved.getMoved(), new Shape());
					}
					break;
				}
				case Clear: {
					shapes.clear();
					break;
				}
				case Remove: {
					ListObserverChangeRemove<LayoutNode> removed = change.getRemoved();
					shapes.remove(removed.getRemoved());
					break;
				}
			}
		});
	}

	@Override
	public double computeWidth(@NotNull LayoutNode node) {
		return getShape(node).width;
	}

	@Override
	public double computeHeight(@NotNull LayoutNode node) {
		return getShape(node).height;
	}

	@Override
	public double computeX(@NotNull LayoutNode node) {
		return getShape(node).x;
	}

	@Override
	public double computeY(@NotNull LayoutNode node) {
		return getShape(node).y;
	}

	@Override
	public void recomputePositions() {
		// do nothing
	}

	public void setX(@NotNull LayoutNode node, double x) {
		Shape shape = getShape(node);
		shape.x = x;
		node.getBounds().setX(x);
	}

	public void setY(@NotNull LayoutNode node, double y) {
		Shape shape = getShape(node);
		shape.y = y;
		node.getBounds().setY(y);
	}

	public void setWidth(@NotNull LayoutNode node, double width) {
		Shape shape = getShape(node);
		shape.width = width;
		node.getBounds().setWidth(width);
	}

	public void setHeight(@NotNull LayoutNode node, double height) {
		Shape shape = getShape(node);
		shape.height = height;
		node.getBounds().setHeight(height);
	}

	@NotNull
	@Override
	public Shape getShape(@NotNull LayoutNode node) {
		Shape shape = shapes.get(node);
		if (shape == null) {
			throw new IllegalArgumentException("Node doesn't belong to this layout");
		}
		return shape;
	}

	@Override
	@NotNull
	public ListObserver<LayoutNode> getChildren() {
		return children;
	}

}
