package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlClass;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/14/2016.
 */
public class UICanvasPreview extends UICanvas {
	private final ValueListener<ArmaControlClass> CONTROL_LISTENER = new ValueListener<ArmaControlClass>() {
		@Override
		public void valueUpdated(@NotNull ValueObserver<ArmaControlClass> observer, ArmaControlClass oldValue, ArmaControlClass newValue) {
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
			renderer.getMyControl().getControlListener().addValueListener(CONTROL_LISTENER);
		}
	}

	@Override
	public void addComponent(@NotNull CanvasComponent component) {
		super.addComponent(component);
		if (component instanceof ArmaControlRenderer) {
			ArmaControlRenderer renderer = (ArmaControlRenderer) component;
			renderer.getMyControl().getControlListener().addValueListener(CONTROL_LISTENER);
		}
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
