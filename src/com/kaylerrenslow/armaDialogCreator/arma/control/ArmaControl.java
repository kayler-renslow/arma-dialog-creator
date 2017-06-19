package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.ArmaControlLookup;
import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVExpression;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVInteger;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasControl;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ControlHolder;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.UpdateListenerGroup;
import com.kaylerrenslow.armaDialogCreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 The base class for all controls.<br>
 <b>NOTE:</b> any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instance of some short hand class
 (like {@link com.kaylerrenslow.armaDialogCreator.arma.control.impl.StaticControl}). Not all controls are created with those shorthand classes. All controls are either {@link ArmaControl} or
 {@link ArmaControlGroup}.

 @author Kayler
 @since 05/20/2016. */
public class ArmaControl extends ControlClass implements CanvasControl<ArmaControl> {
	private RendererLookup rendererLookup;
	private ControlStyle[] allowedStyles;
	/** Type of the control */
	private ControlType type = ControlType.Static;

	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;

	private ControlProperty idcProperty, accessProperty;
	private final UpdateListenerGroup<ArmaControl> rerenderUpdateGroup = new UpdateListenerGroup<>();
	private final ValueObserver<CanvasDisplay<ArmaControl>> displayObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlHolder<ArmaControl>> holderObserver = new ValueObserver<>(null);

	/**
	 Create a control where the position is to be determined

	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param resolution resolution to use
	 @param rendererLookup renderer of the control
	 @param env the environment used to calculate the control's position and other {@link SVExpression} instances stored inside this control's {@link ControlProperty}'s.
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
		idcProperty.setValueIfAbsent(true, new SVInteger(-1));
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
	 @param env the environment used to calculate the control's position and other {@link SVExpression} instances stored inside this control's {@link ControlProperty}'s.
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
	 @param resolution resolution to use
	 @param rendererLookup renderer for the control
	 @param env the environment used to calculate the control's position and other {@link SVExpression} instances stored inside this control's {@link ControlProperty}'s.
	 */
	public ArmaControl(@NotNull ControlType type, @NotNull String name, @NotNull ArmaControlSpecRequirement provider, int idc,
					   @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		this(name, provider, resolution, rendererLookup, env, registry);
		checkControlType(type);

		defineType(type);
		idcProperty.setDefaultValue(false, idc);
	}

	protected ArmaControl(@NotNull ControlClassSpecification specification, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup,
						  @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(specification, registry);
		construct(provider, resolution, rendererLookup, env);
	}

	private void defineType(@NotNull ControlType type) {
		findRequiredProperty(ControlPropertyLookup.TYPE).setDefaultValue(true, type.getTypeId());
		this.type = type;
	}

	private void checkControlType(@NotNull ControlType type) {
		if (type == ControlType.ControlsGroup && !(this instanceof ArmaControlGroup)) {
			throw new IllegalStateException("Do not use ArmaControl for ControlType.ControlsGroup");
		}
	}

	/** Set x and define the x control property. This will also update the renderer's position. */
	public void defineX(SVExpression x) {
		renderer.defineX(x);
	}

	/** Set y and define the y control property. This will also update the renderer's position. */
	public void defineY(SVExpression y) {
		renderer.defineY(y);
	}

	/** Set w (width) and define the w control property. This will also update the renderer's position. */
	public void defineW(SVExpression width) {
		renderer.defineW(width);
	}

	/** Set h (height) and define the h control property. This will also update the renderer's position. */
	public void defineH(SVExpression height) {
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

	/** Set and define the access property */
	public final void defineAccess(int access) {
		this.accessProperty.setValue(access);
	}

	@NotNull
	public final ControlType getControlType() {
		return type;
	}

	@NotNull
	public final ArmaControlRenderer getRenderer() {
		return renderer;
	}

	@NotNull
	public final RendererLookup getRendererLookup() {
		return rendererLookup;
	}

	@NotNull
	public final ControlStyle[] getAllowedStyles() {
		return allowedStyles;
	}

	@Override
	public UpdateListenerGroup<ArmaControl> getRenderUpdateGroup() {
		return rerenderUpdateGroup;
	}


	@NotNull
	public static ArmaControl createControl(@NotNull ControlType type, @NotNull String name, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution,
											@NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		if (type == ControlType.ControlsGroup) {
			return new ArmaControlGroup(name, resolution, rendererLookup, env, registry);
		}
		return new ArmaControl(type, name, provider, resolution, rendererLookup, env, registry);
	}

	public static ArmaControl createControl(@NotNull String className, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		return createControl(lookup.controlType, className, lookup.specProvider, resolution, lookup.defaultRenderer, env, registry);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull ControlType controlType, @NotNull String className, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution,
											@NotNull Env env, @NotNull SpecificationRegistry registry) {
		return createControl(controlType, className, lookup.specProvider, resolution, lookup.defaultRenderer, env, registry);
	}
}
