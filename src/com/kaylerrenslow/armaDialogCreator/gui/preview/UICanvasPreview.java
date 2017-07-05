package com.kaylerrenslow.armaDialogCreator.gui.preview;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControl;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.main.CanvasView;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasContext;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.UICanvas;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import javafx.scene.input.MouseButton;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 A {@link UICanvas} instance that will paint {@link ArmaControl} instances in "preview mode"

 @author Kayler
 @since 06/14/2016 */
public class UICanvasPreview extends UICanvas<ArmaControl> {

	private final CanvasView canvasView = ArmaDialogCreator.getCanvasView();
	private ArmaControl mouseOverControl;

	public UICanvasPreview(@NotNull Resolution resolution, @NotNull ArmaDisplay display) {
		super(resolution, display);
		this.canvasContext = new CanvasContext() {
			@Override
			public boolean paintPartial() {
				return false;
			}
		};
		this.alwaysPaint = true;
	}

	@Override
	protected void paint() {
		this.backgroundColor = canvasView.getCanvasBackgroundColor();
		this.backgroundImage = canvasView.getCanvasBackgroundImage();

		super.paint();
	}

	@Override
	protected void mousePressed(int mousex, int mousey, @NotNull MouseButton mb) {

	}

	@Override
	protected void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb) {

	}

	@Override
	protected void mouseMoved(int mousex, int mousey) {
		Iterator<ArmaControl> controlIter = display.iteratorForAllControls(true);
		if (mouseOverControl != null) {
			setMouseOver(mouseOverControl, 0, 0, false);
		}
		mouseOverControl = null;
		while (controlIter.hasNext()) {
			ArmaControl control = controlIter.next();
			if (control.getRenderer().containsPoint(mousex, mousey)) {
				mouseOverControl = control;
				setMouseOver(mouseOverControl, mousex, mousey, true);
				break;
			}
		}
	}

	@Override
	public void updateResolution(@NotNull Resolution newResolution) {
		super.updateResolution(newResolution);
	}

	private void setMouseOver(@NotNull ArmaControl armaControl, int mousex, int mousey, boolean mouseOver) {
		armaControl.getRenderer().setMouseOver(mousex, mousey, mouseOver);
	}
}
