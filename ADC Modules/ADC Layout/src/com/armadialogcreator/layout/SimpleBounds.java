package com.armadialogcreator.layout;

import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 Specifies how a {@link LayoutNode} is structured within a layout.

 @author Kayler
 @since 7/23/19. */
public class SimpleBounds implements Bounds {
	private @NotNull Insets margin = Insets.NONE;
	private @NotNull Insets padding = Insets.NONE;
	private final NotNullValueObserver<Double> xObs = new NotNullValueObserver<>(0d);
	private final NotNullValueObserver<Double> yObs = new NotNullValueObserver<>(0d);
	private final NotNullValueObserver<Double> widthObs = new NotNullValueObserver<>(0d);
	private final NotNullValueObserver<Double> heightObs = new NotNullValueObserver<>(0d);

	private double minWidth, maxWidth;
	private double minHeight, maxHeight;

	public SimpleBounds() {
	}


	/** @return the minimum width of the node */
	@Override
	public double getMinWidth() {
		return minWidth;
	}


	/** @see #getMinWidth() */
	@Override
	public void setMinWidth(double minWidth) {
		this.minWidth = minWidth;
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
	}


	@Override
	@NotNull
	public NotNullValueObserver<Double> getXObserver() {
		return xObs;
	}

	@Override
	@NotNull
	public NotNullValueObserver<Double> getYObserver() {
		return yObs;
	}

	@Override
	@NotNull
	public NotNullValueObserver<Double> getWidthObserver() {
		return widthObs;
	}

	@Override
	@NotNull
	public NotNullValueObserver<Double> getHeightObserver() {
		return heightObs;
	}

}