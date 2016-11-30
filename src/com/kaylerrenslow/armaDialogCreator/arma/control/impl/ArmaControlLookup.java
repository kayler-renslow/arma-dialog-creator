package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	STATIC(ControlType.STATIC, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	HTML(ControlType.HTML, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	EDIT(ControlType.EDIT, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	STRUCTURED_TEXT(ControlType.STRUCTURED_TEXT, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	ACTIVETEXT(ControlType.ACTIVETEXT, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	BUTTON(ControlType.BUTTON, ButtonControl.SPEC_PROVIDER, RendererLookup.BUTTON),
	SHORTCUTBUTTON(ControlType.SHORTCUTBUTTON, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	XBUTTON(ControlType.XBUTTON, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	PROGRESS(ControlType.PROGRESS, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	STATIC_SKEW(ControlType.STATIC_SKEW, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	LINEBREAK(ControlType.LINEBREAK, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	TREE(ControlType.TREE, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	CONTROLS_GROUP(ControlType.CONTROLS_GROUP, ControlGroupControl.SPEC_PROVIDER, RendererLookup.CONTROL_GROUP),
	XKEYDESC(ControlType.XKEYDESC, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	ANIMATED_TEXTURE(ControlType.ANIMATED_TEXTURE, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	ANIMATED_USER(ControlType.ANIMATED_USER, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	ITEMSLOT(ControlType.ITEMSLOT, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	SLIDER(ControlType.SLIDER, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	XSLIDER(ControlType.XSLIDER, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	COMBO(ControlType.COMBO, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	XCOMBO(ControlType.XCOMBO, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	LISTBOX(ControlType.LISTBOX, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	XLISTBOX(ControlType.XLISTBOX, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	LISTNBOX(ControlType.LISTNBOX, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	TOOLBOX(ControlType.TOOLBOX, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	CHECKBOXES(ControlType.CHECKBOXES, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	CHECKBOX(ControlType.CHECKBOX, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	CONTEXT_MENU(ControlType.CONTEXT_MENU, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	MENU(ControlType.MENU, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	MENU_STRIP(ControlType.MENU_STRIP, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	OBJECT(ControlType.OBJECT, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	OBJECT_ZOOM(ControlType.OBJECT_ZOOM, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	OBJECT_CONTAINER(ControlType.OBJECT_CONTAINER, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	OBJECT_CONT_ANIM(ControlType.OBJECT_CONT_ANIM, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),

	MAP(ControlType.MAP, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC),
	MAP_MAIN(ControlType.MAP_MAIN, StaticControl.SPEC_PROVIDER, RendererLookup.STATIC);

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
