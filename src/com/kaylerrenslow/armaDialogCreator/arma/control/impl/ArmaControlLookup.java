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

import com.kaylerrenslow.armaDialogCreator.arma.control.ArmaControlSpecProvider;
import com.kaylerrenslow.armaDialogCreator.control.ControlType;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/07/2016.
 */
public enum ArmaControlLookup {
	STATIC(ControlType.STATIC, StaticControl.SPEC_PROVIDER, new Image(getPath("control_static.png"))),
	HTML(ControlType.HTML, StaticControl.SPEC_PROVIDER, new Image(getPath("control_html.png"))),
	EDIT(ControlType.EDIT, StaticControl.SPEC_PROVIDER, new Image(getPath("control_edit.png"))),
	STRUCTURED_TEXT(ControlType.STRUCTURED_TEXT, StaticControl.SPEC_PROVIDER),
	ACTIVETEXT(ControlType.ACTIVETEXT, StaticControl.SPEC_PROVIDER),

	BUTTON(ControlType.BUTTON, StaticControl.SPEC_PROVIDER, new Image(getPath("control_button.png"))),
	SHORTCUTBUTTON(ControlType.SHORTCUTBUTTON, StaticControl.SPEC_PROVIDER),
	XBUTTON(ControlType.XBUTTON, StaticControl.SPEC_PROVIDER),

	PROGRESS(ControlType.PROGRESS, StaticControl.SPEC_PROVIDER, new Image(getPath("control_progress.png"))),
	STATIC_SKEW(ControlType.STATIC_SKEW, StaticControl.SPEC_PROVIDER),
	LINEBREAK(ControlType.LINEBREAK, StaticControl.SPEC_PROVIDER),
	TREE(ControlType.TREE, StaticControl.SPEC_PROVIDER, new Image(getPath("control_tree.png"))),
	CONTROLS_GROUP(ControlType.CONTROLS_GROUP, ControlGroupControl.SPEC_PROVIDER, new Image(getPath("control_group.png"))),
	XKEYDESC(ControlType.XKEYDESC, StaticControl.SPEC_PROVIDER),
	ANIMATED_TEXTURE(ControlType.ANIMATED_TEXTURE, StaticControl.SPEC_PROVIDER),
	ANIMATED_USER(ControlType.ANIMATED_USER, StaticControl.SPEC_PROVIDER),
	ITEMSLOT(ControlType.ITEMSLOT, StaticControl.SPEC_PROVIDER),

	SLIDER(ControlType.SLIDER, StaticControl.SPEC_PROVIDER, new Image(getPath("control_slider.png"))),
	XSLIDER(ControlType.XSLIDER, StaticControl.SPEC_PROVIDER),

	COMBO(ControlType.COMBO, StaticControl.SPEC_PROVIDER, new Image(getPath("control_combobox.png"))),
	XCOMBO(ControlType.XCOMBO, StaticControl.SPEC_PROVIDER),

	LISTBOX(ControlType.LISTBOX, StaticControl.SPEC_PROVIDER, new Image(getPath("control_listbox.png"))),
	XLISTBOX(ControlType.XLISTBOX, StaticControl.SPEC_PROVIDER, new Image(getPath("control_xlistbox.png"))),
	LISTNBOX(ControlType.LISTNBOX, StaticControl.SPEC_PROVIDER, new Image(getPath("control_nlistbox.png"))),

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
	public final Image controlIcon;

	ArmaControlLookup(ControlType controlType, ArmaControlSpecProvider specProvider) {
		this(controlType, specProvider, ArmaControlIcons.placeholder);
	}

	ArmaControlLookup(ControlType controlType, ArmaControlSpecProvider specProvider, Image controlIcon) {
		this.controlType = controlType;
		this.specProvider = specProvider;
		this.controlIcon = controlIcon;
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


	private static String getPath(String image){
		return "/com/kaylerrenslow/armaDialogCreator/gui/img/icons/arma/"+image;
	}

	//remove interface and members when all controls have unique icons
	private interface ArmaControlIcons{
		Image placeholder = new Image(getPath("control_static.png"));
	}


}
