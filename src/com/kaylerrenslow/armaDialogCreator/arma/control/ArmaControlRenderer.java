/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Base class for JavaFX canvas rendering of arma controls
 Created on 05/20/2016. */
public class ArmaControlRenderer extends SimpleCanvasComponent {
	protected ArmaControl myControl;
	private ValueObserver<AColor> backgroundColorObserver;
	
	private ValueObserver<Boolean> enabledObserver = new ValueObserver<>(isEnabled());
	
	public ArmaControlRenderer() {
		super(0, 0, 0, 0);
		backgroundColorObserver = new ValueObserver<>(new AColor(backgroundColor));
		backgroundColorObserver.addValueListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
				if (newValue != null) {
					setBackgroundColor(newValue.toJavaFXColor());
				}
			}
		});
	}
	
	final void setMyControl(ArmaControl myControl) {
		this.myControl = myControl;
	}
	
	/** Invoked when the renderer is fully setup by the ArmaControl attached to the renderer. Default implementation does nothing. */
	protected void init() {
	}
	
	/** Invoked when {@link ArmaControl#updateProperties()} is invoked */
	protected void updateProperties() {
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		enabledObserver.updateValue(enabled);
	}
	
	public ValueObserver<Boolean> getEnabledObserver() {
		return enabledObserver;
	}
	
	public ArmaControl getMyControl() {
		return myControl;
	}
	
	public ValueObserver<AColor> getBackgroundColorObserver() {
		return backgroundColorObserver;
	}
	
	@Override
	public void translate(int dx, int dy) {
		super.translate(dx, dy);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void scale(int dxl, int dxr, int dyt, int dyb) {
		super.scale(dxl, dxr, dyt, dyb);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void setPosition(Region r) {
		super.setPosition(r);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void setPosition(int x1, int y1, int x2, int y2) {
		super.setPosition(x1, y1, x2, y2);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void setPositionWH(int x1, int y1, int width, int height) {
		super.setPositionWH(x1, y1, width, height);
		myControl.calcPositionFromRenderer();
	}
	
	/** Set the position without telling the control */
	public void setPositionWHSilent(int x1, int y1, int width, int height) {
		super.setPositionWH(x1, y1, width, height);
	}
	
	@Override
	public void setX1(int x1) {
		super.setX1(x1);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void setY1(int y1) {
		super.setY1(y1);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void setX2(int x2) {
		super.setX2(x2);
		myControl.calcPositionFromRenderer();
	}
	
	@Override
	public void setY2(int y2) {
		super.setY2(y2);
		myControl.calcPositionFromRenderer();
	}
	
	
	public void setX1Silent(int x1) {
		super.setX1(x1);
	}
	
	
	public void setY1Silent(int y1) {
		super.setY1(y1);
	}
	
	
	public void setX2Silent(int x2) {
		super.setX2(x2);
	}
	
	
	public void setY2Silent(int y2) {
		super.setY2(y2);
	}
	
	@Override
	public void setGhost(boolean ghost) {
		super.setGhost(ghost);
		myControl.getUpdateGroup().update(null);
	}
	
}
