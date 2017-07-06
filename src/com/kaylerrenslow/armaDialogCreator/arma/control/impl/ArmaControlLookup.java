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
	HTML(ControlType.HTML, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	Edit(ControlType.Edit, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	StructuredText(ControlType.StructuredText, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ActiveText(ControlType.ActiveText, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	Button(ControlType.Button, ButtonControl.SPEC_PROVIDER, ButtonRenderer.class),
	ShortcutButton(ControlType.ShortcutButton, ShortcutButtonControl.SPEC_PROVIDER, ShortcutButtonRenderer.class),
	XButton(ControlType.XButton, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	Progress(ControlType.Progress, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	StaticSkew(ControlType.StaticSkew, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	LineBreak(ControlType.Linebreak, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	Tree(ControlType.Tree, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ControlsGroup(ControlType.ControlsGroup, ControlGroupControl.SPEC_PROVIDER, ControlGroupRenderer.class),
	XKeyDesc(ControlType.XKeyDesc, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	AnimatedTexture(ControlType.AnimatedTexture, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	AnimatedUser(ControlType.AnimatedUser, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ItemSlot(ControlType.ItemSlot, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	Slider(ControlType.Slider, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	XSlider(ControlType.XSlider, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	Combo(ControlType.Combo, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	XCombo(ControlType.XCombo, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	ListBox(ControlType.ListBox, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	XListBox(ControlType.XListBox, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ListNBox(ControlType.ListNBox, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	ToolBox(ControlType.ToolBox, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	CheckBoxes(ControlType.CheckBoxes, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	CheckBox(ControlType.CheckBox, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	ContextMenu(ControlType.ContextMenu, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	Menu(ControlType.Menu, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	MenuStrip(ControlType.MenuStrip, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	Object(ControlType.Object, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ObjectZoom(ControlType.ObjectZoom, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ObjectContainer(ControlType.ObjectContainer, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	ObjectContAnim(ControlType.ObjectContAnim, StaticControl.SPEC_PROVIDER, StaticRenderer.class),

	Map(ControlType.Map, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	MapMain(ControlType.MapMain, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
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
