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
	private final StackLayout top = new StackLayout();
	private final StackLayout right = new StackLayout();
	private final StackLayout bottom = new StackLayout();
	private final StackLayout left = new StackLayout();
	private final StackLayout center = new StackLayout();
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
		top.getChildren().clearListeners();
		right.getChildren().clearListeners();
		bottom.getChildren().clearListeners();
		left.getChildren().clearListeners();
		center.getChildren().clearListeners();


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
		map.forEach((alignment, stackLayout) -> {
			switch (alignment) {
				case TopCenter: {
					final double lcx = layoutBounds.getCenterX();
					final double mh = 0.25 * layoutBounds.getHeight();
					final double mw = layoutBounds.getWidth();
					for (LayoutNode node : stackLayout.getChildren()) {
						NodeBounds nodeBounds = node.getBounds();
						nodeBounds.maxWidth = mw;
						nodeBounds.setX(lcx - nodeBounds.getWidth() / 2);


						nodeBounds.maxHeight = mh;
						nodeBounds.setY(0);
					}
					break;
				}
				case RightCenter: {
					final double mh = layoutBounds.getHeight();
					final double x = 0.75 * layoutBounds.getWidth();
					final double mw = 0.25 * layoutBounds.getWidth();
					for (LayoutNode node : stackLayout.getChildren()) {
						NodeBounds nodeBounds = node.getBounds();
						nodeBounds.maxWidth = mw;
						nodeBounds.setX(x);


						nodeBounds.maxHeight = mh;
						nodeBounds.setY(nodeBounds.getHeight());
					}
					break;
				}
				case BottomCenter: {
					break;
				}
				case LeftCenter: {
					break;
				}
				case Center: {
					break;
				}
				default: {
					throw new IllegalStateException(); // ?
				}
			}
		});
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
