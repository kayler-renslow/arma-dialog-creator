package com.armadialogcreator.control.impl;

import com.armadialogcreator.control.ArmaConfigClassSpec;
import com.armadialogcreator.control.ArmaControlRenderer;
import com.armadialogcreator.control.TestArmaControlRenderer;
import com.armadialogcreator.core.ControlType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	Static(ControlType.Static, StaticControl.SPEC_PROVIDER, StaticRenderer.class),
	HTML(ControlType.HTML, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	Edit(ControlType.Edit, EditControl.SPEC_PROVIDER, EditRenderer.class),
	StructuredText(ControlType.StructuredText, StructuredTextControl.SPEC_PROVIDER, StructuredTextRenderer.class),
	ActiveText(ControlType.ActiveText, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	Button(ControlType.Button, ButtonControl.SPEC_PROVIDER, ButtonRenderer.class),
	ShortcutButton(ControlType.ShortcutButton, ShortcutButtonControl.SPEC_PROVIDER, ShortcutButtonRenderer.class),
	XButton(ControlType.XButton, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	Progress(ControlType.Progress, ProgressControl.SPEC_PROVIDER, ProgressRenderer.class),
	StaticSkew(ControlType.StaticSkew, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	LineBreak(ControlType.Linebreak, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	Tree(ControlType.Tree, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	ControlsGroup(ControlType.ControlsGroup, ControlGroupControl.SPEC_PROVIDER, ControlGroupRenderer.class),
	XKeyDesc(ControlType.XKeyDesc, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	AnimatedTexture(ControlType.AnimatedTexture, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	AnimatedUser(ControlType.AnimatedUser, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	ItemSlot(ControlType.ItemSlot, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	Slider(ControlType.Slider, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	XSlider(ControlType.XSlider, XSliderControl.SPEC_PROVIDER, XSliderRenderer.class),

	Combo(ControlType.Combo, ComboControl.SPEC_PROVIDER, ComboRenderer.class),
	XCombo(ControlType.XCombo, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	ListBox(ControlType.ListBox, ListboxControl.SPEC_PROVIDER, ListboxRenderer.class),
	XListBox(ControlType.XListBox, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	ListNBox(ControlType.ListNBox, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	ToolBox(ControlType.ToolBox, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	CheckBoxes(ControlType.CheckBoxes, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	CheckBox(ControlType.CheckBox, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	ContextMenu(ControlType.ContextMenu, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	Menu(ControlType.Menu, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	MenuStrip(ControlType.MenuStrip, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	Object(ControlType.Object, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	ObjectZoom(ControlType.ObjectZoom, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	ObjectContainer(ControlType.ObjectContainer, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	ObjectContAnim(ControlType.ObjectContAnim, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),

	Map(ControlType.Map, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	MapMain(ControlType.MapMain, ArmaConfigClassSpec.BASE, ArmaControlRenderer.class),
	/** To be used for TESTING only. Do not use for client code. */
	_Test(ControlType._Test, ArmaConfigClassSpec.BASE, TestArmaControlRenderer.class);

	@NotNull
	public final ControlType controlType;
	@NotNull
	public final ArmaConfigClassSpec specProvider;
	/** The renderer to use for the control */
	@NotNull
	public final Class<? extends ArmaControlRenderer> renderer;

	ArmaControlLookup(@NotNull ControlType controlType, @NotNull ArmaConfigClassSpec specProvider,
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
