package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.arma.control.TestArmaControlRenderer;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	Static(ControlType.Static, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	HTML(ControlType.HTML, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	Edit(ControlType.Edit, EditControl.SPEC_PROVIDER, EditRenderer.class),
	StructuredText(ControlType.StructuredText, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ActiveText(ControlType.ActiveText, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	Button(ControlType.Button, ButtonControl.SPEC_PROVIDER, ButtonRenderer.class),
	ShortcutButton(ControlType.ShortcutButton, ShortcutButtonControl.SPEC_PROVIDER, ShortcutButtonRenderer.class),
	XButton(ControlType.XButton, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	Progress(ControlType.Progress, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	StaticSkew(ControlType.StaticSkew, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	LineBreak(ControlType.Linebreak, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	Tree(ControlType.Tree, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ControlsGroup(ControlType.ControlsGroup, ControlGroupControl.SPEC_PROVIDER, ControlGroupRenderer.class),
	XKeyDesc(ControlType.XKeyDesc, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	AnimatedTexture(ControlType.AnimatedTexture, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	AnimatedUser(ControlType.AnimatedUser, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ItemSlot(ControlType.ItemSlot, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	Slider(ControlType.Slider, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	XSlider(ControlType.XSlider, XSliderControl.SPEC_PROVIDER, XSliderRenderer.class),

	Combo(ControlType.Combo, ComboControl.SPEC_PROVIDER, StaticRenderer.class),
	XCombo(ControlType.XCombo, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	ListBox(ControlType.ListBox, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	XListBox(ControlType.XListBox, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ListNBox(ControlType.ListNBox, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	ToolBox(ControlType.ToolBox, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	CheckBoxes(ControlType.CheckBoxes, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	CheckBox(ControlType.CheckBox, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	ContextMenu(ControlType.ContextMenu, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	Menu(ControlType.Menu, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	MenuStrip(ControlType.MenuStrip, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	Object(ControlType.Object, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ObjectZoom(ControlType.ObjectZoom, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ObjectContainer(ControlType.ObjectContainer, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	ObjectContAnim(ControlType.ObjectContAnim, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),

	Map(ControlType.Map, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	MapMain(ControlType.MapMain, ArmaControlSpecRequirement.TRIVIAL, StaticRenderer.class),
	/** To be used for TESTING only. Do not use for client code. */
	_Test(ControlType._Test, ArmaControlSpecRequirement.TRIVIAL, TestArmaControlRenderer.class);

	@NotNull
	public final ControlType controlType;
	@NotNull
	public final ArmaControlSpecRequirement specProvider;
	/** The renderer to use for the control */
	@NotNull
	public final Class<? extends ArmaControlRenderer> renderer;

	ArmaControlLookup(@NotNull ControlType controlType, @NotNull ArmaControlSpecRequirement specProvider,
					  @NotNull Class<? extends ArmaControlRenderer> renderer) {
		this.controlType = controlType;
		this.specProvider = specProvider;
		this.renderer = renderer;
	}

	@NotNull
	public static ArmaControlLookup findByControlType(ControlType toFind) {
		for (ArmaControlLookup lookup : values()) {
			if (lookup.controlType == toFind) {
				return lookup;
			}
		}
		throw new IllegalStateException("control type should have been matched");
	}

}
