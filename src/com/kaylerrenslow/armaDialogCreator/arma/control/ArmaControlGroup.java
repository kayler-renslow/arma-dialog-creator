package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.util.screen.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.util.ArrayUtil;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 @author Kayler
 Generic implementation of a control that can house many controls. This is not the implementation for control type 15 (CT_CONTROLS_GROUP).
 Created on 06/08/2016. */
public class ArmaControlGroup extends ArmaControl {
	private ObservableList<ArmaControl> controls = FXCollections.observableArrayList(new ArrayList<ArmaControl>());
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
	{
		ArmaControlGroup me = this;
		controls.addListener(new ListChangeListener<ArmaControl>() {
			@Override
			public void onChanged(Change<? extends ArmaControl> c) {
				c.next();
				me.getUpdateGroup().update(me);
			}
		});
	}

	public ArmaControlGroup(@NotNull String name, @NotNull ArmaResolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer) {
		super(name, SPEC_PROVIDER, resolution, renderer);
	}

	public ArmaControlGroup(@NotNull String name, int idc, @NotNull ControlType type, @NotNull ControlStyle style, double x, double y, double width, double height, @NotNull ArmaResolution resolution, @NotNull Class<? extends ArmaControlRenderer> renderer) {
		super(name, SPEC_PROVIDER, idc, type, style, x, y, width, height, resolution, renderer);
	}

	/** Get all controls inside the group */
	@NotNull
	public ObservableList<ArmaControl> getControls() {
		return controls;
	}
}
