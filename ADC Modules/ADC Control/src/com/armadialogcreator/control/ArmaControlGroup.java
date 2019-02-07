package com.armadialogcreator.control;

import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.control.impl.ArmaControlLookup;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.core.ControlStyle;
import com.armadialogcreator.core.old.*;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.ArrayUtil;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 Generic implementation of a control that can house many controls. This is not the implementation for control type 15 (CT_CONTROLS_GROUP).

 @author Kayler
 @since 06/08/2016. */
public class ArmaControlGroup extends ArmaControl {
	private final ControlList controlsList = new ControlList();
	private final ListObserver<UINode> childNodes = new ListObserver<>(new LinkedList<>());

	public static final ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ArmaControlGroup(@NotNull ControlClassSpecification specification, @NotNull ArmaControlLookup lookup,
							@NotNull ArmaResolution resolution, @NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(specification, lookup, resolution, env, registry);
	}

	public ArmaControlGroup(@NotNull String name, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution,
							@NotNull Env env, @NotNull SpecificationRegistry registry) {
		super(name, lookup, resolution, env, registry);
	}

	@NotNull
	public ControlList getControls() {
		return controlsList;
	}

	@NotNull
	public ListObserver<UINode> getChildNodes() {
		return childNodes;
	}

	@Override
	public void resolutionUpdate(@NotNull Resolution newResolution) {
		super.resolutionUpdate(newResolution);
		for (ArmaControl control : controlsList) {
			control.resolutionUpdate(newResolution);
		}
	}

	@Override
	@NotNull
	public Iterable<UINode> iterateChildNodes() {
		return childNodes;
	}

	@Override
	public int getChildCount() {
		return childNodes.size();
	}

	@Override
	@NotNull
	public DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(childNodes);
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeArrays(ControlPropertyLookupConstant.class,
							defaultRequiredProperties, new ControlPropertyLookup[]{
							}));
		}

		@NotNull
		@Override
		public ReadOnlyList<ControlPropertyLookupConstant> getOptionalProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeArrays(ControlPropertyLookupConstant.class,
							defaultOptionalProperties, new ControlPropertyLookup[]{
							}));

		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return ControlStyle.NONE.getStyleGroup().getStyleArray();
		}
	}
}
