package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

/**
 @author Kayler
 @since 7/23/19. */
public class InnerLayout implements Layout {
	public static final Function<Double, Double> NO_OP = aDouble -> {
		return aDouble;
	};
	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>(1));
	private final Layout parentLayout;
	private final Bounds bounds;
	private Function<Double, Double> computeX, computeY, computeWidth, computeHeight;


	public InnerLayout(@NotNull Layout parentLayout) {
		computeX = computeY = computeWidth = computeHeight = NO_OP;

		children.addListener(new LayoutChildrenListener(parentLayout));
		this.parentLayout = parentLayout;
		this.bounds = new SimpleBounds();
		parentLayout.getLayoutBounds().getXObserver().addListener((observer, oldValue, newValue) -> {
			InnerLayout.this.bounds.setX(computeX.apply(newValue));
		});
		parentLayout.getLayoutBounds().getYObserver().addListener((observer, oldValue, newValue) -> {
			InnerLayout.this.bounds.setY(computeY.apply(newValue));
		});
		parentLayout.getLayoutBounds().getWidthObserver().addListener((observer, oldValue, newValue) -> {
			InnerLayout.this.bounds.setWidth(computeWidth.apply(newValue));
		});
		parentLayout.getLayoutBounds().getHeightObserver().addListener((observer, oldValue, newValue) -> {
			InnerLayout.this.bounds.setHeight(computeHeight.apply(newValue));
		});
	}

	public void setComputeX(@Nullable Function<Double, Double> computeX) {
		this.computeX = computeX;
	}

	public void setComputeY(@Nullable Function<Double, Double> computeY) {
		this.computeY = computeY;
	}

	public void setComputeWidth(@Nullable Function<Double, Double> computeWidth) {
		this.computeWidth = computeWidth;
	}

	public void setComputeHeight(@Nullable Function<Double, Double> computeHeight) {
		this.computeHeight = computeHeight;
	}

	@Override
	public void recomputePositions() {
		// do nothing
	}

	@Override
	@NotNull
	public ListObserver<LayoutNode> getChildren() {
		return children;
	}

	@Override
	@NotNull
	public String getName() {
		return parentLayout.getName() + ".inner";
	}

	@Override
	@NotNull
	public Bounds getLayoutBounds() {
		return bounds;
	}

	@Override
	@NotNull
	public NotNullValueObserver<Alignment> getAlignment() {
		return parentLayout.getAlignment();
	}

	@Override
	public void invalidate() {
		children.invalidate();
	}


}
