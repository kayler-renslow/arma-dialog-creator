package com.armadialogcreator.control;

import com.armadialogcreator.canvas.CanvasComponent;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.canvas.UINode;
import com.armadialogcreator.core.old.DisplayProperty;
import com.armadialogcreator.core.old.DisplayPropertyLookup;
import com.armadialogcreator.util.DoubleIterable;
import com.armadialogcreator.util.ListObserver;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)

 @author Kayler
 @since 06/14/2016. */
public class ArmaDisplay extends SimpleBaseUINode {

	private final DisplayProperty iddProperty = DisplayPropertyLookup.IDD.getIntProperty(-1);
	private final ControlList controlsList = new ControlList();
	private final ControlList bgControlsList = new ControlList();

	private final ObservableSet<DisplayProperty> displayProperties = FXCollections.observableSet();
	private final UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup = new UpdateListenerGroup<>();

	public ArmaDisplay() {
		super(null);
		displayProperties.add(iddProperty);

		displayProperties.addListener(new SetChangeListener<DisplayProperty>() {
			@Override
			public void onChanged(Change<? extends DisplayProperty> change) {
				if (change.wasRemoved() && change.getElementRemoved().getPropertyLookup() == DisplayPropertyLookup.IDD) {
					throw new IllegalStateException("can't remove idd from display");
				}
			}
		});

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
	public ControlList getBackgroundControls() {
		return bgControlsList;
	}

	@NotNull
	public ControlList getControls() {
		return controlsList;
	}

	public void resolutionUpdate(@NotNull Resolution newResolution) {
		for (ArmaControl control : bgControlsList) {
			control.resolutionUpdate(newResolution);
		}
		for (ArmaControl control : controlsList) {
			control.resolutionUpdate(newResolution);
		}
	}

	/**
	 Search all controls inside the display (including controls inside {@link ArmaControlGroup} instances).
	 This search is non case sensitive.

	 @return what was matched, or null if nothing was matched
	 */
	@Nullable
	public ArmaControl findControlByClassName(@NotNull String className) {
		for (ArmaControl control : getBackgroundControls().deepIterator()) {
			if (className.equalsIgnoreCase(control.getClassName())) {
				return control;
			}
		}
		for (ArmaControl control : getControls().deepIterator()) {
			if (className.equalsIgnoreCase(control.getClassName())) {
				return control;
			}
		}
		return null;
	}


	@Override
	@NotNull
	public UpdateListenerGroup<UpdateListenerGroup.NoData> renderUpdateGroup() {
		return renderUpdateGroup;
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
}
