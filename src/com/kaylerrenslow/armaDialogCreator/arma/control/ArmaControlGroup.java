package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.impl.RendererLookup;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.control.ControlPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import com.kaylerrenslow.armaDialogCreator.control.sv.Expression;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.Control;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ControlGroup;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 Generic implementation of a control that can house many controls. This is not the implementation for control type 15 (CT_CONTROLS_GROUP).
 Created on 06/08/2016. */
public class ArmaControlGroup extends ArmaControl implements ControlGroup{
	private final List<ArmaControl> controls = new ArrayList<>();
	private final ReadOnlyList<ArmaControl> controlReadOnlyList = new ReadOnlyList<>(controls);
	
	public final static ArmaControlSpecProvider SPEC_PROVIDER = new ArmaControlSpecProvider(){

		private final ControlPropertyLookup[] REQUIRED_PROPERTIES = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_REQUIRED_PROPERTIES, new ControlPropertyLookup[]{
		});

		private final ControlPropertyLookup[] OPTIONAL_PROPERTIES = ArrayUtil.mergeArrays(ControlPropertyLookup.class, DEFAULT_OPTIONAL_PROPERTIES, new ControlPropertyLookup[]{
		});

		@NotNull
		@Override
		public ControlPropertyLookup[] getRequiredProperties() {
			return REQUIRED_PROPERTIES;
		}

		@NotNull
		@Override
		public ControlPropertyLookup[] getOptionalProperties() {
			return OPTIONAL_PROPERTIES;
		}
	};
	

	public ArmaControlGroup(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull RendererLookup renderer, @NotNull Env env) {
		super(name, SPEC_PROVIDER, resolution, renderer, env);
	}

	public ArmaControlGroup(@NotNull String name, int idc, @NotNull ControlType type, @NotNull ControlStyle style, Expression x, Expression y, Expression width, Expression height, @NotNull ArmaResolution resolution, @NotNull RendererLookup renderer, @NotNull Env env) {
		super(name, SPEC_PROVIDER, idc, type, style, x, y, width, height, resolution, renderer, env);
	}
	
	
	@Override
	public ReadOnlyList<ArmaControl> getControls() {
		return controlReadOnlyList;
	}
	
	@Override
	public void addControl(Control control) {
		if(!(control instanceof ArmaControl)){
			throw new IllegalArgumentException("control does not extend ArmaControl");
		}
		controls.add((ArmaControl) control);
	}
	
	@Override
	public void addControl(int index, Control toAdd) {
		if(!(toAdd instanceof ArmaControl)){
			throw new IllegalArgumentException("toAdd does not extend ArmaControl");
		}
		controls.add(index, (ArmaControl) toAdd);
	}
	
	@Override
	public int indexOf(Control control) {
		return controls.indexOf(control);
	}
	
	@Override
	public boolean removeControl(Control control) {
		return controls.remove(control);
	}
}
