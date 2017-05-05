package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.control.DisplayProperty;
import com.kaylerrenslow.armaDialogCreator.control.DisplayPropertyLookup;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.CanvasDisplay;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.DisplayControlList;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.Resolution;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ListMergeIterator;
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
	@SuppressWarnings("unchecked")
	private final ArrayList<List<ArmaControl>> controlsMerged = new ArrayList(2);

	private final ObservableSet<DisplayProperty> displayProperties = FXCollections.observableSet();

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
	public Iterator<ArmaControl> iteratorForAllControls(boolean backwards) {
		return new ListMergeIterator<>(backwards, controlsMerged);
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

	/** Search all controls inside the display (including controls inside {@link ArmaControlGroup} instances) */
	@Nullable
	public ArmaControl findControlByClassName(@NotNull String className) {
		Iterator<ArmaControl> iterator = iteratorForAllControls(false);
		while (iterator.hasNext()) {
			ArmaControl control = iterator.next();
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
	public DataContext getUserData() {
		return userdata;
	}


}
