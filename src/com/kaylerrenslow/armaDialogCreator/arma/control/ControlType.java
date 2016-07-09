package com.kaylerrenslow.armaDialogCreator.arma.control;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	STATIC(0, "Static", TypeGroup.TEXT),
	HTML(9, "HTML", TypeGroup.TEXT),
	EDIT(2, "Edit", TypeGroup.TEXT),
	STRUCTURED_TEXT(13, "Structured Text", TypeGroup.TEXT),
	ACTIVETEXT(11, "Active Text", TypeGroup.TEXT),

	BUTTON(1, "Button", TypeGroup.BUTTON),
	SHORTCUTBUTTON(16, "Shortcut Button", TypeGroup.BUTTON),
	XBUTTON(41, "X Button", TypeGroup.BUTTON),

	PROGRESS(8, "Progress Bar", TypeGroup.MISC),
	STATIC_SKEW(10, "Static Skew", TypeGroup.MISC),
	LINEBREAK(98, "Line Break", TypeGroup.MISC),
	TREE(12, "Tree", TypeGroup.MISC),
	CONTROLS_GROUP(15, "Controls Group", TypeGroup.MISC),
	XKEYDESC(40, "XKEYDESC", TypeGroup.MISC),
	ANIMATED_TEXTURE(45, "Animated Texture", TypeGroup.MISC),
	ANIMATED_USER(99, "Animated User", TypeGroup.MISC),
	ITEMSLOT(103, "Item Slot", TypeGroup.MISC),

	SLIDER(3, "Slider", TypeGroup.SLIDER, true),
	XSLIDER(43, "X Slider", TypeGroup.SLIDER),

	COMBO(4, "Combo", TypeGroup.COMBO),
	XCOMBO(44, "X Combo", TypeGroup.COMBO),

	LISTBOX(5, "List Box", TypeGroup.LIST_BOX),
	XLISTBOX(42, "X List Box", TypeGroup.LIST_BOX),
	LISTNBOX(102, "List N Box", TypeGroup.LIST_BOX),

	TOOLBOX(6, "Tool Box", TypeGroup.CHECK_BOX),
	CHECKBOXES(7, "Check Boxes", TypeGroup.CHECK_BOX),
	CHECKBOX(77, "Check Box", TypeGroup.CHECK_BOX),

	CONTEXT_MENU(14, "Context Menu", TypeGroup.MENU),
	MENU(46, "Menu", TypeGroup.MENU),
	MENU_STRIP(47, "Menu Strip", TypeGroup.MENU),

	OBJECT(80, "Object", TypeGroup.OBJECT),
	OBJECT_ZOOM(81, "Object Zoom", TypeGroup.OBJECT),
	OBJECT_CONTAINER(82, "Object Container", TypeGroup.OBJECT),
	OBJECT_CONT_ANIM(83, "Object Container Animation", TypeGroup.OBJECT),

	MAP(100, "Map", TypeGroup.MAP),
	MAP_MAIN(101, "Map Main", TypeGroup.MAP);
	//@formatter:on

	public enum TypeGroup {
		TEXT("Text"), BUTTON("Button"), COMBO("Combo Box"), SLIDER("Slider"), LIST_BOX("List Box"), CHECK_BOX("Check Box"), MENU("Menu"), OBJECT("Object"), MAP("Map"), MISC("Misc");

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

	/**Return a string formatted as such: 'displayName (typeId)'. {@link #toString()} will return this value*/
	public String fullDisplayText() {
		return displayName + " (" + typeId + ")";
	}

	/**Return the class name for the root control type*/
	public String getNameAsClassName(){
		return "ADC_" + displayName.replaceAll("\\s", "_");
	}

	/** Get the control type by finding the matched between {@link #typeId} and the parameter typeId */
	public static ControlType getById(int typeId) {
		for (ControlType type : values()) {
			if (type.typeId == typeId) {
				return type;
			}
		}
		return null;
	}
}
