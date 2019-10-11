package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 @since 7/23/19. */
public class StackLayout implements Layout{
	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());

	public StackLayout(@Nullable Layout parentLayout) {
		children.addListener(new LayoutChildrenListener(parentLayout != null ? parentLayout : this));
	}

	@Override
	public void recomputePositions() {

	}

	@Override
	public @NotNull ListObserver<LayoutNode> getChildren() {
		return null;
	}

	@Override
	public @NotNull String getName() {
		return null;
	}

	@Override
	public void invalidate() {

	}
}
