package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 @author Kayler
 Generic implementation of a control that can house many controls. This is not the implementation for control type 15 (CT_CONTROLS_GROUP).
 Created on 06/08/2016. */
public class ArmaControlGroup extends ArmaControl {
	private ObservableList<ArmaControl> controls = FXCollections.observableArrayList(new ArrayList<ArmaControl>());

	{
		ArmaControlGroup me = this;
		controls.addListener(new ListChangeListener<ArmaControl>() {
			@Override
			public void onChanged(Change<? extends ArmaControl> c) {
				c.next();
				me.getControlListener().updateValue(me);
			}
		});
	}

	public ArmaControlGroup(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer, @Nullable ArmaControlClass[] requiredSubClasses, @Nullable ArmaControlClass[] optionalSubClasses) {
		super(name, resolution, renderer, requiredSubClasses, optionalSubClasses);
	}

	public ArmaControlGroup(@NotNull String name, int idc, @NotNull ControlType type, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull ArmaResolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer, @Nullable ArmaControlClass[] requiredSubClasses, @Nullable ArmaControlClass[] optionalSubClasses) {
		super(name, idc, type, style, x, y, width, height, resolution, renderer, requiredSubClasses, optionalSubClasses);
	}

	/** Get all controls inside the group */
	@NotNull
	public ObservableList<ArmaControl> getControls() {
		return controls;
	}
}
