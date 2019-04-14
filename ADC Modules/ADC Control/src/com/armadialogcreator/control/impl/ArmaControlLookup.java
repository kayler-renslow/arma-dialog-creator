package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.ArmaControlSpecRequirement;
import com.armadialogcreator.control.TestArmaControlRenderer;
import com.armadialogcreator.core.ControlType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	Static(ControlType.Static, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	HTML(ControlType.HTML, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	Edit(ControlType.Edit, EditControl.SPEC_PROVIDER, EditRenderer.class),
	StructuredText(ControlType.StructuredText, StructuredTextControl.SPEC_PROVIDER, StructuredTextRenderer.class),
	ActiveText(ControlType.ActiveText, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	Button(ControlType.Button, ButtonControl.SPEC_PROVIDER, ButtonRenderer.class),
	ShortcutButton(ControlType.ShortcutButton, ShortcutButtonControl.SPEC_PROVIDER, ShortcutButtonRenderer.class),
	XButton(ControlType.XButton, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	Progress(ControlType.Progress, ProgressControl.SPEC_PROVIDER, ProgressRenderer.class),
	StaticSkew(ControlType.StaticSkew, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	LineBreak(ControlType.Linebreak, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	Tree(ControlType.Tree, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	ControlsGroup(ControlType.ControlsGroup, ControlGroupControl.SPEC_PROVIDER, ControlGroupRenderer.class),
	XKeyDesc(ControlType.XKeyDesc, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	AnimatedTexture(ControlType.AnimatedTexture, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	AnimatedUser(ControlType.AnimatedUser, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	ItemSlot(ControlType.ItemSlot, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	Slider(ControlType.Slider, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	XSlider(ControlType.XSlider, XSliderControl.SPEC_PROVIDER, XSliderRenderer.class),

	Combo(ControlType.Combo, ComboControl.SPEC_PROVIDER, ComboRenderer.class),
	XCombo(ControlType.XCombo, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	ListBox(ControlType.ListBox, ListboxControl.SPEC_PROVIDER, ListboxRenderer.class),
	XListBox(ControlType.XListBox, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	ListNBox(ControlType.ListNBox, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	ToolBox(ControlType.ToolBox, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	CheckBoxes(ControlType.CheckBoxes, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	CheckBox(ControlType.CheckBox, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	ContextMenu(ControlType.ContextMenu, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	Menu(ControlType.Menu, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	MenuStrip(ControlType.MenuStrip, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	Object(ControlType.Object, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	ObjectZoom(ControlType.ObjectZoom, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	ObjectContainer(ControlType.ObjectContainer, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	ObjectContAnim(ControlType.ObjectContAnim, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),

	Map(ControlType.Map, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	MapMain(ControlType.MapMain, ArmaControlSpecRequirement.BASE, ArmaControlRenderer.class),
	/** To be used for TESTING only. Do not use for client code. */
	_Test(ControlType._Test, ArmaControlSpecRequirement.BASE, TestArmaControlRenderer.class);

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
