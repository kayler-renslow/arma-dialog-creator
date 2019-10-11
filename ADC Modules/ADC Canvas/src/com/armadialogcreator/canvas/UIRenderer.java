package com.armadialogcreator.canvas;

import com.armadialogcreator.layout.Bounds;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 8/3/19. */
public interface UIRenderer extends MouseEventHandler {
	@NotNull
	Iterable<RenderAnchorPoint> iterateAnchorPoints();

	/** @return this renderer's {@link RenderAnchorPoint} */
	@NotNull RenderAnchorPoint getAnchorPoint();

	@NotNull Bounds getBounds();

	/** Return true if the node is enabled (user can click on it or move it with mouse), false otherwise. */
	boolean isEnabled();

	/**
	 Set whether is enabled or not.

	 @see #setGhost(boolean)
	 */
	void setEnabled(boolean enabled);

	/**
	 Returns true if is invisible and is disabled, false otherwise
	 */
	boolean isGhost();

	/**
	 Sets the visibility and enable values. A ghost is not visible and is not enabled.
	 */
	void setGhost(boolean ghost);

	/** @return the update group for when a re-render is requested for this renderer */
	@NotNull UpdateListenerGroup<UpdateListenerGroup.NoData> getRenderUpdateGroup();

	@Nullable Border getBorder();

	@NotNull Color getBackgroundColor();

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
