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

import com.kaylerrenslow.armaDialogCreator.main.lang.LookupLang;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	STATIC(0, LookupLang.ControlType.STATIC, TypeGroup.TEXT),
	HTML(9, LookupLang.ControlType.HTML, TypeGroup.TEXT),
	EDIT(2, LookupLang.ControlType.EDIT, TypeGroup.TEXT),
	STRUCTURED_TEXT(13, LookupLang.ControlType.STRUCTURED_TEXT, TypeGroup.TEXT),
	ACTIVETEXT(11, LookupLang.ControlType.ACTIVETEXT, TypeGroup.TEXT),
	
	BUTTON(1, LookupLang.ControlType.BUTTON, TypeGroup.BUTTON),
	SHORTCUTBUTTON(16, LookupLang.ControlType.SHORTCUTBUTTON, TypeGroup.BUTTON),
	XBUTTON(41, LookupLang.ControlType.XBUTTON, TypeGroup.BUTTON),
	
	PROGRESS(8, LookupLang.ControlType.PROGRESS, TypeGroup.MISC),
	STATIC_SKEW(10, LookupLang.ControlType.STATIC_SKEW, TypeGroup.MISC),
	LINEBREAK(98, LookupLang.ControlType.LINEBREAK, TypeGroup.MISC),
	TREE(12, LookupLang.ControlType.TREE, TypeGroup.MISC),
	CONTROLS_GROUP(15, LookupLang.ControlType.CONTROLS_GROUP, TypeGroup.MISC),
	XKEYDESC(40, LookupLang.ControlType.XKEYDESC, TypeGroup.MISC),
	ANIMATED_TEXTURE(45, LookupLang.ControlType.ANIMATED_TEXTURE, TypeGroup.MISC),
	ANIMATED_USER(99, LookupLang.ControlType.ANIMATED_USER, TypeGroup.MISC),
	ITEMSLOT(103, LookupLang.ControlType.ITEMSLOT, TypeGroup.MISC),
	
	SLIDER(3, LookupLang.ControlType.SLIDER, TypeGroup.SLIDER, true),
	XSLIDER(43, LookupLang.ControlType.XSLIDER, TypeGroup.SLIDER),
	
	COMBO(4, LookupLang.ControlType.COMBO, TypeGroup.COMBO),
	XCOMBO(44, LookupLang.ControlType.XCOMBO, TypeGroup.COMBO),
	
	LISTBOX(5, LookupLang.ControlType.LISTBOX, TypeGroup.LIST_BOX),
	XLISTBOX(42, LookupLang.ControlType.XLISTBOX, TypeGroup.LIST_BOX),
	LISTNBOX(102, LookupLang.ControlType.LISTNBOX, TypeGroup.LIST_BOX),
	
	TOOLBOX(6, LookupLang.ControlType.TOOLBOX, TypeGroup.CHECK_BOX),
	CHECKBOXES(7, LookupLang.ControlType.CHECKBOXES, TypeGroup.CHECK_BOX),
	CHECKBOX(77, LookupLang.ControlType.CHECKBOX, TypeGroup.CHECK_BOX),
	
	CONTEXT_MENU(14, LookupLang.ControlType.CONTEXT_MENU, TypeGroup.MENU),
	MENU(46, LookupLang.ControlType.MENU, TypeGroup.MENU),
	MENU_STRIP(47, LookupLang.ControlType.MENU_STRIP, TypeGroup.MENU),
	
	OBJECT(80, LookupLang.ControlType.OBJECT, TypeGroup.OBJECT),
	OBJECT_ZOOM(81, LookupLang.ControlType.OBJECT_ZOOM, TypeGroup.OBJECT),
	OBJECT_CONTAINER(82, LookupLang.ControlType.OBJECT_CONTAINER, TypeGroup.OBJECT),
	OBJECT_CONT_ANIM(83, LookupLang.ControlType.OBJECT_CONT_ANIM, TypeGroup.OBJECT),
	
	MAP(100, LookupLang.ControlType.MAP, TypeGroup.MAP),
	MAP_MAIN(101, LookupLang.ControlType.MAP_MAIN, TypeGroup.MAP);
	//@formatter:on
	
	public enum TypeGroup {
		TEXT(LookupLang.ControlType.TypeGroup.TEXT), BUTTON(LookupLang.ControlType.TypeGroup.BUTTON), COMBO(LookupLang.ControlType.TypeGroup.COMBO), SLIDER(LookupLang.ControlType.TypeGroup.SLIDER),
		LIST_BOX(LookupLang.ControlType.TypeGroup.LIST_BOX), CHECK_BOX(LookupLang.ControlType.TypeGroup.CHECK_BOX), MENU(LookupLang.ControlType.TypeGroup.MENU), OBJECT(LookupLang.ControlType.TypeGroup.OBJECT),
		MAP(LookupLang.ControlType.TypeGroup.MAP), MISC(LookupLang.ControlType.TypeGroup.MISC);
		
		public final String displayName;
		
		TypeGroup(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public final int typeId;
	public final String displayName;
	/** If true, the type should be avoided. */
	public final boolean deprecated;
	public final TypeGroup group;
	
	ControlType(int typeId, String displayName, TypeGroup group) {
		this(typeId, displayName, group, false);
	}
	
	//todo: do not add ArmaControlClasses in here. Have a different enum so that you can create custom controls and specify the same type again (like RscPicture and RscFrame both use type STATIC)
	ControlType(int typeId, String displayName, TypeGroup group, boolean deprecated) {
		this.typeId = typeId;
		this.displayName = displayName;
		this.group = group;
		this.deprecated = deprecated;
	}
	
	@Override
	public String toString() {
		return fullDisplayText();
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
	public static ControlType getById(int typeId) {
		for (ControlType type : values()) {
			if (type.typeId == typeId) {
				return type;
			}
		}
		throw new IllegalArgumentException("typeId " + typeId + " couldn't be matched.");
	}
	
	private static final ControlType[] supported = {STATIC, CONTROLS_GROUP};
	
	/** Return true if ControlType's is supported in the beta */
	public boolean betaSupported() {
		for (ControlType type : supported) {
			if (this == type) {
				return true;
			}
		}
		return false;
	}
	
}
