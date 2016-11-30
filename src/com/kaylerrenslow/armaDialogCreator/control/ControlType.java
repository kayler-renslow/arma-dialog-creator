package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.SVIntegerUnmodifiable;
import com.kaylerrenslow.armaDialogCreator.main.Lang;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 05/20/2016.
 */
public enum ControlType {
	//@formatter:off
	STATIC(0, getString("ControlType.static"), ControlTypeGroup.TEXT, "control_static.png"),
	HTML(9, getString("ControlType.html"), ControlTypeGroup.TEXT, "control_html.png"),
	EDIT(2, getString("ControlType.edit"), ControlTypeGroup.TEXT, "control_edit.png"),
	STRUCTURED_TEXT(13, getString("ControlType.structured_text"), ControlTypeGroup.TEXT, "control_structuredtext.png"),
	ACTIVETEXT(11, getString("ControlType.activetext"), ControlTypeGroup.TEXT, "control_activetext.png"),
	
	BUTTON(1, getString("ControlType.button"), ControlTypeGroup.BUTTON, "control_button.png"),
	SHORTCUTBUTTON(16, getString("ControlType.shortcutbutton"), ControlTypeGroup.BUTTON, "control_shortcutbutton.png"),
	XBUTTON(41, getString("ControlType.xbutton"), ControlTypeGroup.BUTTON),
	
	PROGRESS(8, getString("ControlType.progress"), ControlTypeGroup.MISC, "control_progress.png"),
	STATIC_SKEW(10, getString("ControlType.static_skew"), ControlTypeGroup.MISC),
	LINEBREAK(98, getString("ControlType.linebreak"), ControlTypeGroup.MISC),
	TREE(12, getString("ControlType.tree"), ControlTypeGroup.MISC, "control_tree.png"),
	CONTROLS_GROUP(15, getString("ControlType.controls_group"), ControlTypeGroup.MISC, "control_group.png"),
	XKEYDESC(40, getString("ControlType.xkeydesc"), ControlTypeGroup.MISC),
	ANIMATED_TEXTURE(45, getString("ControlType.animated_texture"), ControlTypeGroup.MISC),
	ANIMATED_USER(99, getString("ControlType.animated_user"), ControlTypeGroup.MISC),
	ITEMSLOT(103, getString("ControlType.itemslot"), ControlTypeGroup.MISC),
	
	SLIDER(3, getString("ControlType.slider"), ControlTypeGroup.SLIDER, "control_slider.png", true),
	XSLIDER(43, getString("ControlType.xslider"), ControlTypeGroup.SLIDER),
	
	COMBO(4, getString("ControlType.combo"), ControlTypeGroup.COMBO, "control_combobox.png"),
	XCOMBO(44, getString("ControlType.xcombo"), ControlTypeGroup.COMBO),
	
	LISTBOX(5, getString("ControlType.listbox"), ControlTypeGroup.LIST_BOX, "control_listbox.png"),
	XLISTBOX(42, getString("ControlType.xlistbox"), ControlTypeGroup.LIST_BOX, "control_xlistbox.png"),
	LISTNBOX(102, getString("ControlType.listnbox"), ControlTypeGroup.LIST_BOX, "control_nlistbox.png"),
	
	TOOLBOX(6, getString("ControlType.toolbox"), ControlTypeGroup.CHECK_BOX),
	CHECKBOXES(7, getString("ControlType.checkboxes"), ControlTypeGroup.CHECK_BOX),
	CHECKBOX(77, getString("ControlType.checkbox"), ControlTypeGroup.CHECK_BOX),
	
	CONTEXT_MENU(14, getString("ControlType.context_menu"), ControlTypeGroup.MENU),
	MENU(46, getString("ControlType.menu"), ControlTypeGroup.MENU),
	MENU_STRIP(47, getString("ControlType.menu_strip"), ControlTypeGroup.MENU),
	
	OBJECT(80, getString("ControlType.object"), ControlTypeGroup.OBJECT),
	OBJECT_ZOOM(81, getString("ControlType.object_zoom"), ControlTypeGroup.OBJECT),
	OBJECT_CONTAINER(82, getString("ControlType.object_container"), ControlTypeGroup.OBJECT),
	OBJECT_CONT_ANIM(83, getString("ControlType.object_cont_anim"), ControlTypeGroup.OBJECT),
	
	MAP(100, getString("ControlType.map"), ControlTypeGroup.MAP),
	MAP_MAIN(101, getString("ControlType.map_main"), ControlTypeGroup.MAP);
	//@formatter:on

	public static final ControlType[] BETA_SUPPORTED = {STATIC, CONTROLS_GROUP, BUTTON};

	private final int typeId;
	private final String displayName;
	private final Image icon;
	private final Image customIcon;

	private final boolean deprecated;
	private final ControlTypeGroup group;
	private SVIntegerUnmodifiable sv;

	ControlType(int typeId, String displayName, ControlTypeGroup group) {
		this(typeId, displayName, group, ControlIcons.placeholder, false);
	}

	ControlType(int typeId, String displayName, ControlTypeGroup group, String iconPath) {
		this(typeId, displayName, group, iconPath, false);
	}

	//todo: do not add ArmaControlClasses in here. Have a different enum so that you can create custom controls and specify the same type again (like RscPicture and RscFrame both use type STATIC)
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


	/** Returns the {@link #typeId} inside a {@link SVIntegerUnmodifiable} instance. Only one instance is returned */
	public SVIntegerUnmodifiable toSerializableValue() {
		if (sv == null) {
			sv = new SVIntegerUnmodifiable(getTypeId());
		}
		return sv;
	}


	/** Return a string formatted as such: 'displayName (typeId)'. {@link #toString()} will return this value */
	public String fullDisplayText() {
		return getDisplayName() + " (" + getTypeId() + ")";
	}

	/** Return the class name for the root control type */
	public String getNameAsClassName() {
		return "ADC_" + getDisplayName().replaceAll("\\s", "_");
	}

	/**
	 Get the control type by finding the matched between {@link #typeId} and the parameter typeId

	 @throws IllegalArgumentException when id couldn't be matched
	 */
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
		for (ControlType type : BETA_SUPPORTED) {
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
