package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.*;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasControlGroup;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ControlList;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Generic implementation of a control that can house many controls. This is not the implementation for control type 15 (CT_CONTROLS_GROUP).

 @author Kayler
 @since 06/08/2016. */
public class ArmaControlGroup extends ArmaControl implements CanvasControlGroup<ArmaControl> {
	private final ControlList<ArmaControl> controlsList = new ControlList<>(this);

	public static final ArmaControlSpecRequirement SPEC_PROVIDER = new ArmaControlSpecRequirement() {

		private final ReadOnlyList<ControlPropertyLookupConstant> requiredProperties = new ReadOnlyList<>(ArrayUtil.mergeArrays(ControlPropertyLookupConstant.class,
				defaultRequiredProperties, new ControlPropertyLookup[]{
				}));

		private final ReadOnlyList<ControlPropertyLookupConstant> optionalProperties = new ReadOnlyList<>(ArrayUtil.mergeArrays(ControlPropertyLookupConstant.class,
				defaultOptionalProperties, new ControlPropertyLookup[]{
				}));

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return requiredProperties;
		}

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
			return optionalProperties;
		}

		@Override
		public ControlStyle[] getAllowedStyles() {
			return ControlStyle.NA.getStyleGroup().getValues();
		}
	};


	protected ArmaControlGroup(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull RendererLookup renderer, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(ControlType.ControlsGroup, name, SPEC_PROVIDER, resolution, renderer, env, registry);
		defineStyle(ControlStyle.NA.getStyleGroup());
	}

	protected ArmaControlGroup(@NotNull String name, int idc, @NotNull ArmaResolution resolution, @NotNull RendererLookup renderer,
							   @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(ControlType.ControlsGroup, name, SPEC_PROVIDER, idc, ControlStyle.NA.getStyleGroup(), resolution, renderer, env, registry);
	}

	protected ArmaControlGroup(@NotNull ControlClassSpecification specification, @NotNull ArmaControlSpecRequirement provider, @NotNull ArmaResolution resolution,
							   @NotNull RendererLookup rendererLookup, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(specification, provider, resolution, rendererLookup, env, registry);
	}


	@Override
	public ControlList<ArmaControl> getControls() {
		return controlsList;
	}

	/** Search all controls inside {@link #getControls()} (including controls inside {@link ArmaControlGroup} instances) */
	@Nullable
	public ArmaControl findControlByClassName(@NotNull String className) {
		for (ArmaControl control : getControls()) {
			if (className.equals(control.getClassName())) {
				return control;
			}
			if (control instanceof ArmaControlGroup) {
				ArmaControlGroup group = (ArmaControlGroup) control;
				ArmaControl found = group.findControlByClassName(className);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	@Override
	public void resolutionUpdate(Resolution newResolution) {
		super.resolutionUpdate(newResolution);
		for (ArmaControl control : controlsList) {
			control.resolutionUpdate(newResolution);
		}
	}
}
