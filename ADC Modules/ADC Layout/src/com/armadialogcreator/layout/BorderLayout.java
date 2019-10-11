package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.ListObserverChangeMove;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 @author Kayler
 @since 7/23/19. */
public class BorderLayout implements Layout {
	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());
	private final Map<Alignment, StackLayout> map = new HashMap<>(5);
	private final StackLayout top = new StackLayout(this);
	private final StackLayout right = new StackLayout(this);
	private final StackLayout bottom = new StackLayout(this);
	private final StackLayout left = new StackLayout(this);
	private final StackLayout center = new StackLayout(this);
	private final Bounds layoutBounds;
	private final NotNullValueObserver<Alignment> alignment = new NotNullValueObserver<>(Alignment.Center);


	public BorderLayout(@NotNull Bounds layoutBounds) {
		this.layoutBounds = layoutBounds;
		children.addListener(new LayoutChildrenListener(this));

		map.put(Alignment.Center, center);
		map.put(Alignment.TopCenter, top);
		map.put(Alignment.RightCenter, right);
		map.put(Alignment.BottomCenter, bottom);
		map.put(Alignment.LeftCenter, left);

		// remove listeners so that the stack layouts don't assign bounds
		top.setParentLayout(this);
		right.setParentLayout(this);
		bottom.setParentLayout(this);
		left.setParentLayout(this);
		center.setParentLayout(this);

		top.getLayoutBounds().setX(layoutBounds.getX());
		// todo assign the stack pane bounds and then we don't need to calc the positions :set stackpane bounds


		children.addListener((list, change) -> {
			if (change.wasAdded()) {
				LayoutNode addedNode = change.getAdded().getAdded();
				center.getChildren().add(addedNode);
			} else if (change.wasRemoved()) {
				LayoutNode removedNode = change.getRemoved().getRemoved();
				removeNodeFromAny(removedNode);
			} else if (change.wasSet()) {
				LayoutNode oldNode = change.getSet().getOld();
				LayoutNode newNode = change.getSet().getNew();

				StackLayout removedFrom = removeNodeFromAny(oldNode);
				if (removedFrom != null) {
					removedFrom.getChildren().add(newNode);
				} else {
					center.getChildren().add(newNode);
				}
			} else if (change.wasMoved()) {
				ListObserverChangeMove<LayoutNode> moveChange = change.getMoved();
				if (moveChange.isSourceListChange()) {
					removeNodeFromAny(moveChange.getMoved());
				} else {
					center.getChildren().add(moveChange.getMoved());
				}
			} else if (change.wasCleared()) {
				top.getChildren().clear();
				right.getChildren().clear();
				bottom.getChildren().clear();
				left.getChildren().clear();
				center.getChildren().clear();
			} else {
				throw new IllegalStateException(); // ?
			}

		});
	}

	@Nullable
	private StackLayout removeNodeFromAny(LayoutNode removedNode) {
		boolean removed = top.getChildren().remove(removedNode);
		if (removed) {
			return top;
		}
		removed = right.getChildren().remove(removedNode);
		if (removed) {
			return right;
		}
		removed = bottom.getChildren().remove(removedNode);
		if (removed) {
			return bottom;
		}
		removed = left.getChildren().remove(removedNode);
		if (removed) {
			return left;
		}
		removed = center.getChildren().remove(removedNode);
		if (removed) {
			return center;
		}
		return null;
	}

	@Override
	public void recomputePositions() {
		// todo see :set stackpane bounds
	}

	@Override
	@NotNull
	public ListObserver<LayoutNode> getChildren() {
		return children;
	}

	@Override
	public @NotNull String getName() {
		return "Border Layout";
	}

	@Override
	public void invalidate() {
		children.invalidate();
		top.invalidate();
		right.invalidate();
		bottom.invalidate();
		left.invalidate();
		center.invalidate();
		map.clear();
	}

	@NotNull
	@Override
	public Bounds getLayoutBounds() {
		return layoutBounds;
	}

	@Override
	@NotNull
	public NotNullValueObserver<Alignment> getAlignment() {
		return alignment;
	}
}
