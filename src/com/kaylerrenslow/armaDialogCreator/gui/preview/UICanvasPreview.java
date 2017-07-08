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
	private final ControlFocusHandler focusHandler;
	private ArmaControl mouseOverControl;
	private ArmaControl mousePressControl;

	public UICanvasPreview(@NotNull Resolution resolution, @NotNull ArmaDisplay display,
						   @NotNull ControlFocusHandler focusHandler) {
		super(resolution, display);
		this.focusHandler = focusHandler;
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
		if (mouseOverControl == null) {
			return;
		}
		mousePressControl = mouseOverControl;
		mouseOverControl.getRenderer().mousePress(mb);
	}

	@Override
	protected void mouseReleased(int mousex, int mousey, @NotNull MouseButton mb) {
		if (mousePressControl == null) {
			return;
		}
		mousePressControl.getRenderer().mouseRelease();
		if (mousePressControl.getRenderer().canHaveFocus()) {
			focusHandler.setFocusedControl(mousePressControl);
		}
	}

	@Override
	protected void mouseMoved(int mousex, int mousey) {
		if (mouseOverControl != null) {
			//Do not check if mouseOverControl still contains the point
			//because it may be wider that the control it is behind (let's call it behindControl).
			//if mouseOverControl is wider than behindControl and the mouse moves over behindControl, the mouse would
			//both be over mouseOverControl and behindControl, but we should set mouseOverControl to behindControl since
			//behindControl is in front of current mouseOverControl.
			setMouseOver(mouseOverControl, 0, 0, false);
		}
		Iterator<ArmaControl> controlIter = display.iteratorForAllControls(true);
		mouseOverControl = null;
		while (controlIter.hasNext()) {
			ArmaControl control = controlIter.next();
			if (control.getRenderer().containsPoint(mousex, mousey) && control.getRenderer().isEnabled()) {
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
