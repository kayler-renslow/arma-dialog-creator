package com.armadialogcreator.arma.control;

import com.armadialogcreator.arma.control.impl.ArmaControlLookup;
import com.armadialogcreator.arma.control.impl.StaticControl;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.canvas.CanvasDisplay;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.core.old.*;
import com.armadialogcreator.core.sv.SVExpression;
import com.armadialogcreator.core.sv.SVInteger;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ValueObserver;
import org.jetbrains.annotations.NotNull;

/**
 The base class for all controls.<br>
 <b>NOTE:</b> any classes that extend this class are SHORT-HAND ways of creating this class. Never check if an {@link ArmaControl} instance is an instance of some short hand class
 (like {@link StaticControl}). Not all controls are created with those shorthand classes. All controls are either {@link ArmaControl} or
 {@link ArmaControlGroup}.

 @author Kayler
 @since 05/20/2016. */
public class ArmaControl extends ControlClassOld implements CanvasControl<ArmaControl> {
	/** Type of the control */
	private ControlType controlType = ControlType.Static;

	/** Renderer of the control for the canvas */
	protected ArmaControlRenderer renderer;

	private ControlProperty idcProperty, accessProperty;
	private final ValueObserver<CanvasDisplay<ArmaControl>> displayObserver = new ValueObserver<>(null);
	private final ValueObserver<ControlHolder<ArmaControl>> holderObserver = new ValueObserver<>(null);
	private final ArmaControlLookup armaControlLookup;

	/**
	 Create a control where the position is to be determined

	 @param name control class name (e.g. RscText or OMGClass). Keep in mind that it should follow normal Identifier rules (letter letterOrDigit*)
	 @param lookup lookup to use
	 @param resolution resolution to use
	 @param env the environment used to calculate the control's position and other {@link SVExpression} instances stored inside this control's {@link ControlProperty}'s.
	 @param registry registry to use
	 */
	protected ArmaControl(@NotNull String name, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution,
						  @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, lookup.specProvider, registry, new DefaultValueProvider.ControlTypeContext(lookup.controlType));
		this.armaControlLookup = lookup;
		construct(lookup, resolution, env);
	}

	protected ArmaControl(@NotNull ControlClassSpecification specification, @NotNull ArmaControlLookup lookup,
						  @NotNull ArmaResolution resolution, @NotNull Env env,
						  @NotNull SpecificationRegistry registry) {
		super(specification, registry, new DefaultValueProvider.ControlTypeContext(lookup.controlType));
		this.armaControlLookup = lookup;
		construct(lookup, resolution, env);
	}

	private void construct(@NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution, @NotNull Env env) {
		defineType(lookup.controlType);
		Class<? extends ArmaControlRenderer> rendererClass = ArmaControlLookup.findByControlType(controlType).renderer;
		try {
			this.renderer = rendererClass.getConstructor(ArmaControl.class, ArmaResolution.class, Env.class).newInstance(this, resolution, env);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			throw new RuntimeException("Class " + rendererClass.getName() + " couldn't be instantiated.");
		}
		idcProperty = findRequiredProperty(ControlPropertyLookup.IDC);
		idcProperty.setValueIfAbsent(true, new SVInteger(-1));
		accessProperty = findOptionalProperty(ControlPropertyLookup.ACCESS);

		//This used for ControlProperty documentation so that each property can have its own documentation for each ControlType
		//and each nested ControlClassOld
		for (ControlProperty property : getAllChildProperties()) {
			ControlPropertyDocumentationProvider.CONTROL_PROPERTY_DOCUMENTATION_PATH.put(property.getUserData(), controlType.name());
		}
		for (ControlClassOld nested : getAllNestedClasses()) {
			for (ControlProperty property : nested.getAllChildProperties()) {
				ControlPropertyDocumentationProvider.CONTROL_PROPERTY_DOCUMENTATION_PATH.put(property.getUserData(), controlType.name() + "/" + nested.getClassName());
			}
		}
	}

	private void defineType(@NotNull ControlType type) {
		findRequiredProperty(ControlPropertyLookup.TYPE).setDefaultValue(true, type.getTypeId());
		this.controlType = type;
	}

	@Override
	public void resolutionUpdate(@NotNull Resolution newResolution) {
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

	/** Set and define the access property */
	public final void defineAccess(int access) {
		this.accessProperty.setValue(access);
	}

	@NotNull
	public final ControlType getControlType() {
		return controlType;
	}

	@NotNull
	public final ArmaControlRenderer getRenderer() {
		return renderer;
	}

	@NotNull
	public ArmaControl duplicate(@NotNull String controlName, @NotNull SpecificationRegistry registry) {
		ArmaControl dup = ArmaControl.createControl(
				controlName,
				ArmaControlLookup.findByControlType(this.controlType),
				renderer.getResolution(),
				renderer.env,
				registry
		);
		dup.setTo(new ControlClassSpecification(this).constructNewControlClass(registry));
		dup.setClassName(controlName);
		return dup;
	}

	@NotNull
	public static ArmaControl createControl(@NotNull ControlType type, @NotNull String name,
											@NotNull ArmaResolution resolution, @NotNull Env env,
											@NotNull SpecificationRegistry registry) {
		ArmaControlLookup lookup = ArmaControlLookup.findByControlType(type);
		return createControl(name, lookup, resolution, env, registry);
	}

	@NotNull
	public static ArmaControl createControl(@NotNull String name, @NotNull ArmaControlLookup lookup,
											@NotNull ArmaResolution resolution,
											@NotNull Env env, @NotNull SpecificationRegistry registry) {
		if (lookup.controlType == ControlType.ControlsGroup) {
			return new ArmaControlGroup(name, lookup, resolution, env, registry);
		}
		return new ArmaControl(name, lookup, resolution, env, registry);
	}
}
