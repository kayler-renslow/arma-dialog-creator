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
	private final Map<Alignment, InnerLayout> map = new HashMap<>(5);
	private final InnerLayout top, right, bottom, left, center;
	private final Bounds layoutBounds;
	private final NotNullValueObserver<Alignment> alignment = new NotNullValueObserver<>(Alignment.Center);

	private static final double percentage = 0.25;
	private static final double remainderPercentage = 0.50; // 1 - 0.25 * 2

	public BorderLayout(@NotNull Bounds layoutBounds) {
		this.layoutBounds = layoutBounds;
		children.addListener(new LayoutChildrenListener(this));

		top = new InnerLayout(this);
		right = new InnerLayout(this);
		bottom = new InnerLayout(this);
		left = new InnerLayout(this);
		center = new InnerLayout(this);

		final Bounds myBounds = BorderLayout.this.getLayoutBounds();

		top.setComputeHeight(aDouble -> computeInnerHeightTopBot());

		right.setComputeHeight(aDouble -> computeInnerHeightLeftRight());
		right.setComputeWidth(aDouble -> computeInnerWidthLeftRight());

		bottom.setComputeY(borderLayoutYPos -> {
			return myBounds.getBottomY() - computeInnerHeightTopBot();
		});
		bottom.setComputeHeight(aDouble -> computeInnerHeightTopBot());


		left.setComputeHeight(aDouble -> computeInnerHeightLeftRight());
		left.setComputeWidth(aDouble -> computeInnerWidthLeftRight());

		center.setComputeX(aDouble -> getLayoutBounds().getX() * percentage);
		center.setComputeY(aDouble -> getLayoutBounds().getY() * percentage);
		center.setComputeWidth(aDouble -> remainderPercentage * getLayoutBounds().getWidth());
		center.setComputeHeight(aDouble -> remainderPercentage * getLayoutBounds().getHeight());

		map.put(Alignment.Center, center);
		map.put(Alignment.TopCenter, top);
		map.put(Alignment.RightCenter, right);
		map.put(Alignment.BottomCenter, bottom);
		map.put(Alignment.LeftCenter, left);


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

				InnerLayout removedFrom = removeNodeFromAny(oldNode);
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

	private double computeInnerHeightLeftRight() {
		return getLayoutBounds().getHeight() * remainderPercentage;
	}

	private double computeInnerHeightTopBot() {
		return getLayoutBounds().getHeight() * percentage;
	}

	private double computeInnerWidthLeftRight() {
		return getLayoutBounds().getWidth() * percentage;
	}

	@Nullable
	private InnerLayout removeNodeFromAny(LayoutNode removedNode) {
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
