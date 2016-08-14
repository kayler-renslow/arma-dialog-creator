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
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Control;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlHolder;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 The base class for all controls.<br>
 <b>NOTE: any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instanceof some extended class because when the project is loaded from .xml
 via {@link com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader}, all controls are only {@link ArmaControl} or {@link ArmaControlGroup} and
 <b>not</b> something like {@link com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl}</b>
 Created on 05/20/2016. */
public class ArmaControl extends ControlClass implements Control {
	private final RendererLookup rendererLookup;
	private final ControlStyle[] allowedStyles;
	/** Type of the control */
	private ControlType type = ControlType.STATIC;
	
	/** Renderer of the control for the canvas */
	protected final ArmaControlRenderer renderer;
	
	private ControlHolder<ArmaControl> parent;
	private ArmaDisplay display;
	
	/** Control id (-1 if doesn't matter) */
	private int idc = -1;
	
	private final ControlProperty idcProperty, typeProperty, accessProperty;
	private final DataContext userdata = new DataContext();
	
	/**
	 Create a control where the position is to be determined
	 
	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param rendererLookup renderer of the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull String name, @NotNull ArmaControlSpecProvider provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		super(name, provider);
		try {
			this.rendererLookup = rendererLookup;
			this.renderer = rendererLookup.rendererClass.getConstructor(ArmaControl.class, ArmaResolution.class, Env.class).newInstance(this, resolution, env);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + rendererLookup.rendererClass.getName() + " couldn't be instantiated.");
		}
		
		idcProperty = findRequiredProperty(ControlPropertyLookup.IDC);
		idcProperty.setDefaultValue(true, -1);
		typeProperty = findRequiredProperty(ControlPropertyLookup.TYPE);
		typeProperty.setDefaultValue(true, type.typeId);
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
		renderer.styleProperty.setDefaultValue(false, style);
		renderer.xProperty.setDefaultValue(false, x);
		renderer.yProperty.setDefaultValue(false, y);
		renderer.wProperty.setDefaultValue(false, width);
		renderer.hProperty.setDefaultValue(false, height);
		
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
		renderer.defineX(x);
	}
	
	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(Expression y) {
		renderer.defineY(y);
	}
	
	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(Expression width) {
		renderer.defineW(width);
	}
	
	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(Expression height) {
		renderer.defineH(height);
	}
		
	@Override
	public void resolutionUpdate(Resolution newResolution) {
		renderer.resolutionUpdate(newResolution);
	}
	
	void setParent(@NotNull ControlHolder<ArmaControl> parent){
		this.parent = parent;
	}
	
	void setDisplay(@NotNull ArmaDisplay display){
		this.display = display;
	}
	
	@Override
	@NotNull
	public ControlHolder<ArmaControl> getHolder() {
		return parent;
	}
	
	@Override
	@NotNull
	public ArmaDisplay getDisplay() {
		return display;
	}
	
	@Override
	public DataContext getUserData() {
		return userdata;
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
		renderer.defineStyle(style);
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
