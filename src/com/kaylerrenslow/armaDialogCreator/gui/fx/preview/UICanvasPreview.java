package com.kaylerrenslow.armaDialogCreator.gui.fx.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlClass;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.display.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasComponent;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import javafx.collections.ObservableList;
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
	private ArmaDisplay display;

	public UICanvasPreview(int width, int height) {
		super(width, height);
	}

	public void setDisplay(ArmaDisplay display){
		this.display = display;
		paint();
	}

	@Override
	protected void paintComponents() {
		super.paintComponents();
		paintControls(display.getControls());
	}

	private void paintControls(ObservableList<ArmaControl> controls) {
		for(ArmaControl control : controls){
			control.getRenderer().paint(gc);
		}
	}

	@Override
	public void addComponentNoPaint(@NotNull CanvasComponent component) {
		if (component instanceof ArmaControlRenderer) {
			throw new IllegalArgumentException("Do not add the controls this way. The controls that should be shown should be in the display.");
		}
		super.addComponentNoPaint(component);
	}

	@Override
	public void addComponent(@NotNull CanvasComponent component) {
		if (component instanceof ArmaControlRenderer) {
			throw new IllegalArgumentException("Do not add the controls this way. The controls that should be shown should be in the display.");
		}
		super.addComponent(component);
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
