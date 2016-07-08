package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecProvider;
import com.kaylerrenslow.armaDialogCreator.arma.control.ControlType;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	STATIC(ControlType.STATIC, StaticControl.SPEC_PROVIDER),
	HTML(ControlType.HTML, StaticControl.SPEC_PROVIDER),
	EDIT(ControlType.EDIT, StaticControl.SPEC_PROVIDER),
	STRUCTURED_TEXT(ControlType.STRUCTURED_TEXT, StaticControl.SPEC_PROVIDER),
	ACTIVETEXT(ControlType.ACTIVETEXT, StaticControl.SPEC_PROVIDER),

	BUTTON(ControlType.BUTTON, StaticControl.SPEC_PROVIDER),
	SHORTCUTBUTTON(ControlType.SHORTCUTBUTTON, StaticControl.SPEC_PROVIDER),
	XBUTTON(ControlType.XBUTTON, StaticControl.SPEC_PROVIDER),

	PROGRESS(ControlType.PROGRESS, StaticControl.SPEC_PROVIDER),
	STATIC_SKEW(ControlType.STATIC_SKEW, StaticControl.SPEC_PROVIDER),
	LINEBREAK(ControlType.LINEBREAK, StaticControl.SPEC_PROVIDER),
	TREE(ControlType.TREE, StaticControl.SPEC_PROVIDER),
	CONTROLS_GROUP(ControlType.CONTROLS_GROUP, ControlGroupControl.SPEC_PROVIDER),
	XKEYDESC(ControlType.XKEYDESC, StaticControl.SPEC_PROVIDER),
	ANIMATED_TEXTURE(ControlType.ANIMATED_TEXTURE, StaticControl.SPEC_PROVIDER),
	ANIMATED_USER(ControlType.ANIMATED_USER, StaticControl.SPEC_PROVIDER),
	ITEMSLOT(ControlType.ITEMSLOT, StaticControl.SPEC_PROVIDER),

	SLIDER(ControlType.SLIDER, StaticControl.SPEC_PROVIDER),
	XSLIDER(ControlType.XSLIDER, StaticControl.SPEC_PROVIDER),

	COMBO(ControlType.COMBO, StaticControl.SPEC_PROVIDER),
	XCOMBO(ControlType.XCOMBO, StaticControl.SPEC_PROVIDER),

	LISTBOX(ControlType.LISTBOX, StaticControl.SPEC_PROVIDER),
	XLISTBOX(ControlType.XLISTBOX, StaticControl.SPEC_PROVIDER),
	LISTNBOX(ControlType.LISTNBOX, StaticControl.SPEC_PROVIDER),

	TOOLBOX(ControlType.TOOLBOX, StaticControl.SPEC_PROVIDER),
	CHECKBOXES(ControlType.CHECKBOXES, StaticControl.SPEC_PROVIDER),
	CHECKBOX(ControlType.CHECKBOX, StaticControl.SPEC_PROVIDER),

	CONTEXT_MENU(ControlType.CONTEXT_MENU, StaticControl.SPEC_PROVIDER),
	MENU(ControlType.MENU, StaticControl.SPEC_PROVIDER),
	MENU_STRIP(ControlType.MENU_STRIP, StaticControl.SPEC_PROVIDER),

	OBJECT(ControlType.OBJECT, StaticControl.SPEC_PROVIDER),
	OBJECT_ZOOM(ControlType.OBJECT_ZOOM, StaticControl.SPEC_PROVIDER),
	OBJECT_CONTAINER(ControlType.OBJECT_CONTAINER, StaticControl.SPEC_PROVIDER),
	OBJECT_CONT_ANIM(ControlType.OBJECT_CONT_ANIM, StaticControl.SPEC_PROVIDER),

	MAP(ControlType.MAP, StaticControl.SPEC_PROVIDER),
	MAP_MAIN(ControlType.MAP_MAIN, StaticControl.SPEC_PROVIDER);

	public final ControlType controlType;
	public final ArmaControlSpecProvider specProvider;

	ArmaControlLookup(ControlType controlType, ArmaControlSpecProvider specProvider) {
		this.controlType = controlType;
		this.specProvider = specProvider;
	}

	public static ArmaControlLookup findByControlType(ControlType toFind) {
		for (ArmaControlLookup lookup : values()) {
			if (lookup.controlType == toFind) {
				return lookup;
			}
		}
		return null;
	}
}
