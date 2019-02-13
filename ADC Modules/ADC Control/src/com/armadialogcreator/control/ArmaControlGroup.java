package com.armadialogcreator.control;

import com.armadialogcreator.canvas.DeepUINodeIterable;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.canvas.UINodeChange;
import com.armadialogcreator.control.impl.ArmaControlLookup;
import com.armadialogcreator.core.AllowedStyleProvider;
import com.armadialogcreator.core.ConfigPropertyLookup;
import com.armadialogcreator.core.ConfigPropertyLookupConstant;
import com.armadialogcreator.core.ControlStyle;
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
	private final ListObserver<UINode> children = new ListObserver<>(new LinkedList<>());

	public static final ArmaControlSpecRequirement SPEC_PROVIDER = new SpecReq();

	public ArmaControlGroup(@NotNull String name, @NotNull ArmaControlLookup lookup, @NotNull ArmaResolution resolution,
							@NotNull Env env) {
		super(name, lookup, resolution, env);
	}

	@Override
	public void resolutionUpdate(@NotNull Resolution newResolution) {
		super.resolutionUpdate(newResolution);
		for (UINode node : new DeepUINodeIterable(children)) {
			if (node instanceof ArmaControl) {
				((ArmaControl) node).resolutionUpdate(newResolution);
			}
		}
	}

	@Override
	@NotNull
	public Iterable<UINode> iterateChildNodes() {
		return children;
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	@NotNull
	public DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(children);
	}

	@Override
	public boolean containsChildNode(@NotNull UINode node) {
		return children.contains(node);
	}

	@Override
	public void addChild(@NotNull UINode node) {
		children.add(node);
		node.setParentNode(this);
		updateGroup.update(new UINodeChange.AddChild(node));
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		children.add(index, node);
		node.setParentNode(this);
		updateGroup.update(new UINodeChange.AddChild(node, index));
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		node.setParentNode(null);
		if (children.remove(node)) {
			updateGroup.update(new UINodeChange.RemoveChild(node));
			return true;
		}
		return false;
	}

	@Override
	public void removeChild(int index) {
		UINode removed = children.remove(index);
		if (removed != null) {
			removed.setParentNode(null);
			updateGroup.update(new UINodeChange.RemoveChild(removed));
		}
	}

	@Override
	public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
		children.remove(child);
		updateGroup.update(new UINodeChange.MoveChild(child, this, newParent, destIndex, true));
		newParent.acceptMovedChild(child, this, destIndex);
	}

	@Override
	public int indexOf(@NotNull UINode child) {
		return children.indexOf(child);
	}

	@Override
	public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
		children.add(destIndex, child);
		child.setParentNode(this);
		updateGroup.update(new UINodeChange.MoveChild(child, oldParent, this, destIndex, false));
	}

	private static class SpecReq implements ArmaControlSpecRequirement, AllowedStyleProvider {

		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getRequiredProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeArrays(ConfigPropertyLookupConstant.class,
							defaultRequiredProperties, new ConfigPropertyLookup[]{
							}));
		}

		@NotNull
		@Override
		public ReadOnlyList<ConfigPropertyLookupConstant> getOptionalProperties() {
			return new ReadOnlyList<>(
					ArrayUtil.mergeArrays(ConfigPropertyLookupConstant.class,
							defaultOptionalProperties, new ConfigPropertyLookup[]{
							}));

		}

		@NotNull
		@Override
		public ControlStyle[] getAllowedStyles() {
			return ControlStyle.NONE.getStyleGroup().getStyleArray();
		}
	}
}
