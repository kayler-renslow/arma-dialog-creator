package com.armadialogcreator.canvas;

import com.armadialogcreator.layout.Bounds;
import org.jetbrains.annotations.NotNull;

/**
 A partial or full segment within a visual position of a {@link Bounds}.

 @author Kayler
 @since 10/11/19. */
public abstract class RenderAnchorPoint {
	protected final Bounds bounds;

	public RenderAnchorPoint(@NotNull Bounds bounds) {
		this.bounds = bounds;
	}

	@NotNull
	public abstract UIRenderer getRenderer();

	public abstract void addLeft(@NotNull UIRenderer renderer);

	public abstract void addRight(@NotNull UIRenderer renderer);
}
