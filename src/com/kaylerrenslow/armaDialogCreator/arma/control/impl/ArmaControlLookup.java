/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.control.impl;

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecRequirement;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import org.jetbrains.annotations.NotNull;

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
	public final ArmaControlSpecRequirement specProvider;

	ArmaControlLookup(ControlType controlType, ArmaControlSpecRequirement specProvider) {
		this.controlType = controlType;
		this.specProvider = specProvider;
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
