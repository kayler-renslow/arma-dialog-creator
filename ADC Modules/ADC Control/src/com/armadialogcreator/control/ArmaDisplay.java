package com.armadialogcreator.control;

import com.armadialogcreator.canvas.*;
import com.armadialogcreator.core.old.DisplayProperty;
import com.armadialogcreator.core.old.DisplayPropertyLookup;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.DoubleIterable;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)

 @author Kayler
 @since 06/14/2016. */
public class ArmaDisplay implements UINode {

	private final DisplayProperty iddProperty = DisplayPropertyLookup.IDD.getIntProperty(-1);

	private final SimpleBaseUINode controlNodes = new ControlsNode(this);
	private final SimpleBaseUINode bgControlNodes = new ControlsNode(this);
	private final UpdateListenerGroup<UINodeChange> updateGroup = new UpdateListenerGroup<>();

	private final ObservableSet<DisplayProperty> displayProperties = FXCollections.observableSet();
	private final UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup = new UpdateListenerGroup<>();

	public ArmaDisplay() {
		displayProperties.add(iddProperty);

		displayProperties.addListener(new SetChangeListener<DisplayProperty>() {
			@Override
			public void onChanged(Change<? extends DisplayProperty> change) {
				if (change.wasRemoved() && change.getElementRemoved().getPropertyLookup() == DisplayPropertyLookup.IDD) {
					throw new IllegalStateException("can't remove idd from display");
				}
			}
		});

		controlNodes.updateGroup.chain(updateGroup);
		bgControlNodes.updateGroup.chain(updateGroup);
	}

	@NotNull
	public DisplayProperty getIddProperty() {
		return iddProperty;
	}

	@Nullable
	public DisplayProperty getProperty(@NotNull DisplayPropertyLookup propertyLookup) {
		for (DisplayProperty displayProperty : displayProperties) {
			if (propertyLookup == displayProperty.getPropertyLookup()) {
				return displayProperty;
			}
		}
		return null;
	}

	/** @return true if the display/dialog is allowed to move. If it isn't, return false. */
	public boolean movingEnabled() {
		DisplayProperty property = getProperty(DisplayPropertyLookup.MOVING_ENABLE);
		return property != null && property.getBooleanValue();
	}

	/** @return true if the display/dialog has user interaction. If no interaction is allowed, return false. */
	public boolean simulationEnabled() {
		DisplayProperty property = getProperty(DisplayPropertyLookup.ENABLE_SIMULATION);
		return property != null && property.getBooleanValue();
	}

	@NotNull
	public ObservableSet<DisplayProperty> getDisplayProperties() {
		return displayProperties;
	}

	@NotNull
	public UINode getControlNodes() {
		return controlNodes;
	}

	@NotNull
	public UINode getBackgroundControlNodes() {
		return bgControlNodes;
	}

	public void resolutionUpdate(@NotNull Resolution newResolution) {
		for (UINode node : bgControlNodes.deepIterateChildren()) {
			if (node instanceof ArmaControl) {
				((ArmaControl) node).resolutionUpdate(newResolution);
			}
		}
		for (UINode node : controlNodes.deepIterateChildren()) {
			if (node instanceof ArmaControl) {
				((ArmaControl) node).resolutionUpdate(newResolution);
			}
		}
	}

	@Override
	@NotNull
	public Iterable<? extends UINode> iterateChildNodes() {
		return new DoubleIterable<>(controlNodes.children, bgControlNodes.children);
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		return renderUpdateGroup;
	}

	@Override
	public int getChildCount() {
		return bgControlNodes.children.size() + controlNodes.children.size();
	}

	@Override
	public int indexOf(@NotNull UINode child) {
		int i = bgControlNodes.indexOf(child);
		if (i != -1) {
			return i;
		}
		return controlNodes.indexOf(child);
	}

	@Override
	public boolean containsChildNode(@NotNull UINode node) {
		return bgControlNodes.containsChildNode(node) || controlNodes.containsChildNode(node);
	}

	@Override
	public void addChild(@NotNull UINode node) {
		controlNodes.addChild(node);
	}

	@Override
	public void addChild(@NotNull UINode node, int index) {
		controlNodes.addChild(node, index);
	}

	@Override
	public boolean removeChild(@NotNull UINode node) {
		if (!bgControlNodes.removeChild(node)) {
			return controlNodes.removeChild(node);
		}
		return false;
	}

	@Override
	public void removeChild(int index) {
		controlNodes.removeChild(index);
	}

	@Override
	public void moveChild(@NotNull UINode child, @NotNull UINode newParent, int destIndex) {
		if (bgControlNodes.containsChildNode(child)) {
			bgControlNodes.moveChild(child, newParent, destIndex);
			return;
		}
		controlNodes.moveChild(child, newParent, destIndex);
	}

	@Override
	public void acceptMovedChild(@NotNull UINode child, @NotNull UINode oldParent, int destIndex) {
		controlNodes.acceptMovedChild(child, oldParent, destIndex);
	}

	@Override
	@NotNull
	public DeepUINodeIterable deepIterateChildren() {
		return new DeepUINodeIterable(iterateChildNodes());
	}

	@Override
	@Nullable
	public CanvasComponent getComponent() {
		return null;
	}

	@Override
	@Nullable
	public UINode getRootNode() {
		return this;
	}

	@Override
	@Nullable
	public UINode getParentNode() {
		return this;
	}

	@Override
	public void setParentNode(@Nullable UINode newParent) {
		//why are you calling this method on a display?
		throw new IllegalStateException();
	}

	@Override
	public void setRootNode(@Nullable UINode newParent) {
		//why are you calling this method on a display?
		throw new IllegalStateException();
	}

	@Override
	@NotNull
	public DataContext getUserData() {
		return controlNodes.getUserData();
	}

	@Override
	@NotNull
	public UpdateListenerGroup<UINodeChange> getUpdateGroup() {
		return updateGroup;
	}

	private static class ControlsNode extends SimpleBaseUINode {
		public ControlsNode(@NotNull ArmaDisplay display) {
			super(display);
		}

		@Override
		@Nullable
		public CanvasComponent getComponent() {
			return null;
		}

		@Override
		@Nullable
		public UINode getParentNode() {
			return rootNode;
		}
	}

}
