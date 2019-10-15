package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

/**
 @author Kayler
 @since 7/23/19. */
public class InnerLayout implements Layout {
	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>(1));
	private final NotNullValueObserver<LayoutNode>
	private final Layout parentLayout;
	private final Bounds bounds;

	public static final Function<Double, Double> NO_OP = aDouble -> {
		return aDouble;
	};

	public InnerLayout(@NotNull Layout parentLayout,
					   @NotNull Function<Double, Double> computeX,
					   @NotNull Function<Double, Double> computeY,
					   @NotNull Function<Double, Double> computeWidth,
					   @NotNull Function<Double, Double> computeHeight
	) {
		children.addListener(new LayoutChildrenListener(parentLayout));
		this.parentLayout = parentLayout;
		this.bounds = new SimpleBounds();
		parentLayout.getLayoutBounds().getXObserver().addListener((observer, oldValue, newValue) -> {
			InnerLayout.this.bounds.setX(computeX.apply(newValue));
		});
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
