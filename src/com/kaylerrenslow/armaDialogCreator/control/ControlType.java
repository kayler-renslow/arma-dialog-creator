package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	STATIC(0, Lang.ControlType.STATIC, TypeGroup.TEXT),
	HTML(9, Lang.ControlType.HTML, TypeGroup.TEXT),
	EDIT(2, Lang.ControlType.EDIT, TypeGroup.TEXT),
	STRUCTURED_TEXT(13, Lang.ControlType.STRUCTURED_TEXT, TypeGroup.TEXT),
	ACTIVETEXT(11, Lang.ControlType.ACTIVETEXT, TypeGroup.TEXT),
	
	BUTTON(1, Lang.ControlType.BUTTON, TypeGroup.BUTTON),
	SHORTCUTBUTTON(16, Lang.ControlType.SHORTCUTBUTTON, TypeGroup.BUTTON),
	XBUTTON(41, Lang.ControlType.XBUTTON, TypeGroup.BUTTON),
	
	PROGRESS(8, Lang.ControlType.PROGRESS, TypeGroup.MISC),
	STATIC_SKEW(10, Lang.ControlType.STATIC_SKEW, TypeGroup.MISC),
	LINEBREAK(98, Lang.ControlType.LINEBREAK, TypeGroup.MISC),
	TREE(12, Lang.ControlType.TREE, TypeGroup.MISC),
	CONTROLS_GROUP(15, Lang.ControlType.CONTROLS_GROUP, TypeGroup.MISC),
	XKEYDESC(40, Lang.ControlType.XKEYDESC, TypeGroup.MISC),
	ANIMATED_TEXTURE(45, Lang.ControlType.ANIMATED_TEXTURE, TypeGroup.MISC),
	ANIMATED_USER(99, Lang.ControlType.ANIMATED_USER, TypeGroup.MISC),
	ITEMSLOT(103, Lang.ControlType.ITEMSLOT, TypeGroup.MISC),
	
	SLIDER(3, Lang.ControlType.SLIDER, TypeGroup.SLIDER, true),
	XSLIDER(43, Lang.ControlType.XSLIDER, TypeGroup.SLIDER),
	
	COMBO(4, Lang.ControlType.COMBO, TypeGroup.COMBO),
	XCOMBO(44, Lang.ControlType.XCOMBO, TypeGroup.COMBO),
	
	LISTBOX(5, Lang.ControlType.LISTBOX, TypeGroup.LIST_BOX),
	XLISTBOX(42, Lang.ControlType.XLISTBOX, TypeGroup.LIST_BOX),
	LISTNBOX(102, Lang.ControlType.LISTNBOX, TypeGroup.LIST_BOX),
	
	TOOLBOX(6, Lang.ControlType.TOOLBOX, TypeGroup.CHECK_BOX),
	CHECKBOXES(7, Lang.ControlType.CHECKBOXES, TypeGroup.CHECK_BOX),
	CHECKBOX(77, Lang.ControlType.CHECKBOX, TypeGroup.CHECK_BOX),
	
	CONTEXT_MENU(14, Lang.ControlType.CONTEXT_MENU, TypeGroup.MENU),
	MENU(46, Lang.ControlType.MENU, TypeGroup.MENU),
	MENU_STRIP(47, Lang.ControlType.MENU_STRIP, TypeGroup.MENU),
	
	OBJECT(80, Lang.ControlType.OBJECT, TypeGroup.OBJECT),
	OBJECT_ZOOM(81, Lang.ControlType.OBJECT_ZOOM, TypeGroup.OBJECT),
	OBJECT_CONTAINER(82, Lang.ControlType.OBJECT_CONTAINER, TypeGroup.OBJECT),
	OBJECT_CONT_ANIM(83, Lang.ControlType.OBJECT_CONT_ANIM, TypeGroup.OBJECT),
	
	MAP(100, Lang.ControlType.MAP, TypeGroup.MAP),
	MAP_MAIN(101, Lang.ControlType.MAP_MAIN, TypeGroup.MAP);
	//@formatter:on
	
	public enum TypeGroup {
		TEXT(Lang.ControlType.TypeGroup.TEXT), BUTTON(Lang.ControlType.TypeGroup.BUTTON), COMBO(Lang.ControlType.TypeGroup.COMBO), SLIDER(Lang.ControlType.TypeGroup.SLIDER),
		LIST_BOX(Lang.ControlType.TypeGroup.LIST_BOX), CHECK_BOX(Lang.ControlType.TypeGroup.CHECK_BOX), MENU(Lang.ControlType.TypeGroup.MENU), OBJECT(Lang.ControlType.TypeGroup.OBJECT),
		MAP(Lang.ControlType.TypeGroup.MAP), MISC(Lang.ControlType.TypeGroup.MISC);
		
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
	
	private static final ControlType[] supported = {STATIC};
	
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
