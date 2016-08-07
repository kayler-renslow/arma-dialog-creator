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

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Control;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 The base class for all controls.<br>
 <b>NOTE: any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instanceof some extended class because when the project is loaded from .xml
 via {@link com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader}, all controls are only {@link ArmaControl} or {@link ArmaControlGroup} and
 <b>not</b> something like {@link com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl}</b>
 Created on 05/20/2016. */
public class ArmaControl extends ControlClass implements Control {
	/** Resolution of the control. Should not change the reference, but rather change the values inside the resolution. */
	protected final ArmaResolution resolution;
	private final Env env;
	private final RendererLookup rendererLookup;
	private final ControlStyle[] allowedStyles;
	/** Type of the control */
	protected ControlType type = ControlType.STATIC;
	/** Style of the control TODO: allow multiple styles */
	protected ControlStyleGroup style = new ControlStyleGroup(new ControlStyle[]{ControlStyle.CENTER});
	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;
	
	protected Expression x, y, width, height;
	
	/** Control id (-1 if doesn't matter) */
	protected int idc = -1;
	
	private final ControlProperty idcProperty, typeProperty, styleProperty, xProperty, yProperty, wProperty, hProperty, accessProperty;
	
	/**
	 Create a control where the position is to be determined
	 
	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param rendererLookup renderer of the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		super(name, provider);
		this.resolution = resolution;
		this.env = env;
		try {
			this.renderer = rendererLookup.rendererClass.newInstance();
			this.renderer.setMyControl(this);
			this.rendererLookup = rendererLookup;
			this.renderer.init();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + rendererLookup.rendererClass.getName() + " couldn't be instantiated.");
		}
		
		idcProperty = findRequiredProperty(ControlPropertyLookup.IDC);
		idcProperty.setDefaultValue(true, -1);
		typeProperty = findRequiredProperty(ControlPropertyLookup.TYPE);
		typeProperty.setDefaultValue(true, type.typeId);
		styleProperty = findRequiredProperty(ControlPropertyLookup.STYLE);
		styleProperty.setDefaultValue(true, style);
		xProperty = findRequiredProperty(ControlPropertyLookup.X);
		yProperty = findRequiredProperty(ControlPropertyLookup.Y);
		wProperty = findRequiredProperty(ControlPropertyLookup.W);
		hProperty = findRequiredProperty(ControlPropertyLookup.H);
		accessProperty = findOptionalProperty(ControlPropertyLookup.ACCESS);
		this.allowedStyles = provider.getAllowedStyles();
		//do not define properties x,y,w,h,idc,type,style here so that they are marked as missed when checking what requirements have been filled
	}
	
	/**
	 Create a control where the position is known
	 
	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param idc control id (-1 if doesn't matter)
	 @param type type of the control
	 @param style style of the control
	 @param x x position (abs region)
	 @param y y position (abs region)
	 @param width width (abs region)
	 @param height height (abs region)
	 @param resolution resolution to use
	 @param rendererLookup renderer for the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, int idc, @NotNull ControlType type, @NotNull ControlStyleGroup style,
					   @NotNull Expression x, @NotNull Expression y, @NotNull Expression width, @NotNull Expression height, @NotNull ArmaResolution resolution,
					   @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		this(name, provider, resolution, rendererLookup, env);
		typeProperty.setDefaultValue(false, type.typeId);
		styleProperty.setDefaultValue(false, style);
		xProperty.setDefaultValue(false, x);
		yProperty.setDefaultValue(false, y);
		wProperty.setDefaultValue(false, width);
		hProperty.setDefaultValue(false, height);
		
		defineType(type);
		defineIdc(idc);
		defineStyle(style);
		defineX(x);
		defineY(y);
		defineW(width);
		defineH(height);
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
		renderer.setX1Silent(calcScreenX(x.getNumVal()));
	}
	
	/** Just set the y position without updating the y property. This will also update the renderer's position. */
	protected void setYSilent(Expression y) {
		this.y = y;
		renderer.setY1Silent(calcScreenY(y.getNumVal()));
	}
	
	/** Set the width without updating it's control property. This will also update the renderer's position. */
	protected void setWSilent(Expression width) {
		this.width = width;
		int w = calcScreenWidth(width.getNumVal());
		renderer.setX2Silent(renderer.getX1() + w);
	}
	
	/** Just set height without setting control property. This will also update the renderer's position. */
	protected void setHSilent(Expression height) {
		this.height = height;
		int h = calcScreenHeight(height.getNumVal());
		renderer.setY2Silent(renderer.getY1() + h);
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
	protected void setPositionWHSilent(Expression x, Expression y, Expression w, Expression h) {
		//do not use @NotNull annotations for parameters because this method is called from updateProperties. updateProperties is only invoked when a ControlProperty is edited.
		//when a ControlProperty is edited, required (like x,y,w,h), and has no input, the user should not be allowed to exit editing until valid input is entered
		if (x == null || y == null || w == null || h == null) {
			return;
		}
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		renderer.setPositionWHSilent(calcScreenX(x.getNumVal()), calcScreenY(y.getNumVal()), calcScreenWidth(w.getNumVal()), calcScreenHeight(h.getNumVal()));
	}
	
	/** Set the x and y values (and width and height) based upon the renderer's position */
	protected void calcPositionFromRenderer() {
		this.x = new Expression(PositionCalculator.getSafeZoneExpressionX(this.resolution, renderer.getX1()), env);
		this.y = new Expression(PositionCalculator.getSafeZoneExpressionY(this.resolution, renderer.getY1()), env);
		xProperty.setValue(x);
		yProperty.setValue(y);
		
		this.width = new Expression(PositionCalculator.getSafeZoneExpressionW(this.resolution, renderer.getWidth()), env);
		this.height = new Expression(PositionCalculator.getSafeZoneExpressionH(this.resolution, renderer.getHeight()), env);
		wProperty.setValue(width);
		hProperty.setValue(height);
		
		getUpdateGroup().update(null); //don't execute updateProperties
	}
	
	@Override
	public void resolutionUpdate(Resolution newResolution) {
		setXSilent(this.x);
		setYSilent(this.y);
		setWSilent(this.width);
		setHSilent(this.height);
	}
	
	@Override
	protected void updateProperties() {
		if (xProperty.getValue() != this.x || yProperty.getValue() != this.y || wProperty.getValue() != this.width || hProperty.getValue() != this.height) {
			setPositionWHSilent((Expression) xProperty.getValue(), (Expression) yProperty.getValue(), (Expression) wProperty.getValue(), (Expression) hProperty.getValue());
		}
		renderer.updateProperties();
		//		defineStyle(styleProperty.);
	}
	
	/** Set idc and define the idc control property */
	public final void defineIdc(int idc) {
		setIdc(idc);
		idcProperty.setValue(idc);
	}
	
	/** Set idc without telling control property */
	protected final void setIdc(int idc) {
		this.idc = idc;
	}
	
	/** Set the control type and define the type control property */
	protected final void defineType(ControlType type) {
		setType(type);
	}
	
	/** Just set the control type without changing control property */
	protected final void setType(ControlType type) {
		this.type = type;
	}
	
	/** Set and define the style control property */
	protected final void defineStyle(ControlStyleGroup style) {
		setStyle(style);
	}
	
	/** Just set the style */
	protected final void setStyle(ControlStyleGroup style) {
		this.style = style;
	}
	
	/** Set and define the access property */
	public final void defineAccess(int access) {
		this.accessProperty.setValue(access);
	}
	
	/** Get idc (control id for arma) */
	public final int getIdc() {
		return idc;
	}
	
	public final ControlType getType() {
		return type;
	}
	
	public final ControlStyleGroup getStyle() {
		return style;
	}
	
	public final ArmaControlRenderer getRenderer() {
		return renderer;
	}
	
	public final RendererLookup getRendererLookup() {
		return rendererLookup;
	}
	
	public final ControlStyle[] getAllowedStyles() {
		return allowedStyles;
	}
}
