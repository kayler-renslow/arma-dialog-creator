package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	Static(0, getString("ControlType.static"), ControlTypeGroup.TEXT, "control_static.png"),
	HTML(9, getString("ControlType.html"), ControlTypeGroup.TEXT, "control_html.png"),
	Edit(2, getString("ControlType.edit"), ControlTypeGroup.TEXT, "control_edit.png"),
	StructuredText(13, getString("ControlType.structured_text"), ControlTypeGroup.TEXT, "control_structuredtext.png"),
	ActiveText(11, getString("ControlType.activetext"), ControlTypeGroup.TEXT, "control_activetext.png"),
	
	Button(1, getString("ControlType.button"), ControlTypeGroup.BUTTON, "control_button.png"),
	ShortcutButton(16, getString("ControlType.shortcutbutton"), ControlTypeGroup.BUTTON, "control_shortcutbutton.png"),
	XButton(41, getString("ControlType.xbutton"), ControlTypeGroup.BUTTON),
	
	Progress(8, getString("ControlType.progress"), ControlTypeGroup.MISC, "control_progress.png"),
	StaticSkew(10, getString("ControlType.static_skew"), ControlTypeGroup.MISC),
	Linebreak(98, getString("ControlType.linebreak"), ControlTypeGroup.MISC),
	Tree(12, getString("ControlType.tree"), ControlTypeGroup.MISC, "control_tree.png"),
	ControlsGroup(15, getString("ControlType.controls_group"), ControlTypeGroup.MISC, "control_group.png"),
	XKeyDesc(40, getString("ControlType.xkeydesc"), ControlTypeGroup.MISC),
	AnimatedTexture(45, getString("ControlType.animated_texture"), ControlTypeGroup.MISC),
	AnimatedUser(99, getString("ControlType.animated_user"), ControlTypeGroup.MISC),
	ItemSlot(103, getString("ControlType.itemslot"), ControlTypeGroup.MISC),
	
	Slider(3, getString("ControlType.slider"), ControlTypeGroup.SLIDER, "control_slider.png", true),
	XSlider(43, getString("ControlType.xslider"), ControlTypeGroup.SLIDER, "control_slider.png"),
	
	Combo(4, getString("ControlType.combo"), ControlTypeGroup.COMBO, "control_combobox.png"),
	XCombo(44, getString("ControlType.xcombo"), ControlTypeGroup.COMBO),
	
	ListBox(5, getString("ControlType.listbox"), ControlTypeGroup.LIST_BOX, "control_listbox.png"),
	XListBox(42, getString("ControlType.xlistbox"), ControlTypeGroup.LIST_BOX, "control_xlistbox.png"),
	ListNBox(102, getString("ControlType.listnbox"), ControlTypeGroup.LIST_BOX, "control_nlistbox.png"),
	
	ToolBox(6, getString("ControlType.toolbox"), ControlTypeGroup.CHECK_BOX),
	CheckBoxes(7, getString("ControlType.checkboxes"), ControlTypeGroup.CHECK_BOX),
	CheckBox(77, getString("ControlType.checkbox"), ControlTypeGroup.CHECK_BOX),
	
	ContextMenu(14, getString("ControlType.context_menu"), ControlTypeGroup.MENU),
	Menu(46, getString("ControlType.menu"), ControlTypeGroup.MENU),
	MenuStrip(47, getString("ControlType.menu_strip"), ControlTypeGroup.MENU),
	
	Object(80, getString("ControlType.object"), ControlTypeGroup.OBJECT),
	ObjectZoom(81, getString("ControlType.object_zoom"), ControlTypeGroup.OBJECT),
	ObjectContainer(82, getString("ControlType.object_container"), ControlTypeGroup.OBJECT),
	ObjectContAnim(83, getString("ControlType.object_cont_anim"), ControlTypeGroup.OBJECT),
	
	Map(100, getString("ControlType.map"), ControlTypeGroup.MAP),
	MapMain(101, getString("ControlType.map_main"), ControlTypeGroup.MAP),

	/**To be used for TESTING only. Do not use for client code.*/
	_Test(Integer.MIN_VALUE, "ADC TEST", ControlTypeGroup.TEXT);
	//@formatter:on

	/** all control types that are supported for the application */
	public static final ControlType[] AVAILABLE_TYPES = {
			Static, ControlsGroup, Button, ShortcutButton, Edit,
			XSlider, Combo
	};

	private final int typeId;
	private final String displayName;
	private final Image icon;
	private final Image customIcon;

	private final boolean deprecated;
	private final ControlTypeGroup group;

	ControlType(int typeId, String displayName, ControlTypeGroup group) {
		this(typeId, displayName, group, ControlIcons.placeholder, false);
	}

	ControlType(int typeId, String displayName, ControlTypeGroup group, String iconPath) {
		this(typeId, displayName, group, iconPath, false);
	}

	ControlType(int typeId, String displayName, ControlTypeGroup group, String iconPath, boolean deprecated) {
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


	/** Return a string formatted as such: 'displayName (typeId)'. {@link #toString()} will return this value */
	@NotNull
	public String fullDisplayText() {
		return getDisplayName() + " (" + getTypeId() + ")";
	}

	/** Return the class name for the root control type */
	@NotNull
	public String getNameAsClassName() {
		return "ADC_" + getDisplayName().replaceAll("\\s", "_");
	}

	/**
	 Get the control type by finding the matched between {@link #typeId} and the parameter typeId

	 @throws IllegalArgumentException when id couldn't be matched
	 */
	@NotNull
	public static ControlType findById(int typeId) {
		for (ControlType type : values()) {
			if (type.getTypeId() == typeId) {
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
		for (ControlType type : AVAILABLE_TYPES) {
			if (this == type) {
				return true;
			}
		}
		return false;
	}


	public int getTypeId() {
		return typeId;
	}

	@NotNull
	public String getDisplayName() {
		return displayName;
	}

	/** Default icon */
	@NotNull
	public Image getIcon() {
		return icon;
	}

	/** The icon used to designate that the {@link ControlClass} using this type is from {@link CustomControlClass} */
	@NotNull
	public Image getCustomIcon() {
		return customIcon;
	}

	/** If true, the type should be avoided. */
	public boolean isDeprecated() {
		return deprecated;
	}

	@NotNull
	public ControlTypeGroup getGroup() {
		return group;
	}


	private static Image getImage(String image, boolean custom) {
		return new Image("/com/kaylerrenslow/armaDialogCreator/gui/img/icons/controls/" + (custom ? "custom." : "") + image);
	}


	//remove interface and members when all controls have unique icons
	private interface ControlIcons {
		String placeholder = "placeholder.png";
	}


}
