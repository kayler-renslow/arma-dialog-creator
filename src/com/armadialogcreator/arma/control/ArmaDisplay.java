package com.armadialogcreator.arma.control;

import com.armadialogcreator.canvas.CanvasDisplay;
import com.armadialogcreator.canvas.DisplayControlList;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.core.old.DisplayProperty;
import com.armadialogcreator.core.old.DisplayPropertyLookup;
import com.armadialogcreator.util.DataContext;
import com.armadialogcreator.util.ListsIterator;
import com.armadialogcreator.util.UpdateListenerGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 Interface that specifies something that is displayable in preview and in Arma 3 (title, dialog, display)

 @author Kayler
 @since 06/14/2016. */
public class ArmaDisplay implements CanvasDisplay<ArmaControl> {

	private final DisplayProperty iddProperty = DisplayPropertyLookup.IDD.getIntProperty(-1);
	private final DisplayControlList<ArmaControl> controlsList = new DisplayControlList<>(this);
	private final DisplayControlList<ArmaControl> bgControlsList = new DisplayControlList<>(this);
	private final DataContext userdata = new DataContext();

	private final ArrayList<List<ArmaControl>> controlsMerged = new ArrayList<>(2);

	private final ObservableSet<DisplayProperty> displayProperties = FXCollections.observableSet();
	private final UpdateListenerGroup<ArmaControl> renderUpdateGroup = new UpdateListenerGroup<>();

	public ArmaDisplay() {
		controlsMerged.add(getBackgroundControls());
		controlsMerged.add(getControls());

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

	@Override
	@NotNull
	public Iterator<ArmaControl> iteratorForAllControls(boolean reverse) {
		return new ListsIterator<>(controlsMerged, reverse);
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

	/** Return true if the display/dialog is allowed to move. If it isn't, return false. */
	public boolean movingEnabled() {
		DisplayProperty property = getProperty(DisplayPropertyLookup.MOVING_ENABLE);
		return property != null && property.getBooleanValue();
	}

	/** Return true if the display/dialog has user interaction. If no interaction is allowed, return false. */
	public boolean simulationEnabled() {
		DisplayProperty property = getProperty(DisplayPropertyLookup.ENABLE_SIMULATION);
		return property != null && property.getBooleanValue();
	}

	@NotNull
	public ObservableSet<DisplayProperty> getDisplayProperties() {
		return displayProperties;
	}

	@NotNull
	@Override
	public DisplayControlList<ArmaControl> getBackgroundControls() {
		return bgControlsList;
	}

	/** Get all controls. If simulation isn't enabled, return the controls regardless. */
	@NotNull
	public DisplayControlList<ArmaControl> getControls() {
		return controlsList;
	}

	@Override
	public void resolutionUpdate(@NotNull Resolution newResolution) {
		for (ArmaControl control : bgControlsList) {
			control.resolutionUpdate(newResolution);
		}
		for (ArmaControl control : controlsList) {
			control.resolutionUpdate(newResolution);
		}
	}

	@NotNull
	@Override
	public UpdateListenerGroup<ArmaControl> getReRenderUpdateGroup() {
		return renderUpdateGroup;
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
	public DataContext getUserData() {
		return userdata;
	}


}
