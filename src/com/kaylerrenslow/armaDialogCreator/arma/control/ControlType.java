package com.kaylerrenslow.armaDialogCreator.arma.control;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	STATIC(0, "Static"),
	BUTTON(1, "Button"),
	EDIT(2, "Edit"),
	SLIDER(3, "Slider"),
	COMBO(4, "Combo"),
	LISTBOX(5, "List Box"),
	TOOLBOX(6, "Tool Box"),
	CHECKBOXES(7, "Check Boxes"),
	PROGRESS(8, "Progress Bar"),
	HTML(9, "HTML"),
	STATIC_SKEW(10, "Static Skew"),
	ACTIVETEXT(11, "Active Text"),
	TREE(12, "Tree"),
	STRUCTURED_TEXT(13, "Structured Text"),
	CONTEXT_MENU(14, "Context Menu"),
	CONTROLS_GROUP(15, "Controls Group"),
	SHORTCUTBUTTON(16, "Shortcut Button"),
	XKEYDESC(40, "XKEYDESC"),
	XBUTTON(41, "XButton"),
	XLISTBOX(42, "XList Box"),
	XSLIDER(43, "XSlider"),
	XCOMBO(44, "XCombo"),
	ANIMATED_TEXTURE(45, "Animated Texture"),
	MENU(46, "Menu"),
	MENU_STRIP(47, "Menu Strip"),
	CHECKBOX(77, "Check Box"),
	OBJECT(80, "Object"),
	OBJECT_ZOOM(81, "Object Zoom"),
	OBJECT_CONTAINER(82, "Object Container"),
	OBJECT_CONT_ANIM(83, "Object Container Animation"),
	LINEBREAK(98, "Line Break"),
	ANIMATED_USER(99, "Animated User"),
	MAP(100, "Map"),
	MAP_MAIN(101, "Map Main"),
	LISTNBOX(102, "ListNBox"),
	ITEMSLOT(103, "Item Slot");
	//@formatter:on

	public final int typeId;
	public final String displayName;

	ControlType(int typeId, String displayName) {
		this.typeId = typeId;
		this.displayName = displayName;
	}
}
