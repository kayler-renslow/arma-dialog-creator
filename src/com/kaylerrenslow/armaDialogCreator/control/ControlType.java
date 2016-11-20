/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVIntegerUnmodifiable;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.image.Image;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	STATIC(0, getString("ControlType.static"), TypeGroup.TEXT, "control_static.png"),
	HTML(9, getString("ControlType.html"), TypeGroup.TEXT, "control_html.png"),
	EDIT(2, getString("ControlType.edit"), TypeGroup.TEXT, "control_edit.png"),
	STRUCTURED_TEXT(13, getString("ControlType.structured_text"), TypeGroup.TEXT, "control_structuredtext.png"),
	ACTIVETEXT(11, getString("ControlType.activetext"), TypeGroup.TEXT, "control_activetext.png"),
	
	BUTTON(1, getString("ControlType.button"), TypeGroup.BUTTON, "control_button.png"),
	SHORTCUTBUTTON(16, getString("ControlType.shortcutbutton"), TypeGroup.BUTTON, "control_shortcutbutton.png"),
	XBUTTON(41, getString("ControlType.xbutton"), TypeGroup.BUTTON),
	
	PROGRESS(8, getString("ControlType.progress"), TypeGroup.MISC, "control_progress.png"),
	STATIC_SKEW(10, getString("ControlType.static_skew"), TypeGroup.MISC),
	LINEBREAK(98, getString("ControlType.linebreak"), TypeGroup.MISC),
	TREE(12, getString("ControlType.tree"), TypeGroup.MISC, "control_tree.png"),
	CONTROLS_GROUP(15, getString("ControlType.controls_group"), TypeGroup.MISC, "control_group.png"),
	XKEYDESC(40, getString("ControlType.xkeydesc"), TypeGroup.MISC),
	ANIMATED_TEXTURE(45, getString("ControlType.animated_texture"), TypeGroup.MISC),
	ANIMATED_USER(99, getString("ControlType.animated_user"), TypeGroup.MISC),
	ITEMSLOT(103, getString("ControlType.itemslot"), TypeGroup.MISC),
	
	SLIDER(3, getString("ControlType.slider"), TypeGroup.SLIDER, "control_slider.png", true),
	XSLIDER(43, getString("ControlType.xslider"), TypeGroup.SLIDER),
	
	COMBO(4, getString("ControlType.combo"), TypeGroup.COMBO, "control_combobox.png"),
	XCOMBO(44, getString("ControlType.xcombo"), TypeGroup.COMBO),
	
	LISTBOX(5, getString("ControlType.listbox"), TypeGroup.LIST_BOX, "control_listbox.png"),
	XLISTBOX(42, getString("ControlType.xlistbox"), TypeGroup.LIST_BOX, "control_xlistbox.png"),
	LISTNBOX(102, getString("ControlType.listnbox"), TypeGroup.LIST_BOX, "control_nlistbox.png"),
	
	TOOLBOX(6, getString("ControlType.toolbox"), TypeGroup.CHECK_BOX),
	CHECKBOXES(7, getString("ControlType.checkboxes"), TypeGroup.CHECK_BOX),
	CHECKBOX(77, getString("ControlType.checkbox"), TypeGroup.CHECK_BOX),
	
	CONTEXT_MENU(14, getString("ControlType.context_menu"), TypeGroup.MENU),
	MENU(46, getString("ControlType.menu"), TypeGroup.MENU),
	MENU_STRIP(47, getString("ControlType.menu_strip"), TypeGroup.MENU),
	
	OBJECT(80, getString("ControlType.object"), TypeGroup.OBJECT),
	OBJECT_ZOOM(81, getString("ControlType.object_zoom"), TypeGroup.OBJECT),
	OBJECT_CONTAINER(82, getString("ControlType.object_container"), TypeGroup.OBJECT),
	OBJECT_CONT_ANIM(83, getString("ControlType.object_cont_anim"), TypeGroup.OBJECT),
	
	MAP(100, getString("ControlType.map"), TypeGroup.MAP),
	MAP_MAIN(101, getString("ControlType.map_main"), TypeGroup.MAP);
	//@formatter:on

	public enum TypeGroup {
		TEXT(getString("ControlType.TypeGroup.text")), BUTTON(getString("ControlType.TypeGroup.button")), COMBO(getString("ControlType.TypeGroup.combo")), SLIDER(getString("ControlType.TypeGroup.slider")),
		LIST_BOX(getString("ControlType.TypeGroup.list_box")), CHECK_BOX(getString("ControlType.TypeGroup.check_box")), MENU(getString("ControlType.TypeGroup.menu")), OBJECT(getString("ControlType.TypeGroup.object")),
		MAP(getString("ControlType.TypeGroup.map")), MISC(getString("ControlType.TypeGroup.misc"));

		public final String displayName;

		TypeGroup(String displayName) {
			this.displayName = displayName;
		}
	}

	public static final ControlType[] BETA_SUPPORTED = {STATIC, CONTROLS_GROUP};

	public final int typeId;
	public final String displayName;
	/** Default icon */
	public final Image icon;
	/** The icon used to designate that the {@link ControlClass} using this type is from {@link CustomControlClass} */
	public final Image customIcon;

	/** If true, the type should be avoided. */
	public final boolean deprecated;
	public final TypeGroup group;
	private SVIntegerUnmodifiable sv;

	ControlType(int typeId, String displayName, TypeGroup group) {
		this(typeId, displayName, group, ControlIcons.placeholder, false);
	}

	ControlType(int typeId, String displayName, TypeGroup group, String iconPath) {
		this(typeId, displayName, group, iconPath, false);
	}

	//todo: do not add ArmaControlClasses in here. Have a different enum so that you can create custom controls and specify the same type again (like RscPicture and RscFrame both use type STATIC)
	ControlType(int typeId, String displayName, TypeGroup group, String iconPath, boolean deprecated) {
		this.typeId = typeId;
		this.displayName = displayName;
		this.group = group;
		this.icon = getImage(iconPath, false);
		this.customIcon = getImage(iconPath, true);
		this.deprecated = deprecated;
	}

	@Override
	public String toString() {
		return fullDisplayText();
	}


	/** Returns the {@link #typeId} inside a {@link SVIntegerUnmodifiable} instance. Only one instance is returned */
	public SVIntegerUnmodifiable toSerializableValue() {
		if (sv == null) {
			sv = new SVIntegerUnmodifiable(typeId);
		}
		return sv;
	}


	/** Return a string formatted as such: 'displayName (typeId)'. {@link #toString()} will return this value */
	public String fullDisplayText() {
		return displayName + " (" + typeId + ")";
	}

	/** Return the class name for the root control type */
	public String getNameAsClassName() {
		return "ADC_" + displayName.replaceAll("\\s", "_");
	}

	/**
	 Get the control type by finding the matched between {@link #typeId} and the parameter typeId

	 @throws IllegalArgumentException when id couldn't be matched
	 */
	public static ControlType findById(int typeId) {
		for (ControlType type : values()) {
			if (type.typeId == typeId) {
				return type;
			}
		}
		throw new IllegalArgumentException("typeId " + typeId + " couldn't be matched.");
	}

	private static String getString(String s) {
		return Lang.LookupBundle().getString(s);
	}

	/** Return true if ControlType's is supported in the beta */
	public boolean betaSupported() {
		for (ControlType type : BETA_SUPPORTED) {
			if (this == type) {
				return true;
			}
		}
		return false;
	}


	private static Image getImage(String image, boolean custom) {
		return new Image("/com/kaylerrenslow/armaDialogCreator/gui/img/icons/controls/" + (custom ? "custom." : "") + image);
	}


	//remove interface and members when all controls have unique icons
	private interface ControlIcons {
		String placeholder = "placeholder.png";
	}


}
