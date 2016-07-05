package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListener;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/14/2016.
 */
public class UICanvasPreview extends UICanvas {

	private final UpdateListener<Object> controlListener = new UpdateListener<Object>() {
		@Override
		public void update(Object data) {
			paint();
		}
	};

	public UICanvasPreview(int width, int height) {
		super(width, height);
	}

	@Override
	public void addComponentNoPaint(@NotNull CanvasComponent component) {
		super.addComponentNoPaint(component);
		if (component instanceof ArmaControlRenderer) {
			ArmaControlRenderer renderer = (ArmaControlRenderer) component;
			renderer.getMyControl().getUpdateGroup().addListener(controlListener);
		}
	}


	@Override
	public void addComponent(@NotNull CanvasComponent component) {
		addComponentNoPaint(component); //intentionally using addComponentNoPaint so that there is less duplicate code
		paint();
	}


	/**
	 Removes the given component from the canvas render and user interaction. This also removes the component from the selection

	 @param component component to remove
	 @return true if the component was removed, false if nothing was removed
	 */
	public boolean removeComponent(@NotNull CanvasComponent component) {
		boolean removed = removeComponentNoPaint(component);
		paint();
		return removed;
	}

	@Override
	public boolean removeComponentNoPaint(@NotNull CanvasComponent component) {
		boolean removed = super.removeComponentNoPaint(component);
		if (removed) {
			if (component instanceof ArmaControlRenderer) {
				ArmaControlRenderer renderer = (ArmaControlRenderer) component;
				renderer.getMyControl().getUpdateGroup().removeUpdateListener(controlListener);
			}
		}
		return removed;
	}

	@Override
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {

	}

	@Override
	protected void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb) {

	}

	@Override
	protected void mouseMoved(int mousex, int mousey) {

	}
}
