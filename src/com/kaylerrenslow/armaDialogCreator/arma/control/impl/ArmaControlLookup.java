package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	Static(ControlType.Static, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	HTML(ControlType.HTML, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	Edit(ControlType.Edit, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	StructuredText(ControlType.StructuredText, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ActiveText(ControlType.ActiveText, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	Button(ControlType.Button, ButtonControl.SPEC_PROVIDER, RendererLookup.Button),
	ShortcutButton(ControlType.ShortcutButton, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	XButton(ControlType.XButton, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	Progress(ControlType.Progress, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	StaticSkew(ControlType.StaticSkew, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	LineBreak(ControlType.Linebreak, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	Tree(ControlType.Tree, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ControlsGroup(ControlType.ControlsGroup, ControlGroupControl.SPEC_PROVIDER, RendererLookup.ControlGroup),
	XKeyDesc(ControlType.XKeyDesc, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	AnimatedTexture(ControlType.AnimatedTexture, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	AnimatedUser(ControlType.AnimatedUser, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ItemSlot(ControlType.ItemSlot, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	Slider(ControlType.Slider, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	XSlider(ControlType.XSlider, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	Combo(ControlType.Combo, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	XCombo(ControlType.XCombo, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	ListBox(ControlType.ListBox, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	XListBox(ControlType.XListBox, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ListNBox(ControlType.ListNBox, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	ToolBox(ControlType.ToolBox, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	CheckBoxes(ControlType.CheckBoxes, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	CheckBox(ControlType.CheckBox, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	ContextMenu(ControlType.ContextMenu, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	Menu(ControlType.Menu, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	MenuStrip(ControlType.MenuStrip, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	Object(ControlType.Object, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ObjectZoom(ControlType.ObjectZoom, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ObjectContainer(ControlType.ObjectContainer, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	ObjectContAnim(ControlType.ObjectContAnim, StaticControl.SPEC_PROVIDER, RendererLookup.Static),

	Map(ControlType.Map, StaticControl.SPEC_PROVIDER, RendererLookup.Static),
	MapMain(ControlType.MapMain, StaticControl.SPEC_PROVIDER, RendererLookup.Static);

	@NotNull
	public final ControlType controlType;
	@NotNull
	public final ArmaControlSpecRequirement specProvider;
	/** The renderer to use for the control */
	@NotNull
	public final RendererLookup defaultRenderer;

	ArmaControlLookup(@NotNull ControlType controlType, @NotNull ArmaControlSpecRequirement specProvider, @NotNull RendererLookup lookup) {
		this.controlType = controlType;
		this.specProvider = specProvider;
		this.defaultRenderer = lookup;
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
