package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.NotNullValueObserver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 @author Kayler
 @since 7/23/19. */
public class StackLayout implements Layout{
	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());

	public StackLayout() {
		children.addListener(new LayoutChildrenListener(this));
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
	public @NotNull Bounds getLayoutBounds() {
		return null;
	}

	@Override
	public @NotNull NotNullValueObserver<Alignment> getAlignment() {
		return null;
	}

	@Override
	public void invalidate() {

	}
}
