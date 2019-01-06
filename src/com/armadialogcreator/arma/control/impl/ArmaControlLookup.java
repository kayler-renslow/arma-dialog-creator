package com.armadialogcreator.arma.control.impl;

import com.armadialogcreator.arma.control.ArmaControlRenderer;
import com.armadialogcreator.arma.control.ArmaControlSpecRequirement;
import com.armadialogcreator.arma.control.TestArmaControlRenderer;
import com.armadialogcreator.core.ControlType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	Static(ControlType.Static, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	HTML(ControlType.HTML, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	Edit(ControlType.Edit, EditControl.SPEC_PROVIDER, EditRenderer.class),
	StructuredText(ControlType.StructuredText, StructuredTextControl.SPEC_PROVIDER, StructuredTextRenderer.class),
	ActiveText(ControlType.ActiveText, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	Button(ControlType.Button, ButtonControl.SPEC_PROVIDER, ButtonRenderer.class),
	ShortcutButton(ControlType.ShortcutButton, ShortcutButtonControl.SPEC_PROVIDER, ShortcutButtonRenderer.class),
	XButton(ControlType.XButton, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	Progress(ControlType.Progress, ProgressControl.SPEC_PROVIDER, ProgressRenderer.class),
	StaticSkew(ControlType.StaticSkew, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	LineBreak(ControlType.Linebreak, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	Tree(ControlType.Tree, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	ControlsGroup(ControlType.ControlsGroup, ControlGroupControl.SPEC_PROVIDER, ControlGroupRenderer.class),
	XKeyDesc(ControlType.XKeyDesc, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	AnimatedTexture(ControlType.AnimatedTexture, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	AnimatedUser(ControlType.AnimatedUser, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	ItemSlot(ControlType.ItemSlot, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	Slider(ControlType.Slider, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	XSlider(ControlType.XSlider, XSliderControl.SPEC_PROVIDER, XSliderRenderer.class),

	Combo(ControlType.Combo, ComboControl.SPEC_PROVIDER, ComboRenderer.class),
	XCombo(ControlType.XCombo, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	ListBox(ControlType.ListBox, ListboxControl.SPEC_PROVIDER, ListboxRenderer.class),
	XListBox(ControlType.XListBox, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	ListNBox(ControlType.ListNBox, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	ToolBox(ControlType.ToolBox, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	CheckBoxes(ControlType.CheckBoxes, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	CheckBox(ControlType.CheckBox, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	ContextMenu(ControlType.ContextMenu, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	Menu(ControlType.Menu, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	MenuStrip(ControlType.MenuStrip, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	Object(ControlType.Object, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	ObjectZoom(ControlType.ObjectZoom, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	ObjectContainer(ControlType.ObjectContainer, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	ObjectContAnim(ControlType.ObjectContAnim, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),

	Map(ControlType.Map, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
	MapMain(ControlType.MapMain, ArmaControlSpecRequirement.TRIVIAL, ArmaControlRenderer.class),
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
