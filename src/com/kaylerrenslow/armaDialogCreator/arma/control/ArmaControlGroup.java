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
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.*;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Generic implementation of a control that can house many controls. This is not the implementation for control type 15 (CT_CONTROLS_GROUP).
 @author Kayler
 @since 06/08/2016. */
public class ArmaControlGroup extends ArmaControl implements ControlGroup {
	private final ControlList<ArmaControl> controlsList = new ControlList<>(this);

	public static final ArmaControlSpecRequirement SPEC_PROVIDER = new ArmaControlSpecRequirement() {
		
		private final ControlPropertyLookup[] requiredProperties = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_REQUIRED_PROPERTIES, new ControlPropertyLookup[]{
		});
		
		private final ControlPropertyLookup[] optionalProperties = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_OPTIONAL_PROPERTIES, new ControlPropertyLookup[]{
		});

		@NotNull
		@Override
		public ControlPropertyLookup[] getRequiredProperties() {
			return requiredProperties;
		}
		
		@NotNull
		@Override
		public ControlPropertyLookup[] getOptionalProperties() {
			return optionalProperties;
		}
		
		@Override
		public ControlStyle[] getAllowedStyles() {
			return ControlStyle.NA.getStyleGroup().getValues();
		}
	};

	
	public ArmaControlGroup(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull RendererLookup renderer, @NotNull Env env) {
		super(name, SPEC_PROVIDER, resolution, renderer, env);
		defineStyle(ControlStyle.NA.getStyleGroup());
		afterConstructor();
	}

	public ArmaControlGroup(@NotNull String name, @NotNull ControlType type, @NotNull ArmaResolution resolution, @NotNull RendererLookup rendererLookup, @NotNull Env env) {
		super(name, type, SPEC_PROVIDER, resolution, rendererLookup, env);
		afterConstructor();
	}

	public ArmaControlGroup(@NotNull String name, int idc, @NotNull ControlType type, Expression x, Expression y, Expression width, Expression height, @NotNull ArmaResolution resolution, @NotNull RendererLookup renderer, @NotNull Env env) {
		super(name, SPEC_PROVIDER, idc, type, ControlStyle.NA.getStyleGroup(), x, y, width, height, resolution, renderer, env);
		afterConstructor();
	}

	private void afterConstructor() {
		init();
	}

	/** Called by the constructor and only the constructor */
	private void init() {
		ArmaControlGroup group = this;
		controlsList.addChangeListener(new ControlListChangeListener<ArmaControl>() {
			@Override
			public void onChanged(ControlList<ArmaControl> controlList, ControlListChange<ArmaControl> change) {
				//do not set the display in here
				if (change.wasAdded()) {
					change.getAdded().getControl().setHolder(group);
				} else if (change.wasSet()) {
					change.getSet().getNewControl().setHolder(group);
				} else if (change.wasMoved() && change.getMoved().getDestinationList() == group.getControls()) {
					change.getMoved().getMovedControl().setHolder(group);
				}
			}
		});
	}
	
	@Override
	void setDisplay(@NotNull ArmaDisplay display) {
		super.setDisplay(display);
		for (ArmaControl control : controlsList) {
			control.setDisplay(display);
		}
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
