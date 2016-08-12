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

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.ControlProperty;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.sv.AColor;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.control.sv.SerializableValue;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Region;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ui.SimpleCanvasComponent;
import com.kaylerrenslow.armaDialogCreator.util.ValueListener;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 Base class for JavaFX canvas rendering of arma controls
 Created on 05/20/2016. */
public class ArmaControlRenderer extends SimpleCanvasComponent {
	protected final ArmaControl myControl;
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private ValueObserver<AColor> backgroundColorObserver;
	
	private ValueObserver<Boolean> enabledObserver = new ValueObserver<>(isEnabled());
	protected final ControlProperty styleProperty, xProperty, yProperty, wProperty, hProperty;
	private ControlStyleGroup style = ControlStyle.NA.getStyleGroup();
	private Expression x, y, width, height;
	private final Env env;
		
	public ArmaControlRenderer(ArmaControl control, ArmaResolution resolution, Env env) {
		super(0, 0, 0, 0);
		this.resolution = resolution;
		this.env = env;
		this.myControl = control;
		backgroundColorObserver = new ValueObserver<>(new AColor(backgroundColor));
		backgroundColorObserver.addValueListener(new ValueListener<AColor>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<AColor> observer, AColor oldValue, AColor newValue) {
				if (newValue != null) {
					setBackgroundColor(newValue.toJavaFXColor());
				}
			}
		});
		styleProperty = control.findRequiredProperty(ControlPropertyLookup.STYLE);
		styleProperty.setDefaultValue(true, style);
		xProperty = control.findRequiredProperty(ControlPropertyLookup.X);
		yProperty = control.findRequiredProperty(ControlPropertyLookup.Y);
		wProperty = control.findRequiredProperty(ControlPropertyLookup.W);
		hProperty = control.findRequiredProperty(ControlPropertyLookup.H);
		
		final ValueListener<SerializableValue> positionValueListener = new ValueListener<SerializableValue>() {
			@Override
			public void valueUpdated(@NotNull ValueObserver<SerializableValue> observer, SerializableValue oldValue, SerializableValue newValue) {
				setPositionWHSilent((Expression) xProperty.getValue(), (Expression) yProperty.getValue(), (Expression) wProperty.getValue(), (Expression) hProperty.getValue());
			}
		};
		xProperty.getValueObserver().addValueListener(positionValueListener);
		yProperty.getValueObserver().addValueListener(positionValueListener);
		wProperty.getValueObserver().addValueListener(positionValueListener);
		hProperty.getValueObserver().addValueListener(positionValueListener);
		
	}
	
	/** Set x and define the x control property. This will also update the renderer's position. */
	public void defineX(Expression x) {
		xProperty.setValue(x);
		setXSilent(x);
	}
	
	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(Expression y) {
		yProperty.setValue(y);
		setYSilent(y);
	}
	
	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(Expression width) {
		wProperty.setValue(width);
		setWSilent(width);
	}
	
	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(Expression height) {
		hProperty.setValue(height);
		setHSilent(height);
	}
	
	/** Just set x position without updating the property. This will also update the renderer's position. */
	protected void setXSilent(Expression x) {
		this.x = x;
		setX1Silent(calcScreenX(x.getNumVal()));
	}
	
	/** Just set the y position without updating the y property. This will also update the renderer's position. */
	protected void setYSilent(Expression y) {
		this.y = y;
		setY1Silent(calcScreenY(y.getNumVal()));
	}
	
	/** Set the width without updating it's control property. This will also update the renderer's position. */
	protected void setWSilent(Expression width) {
		this.width = width;
		int w = calcScreenWidth(width.getNumVal());
		setX2Silent(getX1() + w);
	}
	
	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setHSilent(Expression height) {
		this.height = height;
		int h = calcScreenHeight(height.getNumVal());
		setY2Silent(getY1() + h);
	}
	
	protected final int calcScreenX(double percentX) {
		return PositionCalculator.getScreenX(resolution, percentX);
	}
	
	protected final int calcScreenY(double percentY) {
		return PositionCalculator.getScreenY(resolution, percentY);
	}
	
	protected final int calcScreenWidth(double percentWidth) {
		return PositionCalculator.getScreenWidth(resolution, percentWidth);
	}
	
	protected final int calcScreenHeight(double percentHeight) {
		return PositionCalculator.getScreenHeight(resolution, percentHeight);
	}
	
	/** Set the x,y,w,h properties. This will also update the renderer's position. If x, y, w, or h are null, this method will do nothing. This will not update the value observers.*/
	protected final void setPositionWHSilent(Expression x, Expression y, Expression w, Expression h) {
		//do not use @NotNull annotations for parameters because this method is called from updateProperties. updateProperties is only invoked when a ControlProperty is edited.
		//when a ControlProperty is edited, required (like x,y,w,h), and has no input, the user should not be allowed to exit editing until valid input is entered
		if (x == null || y == null || w == null || h == null) {
			return;
		}
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		setPositionWHSilent(calcScreenX(x.getNumVal()), calcScreenY(y.getNumVal()), calcScreenWidth(w.getNumVal()), calcScreenHeight(h.getNumVal()));
	}
	
	/** Set the x and y values (and width and height) based upon the renderer's position */
	protected final void recalcPosition() {
		this.x = new Expression(PositionCalculator.getSafeZoneExpressionX(resolution, getX1()), env);
		this.y = new Expression(PositionCalculator.getSafeZoneExpressionY(resolution, getY1()), env);
		xProperty.setValue(x);
		yProperty.setValue(y);
		
		this.width = new Expression(PositionCalculator.getSafeZoneExpressionW(resolution, getWidth()), env);
		this.height = new Expression(PositionCalculator.getSafeZoneExpressionH(resolution, getHeight()), env);
		wProperty.setValue(width);
		hProperty.setValue(height);
		
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
		
	/** Set and define the style control property */
	protected final void defineStyle(ControlStyleGroup style) {
		styleProperty.setValue(style);
		setStyle(style);
	}
	
	/** Just set the style */
	protected final void setStyle(ControlStyleGroup style) {
		this.style = style;
	}
	
	@Override
	public void translate(int dx, int dy) {
		super.translate(dx, dy);
		recalcPosition();
	}
	
	@Override
	public void scale(int dxl, int dxr, int dyt, int dyb) {
		super.scale(dxl, dxr, dyt, dyb);
		recalcPosition();
	}
	
	@Override
	public void setPosition(Region r) {
		super.setPosition(r);
		recalcPosition();
	}
	
	@Override
	public void setPosition(int x1, int y1, int x2, int y2) {
		super.setPosition(x1, y1, x2, y2);
		recalcPosition();
	}
	
	@Override
	public void setPositionWH(int x1, int y1, int width, int height) {
		super.setPositionWH(x1, y1, width, height);
		recalcPosition();
	}
	
	/** Set the position without telling the control */
	public void setPositionWHSilent(int x1, int y1, int width, int height) {
		super.setPositionWH(x1, y1, width, height);
	}
	
	@Override
	public void setX1(int x1) {
		super.setX1(x1);
		recalcPosition();
	}
	
	@Override
	public void setY1(int y1) {
		super.setY1(y1);
		recalcPosition();
	}
	
	@Override
	public void setX2(int x2) {
		super.setX2(x2);
		recalcPosition();
	}
	
	@Override
	public void setY2(int y2) {
		super.setY2(y2);
		recalcPosition();
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
	
	public void resolutionUpdate(Resolution newResolution) {
		setXSilent(this.x);
		setYSilent(this.y);
		setWSilent(this.width);
		setHSilent(this.height);
	}
}
