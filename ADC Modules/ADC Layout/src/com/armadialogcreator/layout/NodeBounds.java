package com.armadialogcreator.layout;

import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Specifies how a {@link LayoutNode} is structured within a layout.

 @author Kayler
 @since 7/23/19. */
public class NodeBounds implements Bounds {
	private @NotNull Insets margin = Insets.NONE;
	private @NotNull Insets padding = Insets.NONE;

	protected double minWidth, maxWidth;
	protected double minHeight, maxHeight;
	private final @NotNull LayoutNode node;
	private final @Nullable Layout layout;

	NodeBounds(@NotNull LayoutNode node, @Nullable Layout layout) {
		this.node = node;
		this.layout = layout;

		this.getXObserver().addListener((observer, oldValue, newValue) -> {
			recomputePositionFromLayout();
		});
		this.getYObserver().addListener((observer, oldValue, newValue) -> {
			recomputePositionFromLayout();
		});
		this.getWidthObserver().addListener((observer, oldValue, newValue) -> {
			recomputePositionFromLayout();
		});
		this.getHeightObserver().addListener((observer, oldValue, newValue) -> {
			recomputePositionFromLayout();
		});
	}


	/**
	 Creates a new instance from an existing instance. The following attributes will be copied from the
	 provided bounds into this bounds:
	 <ul>
	 <li>{@link #getMargin()}</li>
	 <li>{@link #getPadding()}</li>
	 <li>{@link #getMinHeight()}</li>
	 <li>{@link #getHeight()}</li>
	 <li>{@link #getMaxHeight()}</li>
	 <li>{@link #getMinWidth()}</li>
	 <li>{@link #getWidth()}</li>
	 <li>{@link #getMaxWidth()}</li>
	 </ul>

	 @param copy instance to copy attributes from
	 @param node node to assign the bounds to
	 @param layout layout that owns this bounds, or null if no layout owns this bounds
	 */
	NodeBounds(@NotNull NodeBounds copy, @NotNull LayoutNode node, @Nullable Layout layout) {
		this.node = node;
		this.layout = layout;
		this.margin = copy.margin;
		this.padding = copy.padding;
		this.minHeight = copy.minHeight;
		this.minWidth = copy.minWidth;
		this.setWidth(copy.getWidth());
		this.setHeight(copy.getHeight());
		this.maxHeight = copy.maxHeight;
		this.maxWidth = copy.maxWidth;
	}

	private void recomputePositionFromLayout() {
		if (this.layout != null) {
			this.layout.recomputePositions();
		}
	}

	@Override
	public double getMinWidth() {
		return this.minWidth;
	}

	/** @see #getMinWidth() */
	@Override
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
		recomputePositionFromLayout();
	}

	/** @return the maximum width of the node */
	@Override
	public double getMaxWidth() {
		return maxWidth;
	}

	/** @see #getMaxWidth() */
	@Override
	public void setMaxWidth(double maxWidth) {
		this.maxWidth = maxWidth;
		recomputePositionFromLayout();
	}

	/** @return the minimum height of the node */
	@Override
	public double getMinHeight() {
		return minHeight;
	}

	/** @see #getMinHeight() */
	@Override
	public void setMinHeight(double minHeight) {
		this.minHeight = minHeight;
		recomputePositionFromLayout();
	}

	/** @return the maximum height of the node */
	@Override
	public double getMaxHeight() {
		return maxHeight;
	}

	/** @see #getMaxHeight() */
	@Override
	public void setMaxHeight(double maxHeight) {
		this.maxHeight = maxHeight;
		recomputePositionFromLayout();
	}

	/** @return the margin of this bounds */
	@Override
	@NotNull
	public Insets getMargin() {
		return margin;
	}

	/** @see #getMargin() */
	@Override
	public void setMargin(@NotNull Insets margin) {
		this.margin = margin;
		recomputePositionFromLayout();
	}

	/** @return the padding of this bounds */
	@Override
	@NotNull
	public Insets getPadding() {
		return padding;
	}

	/** @see #getPadding() */
	@Override
	public void setPadding(@NotNull Insets padding) {
		this.padding = padding;
		recomputePositionFromLayout();
	}

	@Override
	public @NotNull NotNullValueObserver<Double> getXObserver() {
		return null;
	}

	@Override
	public @NotNull NotNullValueObserver<Double> getYObserver() {
		return null;
	}

	@Override
	public @NotNull NotNullValueObserver<Double> getWidthObserver() {
		return null;
	}

	@Override
	public @NotNull NotNullValueObserver<Double> getHeightObserver() {
		return null;
	}

	@NotNull
	public LayoutNode getNode() {
		return node;
	}

	/**
	 Get A new instance with the attributes copied and assign the optional new layout.
	 The new instance will have the same {@link #getNode()} instance
	 */
	@NotNull
	public NodeBounds copy(@Nullable Layout newLayout) {
		return new NodeBounds(this, this.node, newLayout);
	}
}