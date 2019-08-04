package com.armadialogcreator.canvas;

import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 @author Kayler
 @since 8/3/19. */
public interface UIRenderer {

	/**
	 Called before any {@link #paint(Graphics, RenderMode)}
	 and {@link #paintAfter(Graphics, RenderMode)} invocations.
	 Any painting/rendering done in this method will not be clipped ({@link Graphics#clip()}),
	 prior to this being called. This means you could render to any part of the whole canvas.
	 <p>
	 Each {@link UIRenderer} will get it's own {@link Graphics#save()} and {@link Graphics#restore()}
	 before this method is called.
	 <p>
	 Default implementation does nothing.

	 @param g graphics to use
	 @param mode mode to use
	 @see Graphics#clip()
	 */
	default void paintBackground(@NotNull Graphics g, @NotNull RenderMode mode) {

	}

	/**
	 Renders to the specified {@link Graphics}. This method CAN be clipped prior to this being called.
	 <p>
	 Each {@link UIRenderer} will get it's own {@link Graphics#save()} and {@link Graphics#restore()}
	 before this method is called.
	 <p>

	 @param g graphics to use
	 @param mode mode to use
	 @see Graphics#clip()
	 */
	void paint(@NotNull Graphics g, @NotNull RenderMode mode);

	/**
	 Called after all {@link #paint(Graphics, RenderMode)} and {@link #paintBackground(Graphics, RenderMode)} invocations.
	 Any painting/rendering done in this method will not be clipped ({@link Graphics#clip()}),
	 prior to this being called. This means you could render to any part of the whole canvas.
	 <p>
	 Each {@link UIRenderer} will get it's own {@link Graphics#save()} and {@link Graphics#restore()}
	 before this method is called.
	 <p>
	 Default implementation does nothing.

	 @param g graphics to use
	 @param mode mode to use
	 @see Graphics#clip()
	 */
	default void paintAfter(@NotNull Graphics g, @NotNull RenderMode mode) {

	}
}
