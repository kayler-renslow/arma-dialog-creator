package com.armadialogcreator.layout;

import com.armadialogcreator.util.ListObserver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 @author Kayler
 @since 7/23/19. */
public class BorderLayout implements Layout {
	private final ListObserver<LayoutNode> children = new ListObserver<>(new ArrayList<>());

	public BorderLayout() {
		children.addListener(new LayoutChildrenListener(this));
	}

	@Override
	public void recomputePositions() {

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
	}
}
