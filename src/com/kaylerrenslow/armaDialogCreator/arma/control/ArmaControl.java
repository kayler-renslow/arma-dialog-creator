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

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.ControlStyleGroup;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasControl;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.CanvasDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlHolder;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 The base class for all controls.<br>
 <b>NOTE: any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instanceof some extended class because when the project is loaded from .xml
 via {@link com.kaylerrenslow.armaDialogCreator.data.io.xml.ProjectXmlLoader}, all controls are only {@link ArmaControl} or {@link ArmaControlGroup} and
 <b>not</b> something like {@link com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl}</b>

 @author Kayler
 @since 05/20/2016. */
public class ArmaControl extends ControlClass implements CanvasControl<ArmaControl> {
	private RendererLookup rendererLookup;
	private ControlStyle[] allowedStyles;
	/** Type of the control */
	private ControlType type = ControlType.STATIC;

	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;


	/** Control id (-1 if doesn't matter) */
	private int idc = -1;

	private ControlProperty idcProperty, accessProperty;
	private UpdateListenerGroup<Object> rerenderUpdateGroup = new UpdateListenerGroup<>();
	private final ValueObserver<CanvasDisplay<ArmaControl>> displayObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlHolder<ArmaControl>> holderObserver = new ValueObserver<>(null);

	/**
	 Create a control where the position is to be determined

	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param rendererLookup renderer of the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	private ArmaControl(@NotNull String name, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env,
						@NotNull SpecificationRegistry registry) {
		super(name, provider, registry);
		construct(provider, resolution, rendererLookup, env);
	}

	private void construct(@NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		try {
			this.rendererLookup = rendererLookup;
			this.renderer = rendererLookup.rendererClass.getConstructor(ArmaControl.class, ArmaResolution.class, Env.class).newInstance(this, resolution, env);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + rendererLookup.rendererClass.getName() + " couldn't be instantiated.");
		}
		idcProperty = findRequiredProperty(ControlPropertyLookup.IDC);
		idcProperty.setDefaultValue(true, -1);
		defineType(type);
		accessProperty = findOptionalProperty(ControlPropertyLookup.ACCESS);
		this.allowedStyles = provider.getAllowedStyles();
		//do not define properties x,y,w,h,idc,type,style here so that they are marked as missed when checking what requirements have been filled
	}

	/**
	 Create a control where the position is to be determined

	 @param type control type
	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param rendererLookup renderer of the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	protected ArmaControl(@NotNull ControlType type, @NotNull String name, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup,
						  @NotNull Env env, @NotNull SpecificationRegistry registry) {
		this(name, provider, resolution, rendererLookup, env, registry);
		checkControlType(type);
		defineType(type);
	}

	/**
	 Create a control where the position is known

	 @param type type of the control
	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param idc control id (-1 if doesn't matter)
	 @param style style of the control
	 @param resolution resolution to use
	 @param rendererLookup renderer for the control
	 @param env the environment used to calculate the control's position and other {@link Expression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull ControlType type, @NotNull String name, @NotNull ArmaControlSpecRequirement provider, int idc, @NotNull ControlStyleGroup style,
					   @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		this(name, provider, resolution, rendererLookup, env, registry);
		checkControlType(type);
		renderer.styleProperty.setDefaultValue(false, style);

		defineType(type);
		idcProperty.setDefaultValue(false, idc);
		defineStyle(style);
	}

	protected ArmaControl(@NotNull ControlClassSpecification specification, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup,
						  @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(specification, registry);
		construct(provider, resolution, rendererLookup, env);
	}

	private void defineType(@NotNull ControlType type) {
		findRequiredProperty(ControlPropertyLookup.TYPE).setDefaultValue(true, type.getTypeId());
	}

	private void checkControlType(@NotNull ControlType type) {
		if (type == ControlType.CONTROLS_GROUP && !(this instanceof ArmaControlGroup)) {
			throw new IllegalStateException("Do not use ArmaControl for ControlType.CONTROLS_GROUP");
		}
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


	@NotNull
	@Override
	public ValueObserver<CanvasDisplay<ArmaControl>> getDisplayObserver() {
		return displayObserver;
	}

	@NotNull
	@Override
	public ValueObserver<ControlHolder<ArmaControl>> getHolderObserver() {
		return holderObserver;
	}


	@NotNull
	public ControlProperty getIdcProperty() {
		return idcProperty;
	}

	/** Set and define the style control property */
	protected final void defineStyle(ControlStyleGroup style) {
		renderer.defineStyle(style);
	}

	/** Set and define the access property */
	public final void defineAccess(int access) {
		this.accessProperty.setValue(access);
	}

	public final ControlType getControlType() {
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

	@Override
	public UpdateListenerGroup<Object> getRenderUpdateGroup() {
		return rerenderUpdateGroup;
	}

	@NotNull
	public static ArmaControl createControl(@NotNull ControlType type, @NotNull ControlClassSpecification specification, @NotNull ArmaControlSpecRequirement provider,
											@NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		if (type == ControlType.CONTROLS_GROUP) {
			return new ArmaControlGroup(specification, provider, resolution, rendererLookup, env, registry);
		}
		return new ArmaControl(specification, provider, resolution, rendererLookup, env, registry);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull ControlType type, @NotNull String name, @NotNull ArmaControlSpecRequirement provider, int idc, @NotNull ControlStyleGroup style,
											@NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		if (type == ControlType.CONTROLS_GROUP) {
			return new ArmaControlGroup(name, idc, resolution, rendererLookup, env, registry);
		}
		return new ArmaControl(type, name, provider, idc, style, resolution, rendererLookup, env, registry);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull ControlType type, @NotNull String name, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution,
											@NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		if (type == ControlType.CONTROLS_GROUP) {
			return new ArmaControlGroup(name, resolution, rendererLookup, env, registry);
		}
		return new ArmaControl(type, name, provider, resolution, rendererLookup, env, registry);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull String className, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		return createControl(lookup.controlType, className, lookup.specProvider, resolution, lookup.defaultRenderer, env, registry);
	}

	public static ArmaControl createControl(@NotNull ControlType controlType, @NotNull String className, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution,
											@NotNull Env env, @NotNull SpecificationRegistry registry) {
		return createControl(controlType, className, lookup.specProvider, resolution, lookup.defaultRenderer, env, registry);
	}
}
