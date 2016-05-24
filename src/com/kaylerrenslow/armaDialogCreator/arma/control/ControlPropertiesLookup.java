package com.kaylerrenslow.armaDialogCreator.arma.control;

import com.kaylerrenslow.armaDialogCreator.arma.control.ControlProperty.PropertyType;
import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.AFont;
import com.kaylerrenslow.armaDialogCreator.arma.util.AHexColor;
import com.kaylerrenslow.armaDialogCreator.arma.util.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 05/22/2016.
 */
public enum ControlPropertiesLookup {
	IDC("idc", PropertyType.INT, "Control id, or -1 if doesn't matter."),
	X("x", PropertyType.FLOAT_RANGE, "X position."),
	Y("y", PropertyType.FLOAT_RANGE, "Y position."),
	W("w", PropertyType.FLOAT_RANGE, "Width of control."),
	H("h", PropertyType.FLOAT_RANGE, "Height of control."),
	TYPE("type", PropertyType.INT, "Type of the control."),
	STYLE("style", PropertyType.INT, "Style of the control."),
	ACCESS("access", PropertyType.INT, "Read and write setting.", new Option("0", "Read and Write (default case where properties can still be added or overridden)."), new Option("1", "Read and Create (only allows creating new properties)."), new Option("2", "Read only (does not allow to do anything in deriving classes)."), new Option("3", "Read only verified (does not allow to do anything either in deriving classes, and a CRC check will be performed).")),

	/*Common*/
	MOVING("moving", PropertyType.BOOLEAN, "Whether or not this control can be dragged."),
	SIZE_EX("sizeEx", PropertyType.FLOAT_RANGE, "Font size of text."),
	FONT("font", PropertyType.FONT, "Font for text."),
	COLOR_TEXT("colorText", PropertyType.COLOR, "Color of text."),
	COLOR_BACKGROUND("colorBackground", PropertyType.COLOR, "Background color of control."),
	TEXT("text", PropertyType.STRING, "Text to show."),
	SHADOW("shadow", PropertyType.INT, "Shadow for control.", new Option("0", "No shadow."), new Option("1", "Drop shadow with soft edges."), new Option("2", "Stroke")), //does absolutely nothing inside the Attributes class for structured text
	TOOLTIP("tooltip", PropertyType.STRING, "Text to display when most hovers over this control."),
	TOOLTIP_COLOR_SHADE("tooltipColorShade", PropertyType.COLOR, "Tooltip background color."),
	TOOLTIP_COLOR_TEXT("tooltipColorText", PropertyType.COLOR, "Tooltip text color."),
	TOOLTIP_COLOR_BOX("tooltipColorBox", PropertyType.COLOR, "Tooltip border color."),
	ALIGN("align", PropertyType.STRING, "Horizontal align of text.", new Option("left", "Left align."), new Option("center", "Center align."), new Option("right", "Right align.")),
	VALIGN("valign", PropertyType.STRING, "Vertical align of text.", new Option("top", "Top align."), new Option("middle", "Middle align."), new Option("bottom", "Bottom align.")),
	COLOR_HEX("color", PropertyType.HEX_COLOR_STRING, "Text color."),
	SHADOW_COLOR("shadowColor", PropertyType.HEX_COLOR_STRING, "Shadow color."), //default shadow color

	/*Static*/
	STATIC_AUTO_PLAY("autoPlay", PropertyType.BOOLEAN, "Autoplay the video (video only)."),
	STATIC_BLINKING_PERIOD("blinkingPeriod", PropertyType.FLOAT, "Makes the text start transparent, go to full opacity and back to full transparent in the amount of time specified."),
	STATIC_KEY("key", PropertyType.STRING, "From the wiki:\"a possibly quite useless xbox value\"."),
	STATIC_LOOPS("loops", PropertyType.INT, "Number of times the video loops."),
	STATIC_LINE_SPACING("lineSpacing", PropertyType.FLOAT, "Line spacing of text and is required if style is MULTI (16)"),
	STATIC_FIXED_WIDTH("fixedWidth", PropertyType.BOOLEAN, null),

	/*mostly Structured Text*/
	STRUCT_TEXT_SIZE("size", PropertyType.FLOAT_RANGE, "Size of text. If 1, size will be value of parent class."),

	/*HTML*/
	HTML_CYCLE_LINKS("cyclelinks", PropertyType.BOOLEAN, null),
	HTML_FILE_NAME("filename", PropertyType.FILE_NAME, "HTML file to load into control at startup."),
	HTML_COLOR_BOLD("colorBold", PropertyType.COLOR, "Text color of inside <b></b> (bold text)."),
	HTML_COLOR_LINK("colorLink", PropertyType.COLOR, "Text color of inside <a href='#section'></a> (link text)."),
	HTML_COLOR_LINK_ACTIVE("colorLinkActive", PropertyType.COLOR, "Text color of an active link."),
	HTML_COLOR_PICTURE("colorPicture", PropertyType.COLOR, "Color of transparent part of image."),
	HTML_COLOR_PICTURE_BORDER("colorPictureBorder", PropertyType.COLOR, "Color of border around image."),
	HTML_COLOR_PICTURE_LINK("colorPictureLink", PropertyType.COLOR, "Color of transparent part of image that is inside link."),
	HTML_COLOR_PICTURE_SELECTED("colorPictureSelected", PropertyType.COLOR, "Color of transparent part of image that is within an active link."),
	HTML_PREV_PAGE("prevPage", PropertyType.IMAGE, "File name of image which is used for left arrow."),
	HTML_NEXT_PAGE("nextPage", PropertyType.IMAGE, "File name of image which is used for right arrow."),

	/*Button*/
	BTN_ACTION("action", PropertyType.STRING, "Script command(s) to execute when button is pressed. Variable 'this' contains unit that pressed button."),
	BTN_BORDER_SIZE("borderSize", PropertyType.FLOAT_RANGE, "If > 0 then a background (in the color defined in 'colorBorder') is drawn behind the button. It extends to the left by the distance defined here, its height is slightly less than that of the button, and it is vertically centered. The width extends to the right, to where the drop shadow starts. Stays static when button is pressed."),
	BTN_COLOR_BACKGROUND_ACTIVE("colorBackgroundActive", PropertyType.COLOR, "Button's background color if 'active' (mouse pointer is over it)."),
	BTN_COLOR_BACKGROUND_DISABLED("colorBackgroundDisabled", PropertyType.COLOR, "Button's background color if disabled."),
	BTN_COLOR_BORDER("colorBorder", PropertyType.COLOR, "Color of left border that is defined in 'borderSize'."),
	BTN_COLOR_DISABLED("colorDisabled", PropertyType.COLOR, "Text color if button is disabled."),
	BTN_COLOR_FOCUSED("colorFocused", PropertyType.COLOR, "Alternating background color. While the control has focus (but without the mouse pointer being over it) the background will cycle between 'colorFocused' and 'colorBackground'. If both are the same, then the color will be steady."),
	BTN_COLOR_SHADOW("colorShadow", PropertyType.COLOR, "Color of drop shadow behind button. This color is not visible when button is disabled."),
	BTN_DEFAULT("default", PropertyType.BOOLEAN, "Whether or not the button will have focus upon loading the dialog."),
	BTN_OFFSET_PRESSED_X("offsetPressedX", PropertyType.FLOAT_RANGE, "The button's text & background will move by this distance when pressed (ff a shadow is defined, it will stay in place)."),
	BTN_OFFSET_PRESSED_Y("offsetPressedY", PropertyType.FLOAT_RANGE, "The button's text & background will move by this distance when pressed (ff a shadow is defined, it will stay in place)."),
	BTN_OFFSET_X("offsetX", PropertyType.FLOAT_RANGE, "Horizontal and vertical offset of drop shadow. if 0, then shadow will be placed directly behind button."),
	BTN_OFFSET_Y("offsetY", PropertyType.FLOAT_RANGE, "Horizontal and vertical offset of drop shadow. if 0, then shadow will be placed directly behind button."),
	BTN_SOUND_CLICK("soundClick", PropertyType.SOUND, "Sound to play when mouse button is released."),
	BTN_SOUND_ENTER("soundEnter", PropertyType.SOUND, "Sound to play when mouse cursor is moved over the button."),
	BTN_SOUND_ESCAPE("soundEscape", PropertyType.SOUND, "Sound to play when the button was clicked via the mouse, and then released outside the button area."),
	BTN_SOUND_PUSH("soundPush", PropertyType.SOUND, "Sound to play when mouse button is clicked."),
	//a note on toolTip: can be tooltip
	/*..Shortcut Button*/
	BTN_ANIM_TEXTURE_NORMAL("animTextureNormal", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_DISABLED("animTextureDisabled", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_OVER("animTextureOver", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_FOCUSED("animTextureFocused", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_PRESSED("animTexturePressed", PropertyType.TEXTURE, null),
	BTN_ANIM_TEXTURE_DEFAULT("animTextureDefault", PropertyType.TEXTURE, null),
	BTN_TEXTURE_NO_SHORTCUT("textureNoShortcut", PropertyType.TEXTURE, null),
	BTN_COLOR2("color2", PropertyType.COLOR, null),
	BTN_COLOR_BACKGROUND2("colorBackground2", PropertyType.COLOR, null),
	BTN_PERIOD("period", PropertyType.FLOAT, null),
	BTN_PERIOD_FOCUS("periodFocus", PropertyType.FLOAT, null),
	BTN_PERIOD_OVER("periodOver", PropertyType.FLOAT, null),

	/*Active Text*/
	


	/*HitZone and TextPos classes*/
	TOP("top", PropertyType.FLOAT, null),
	RIGHT("right", PropertyType.FLOAT, null),
	BOTTOM("bottom", PropertyType.FLOAT, null),
	LEFT("left", PropertyType.FLOAT, null),

	/*ShortcutPos class*/
	CLASS_SHORTCUT_POS__W("w", PropertyType.FLOAT, null),
	CLASS_SHORTCUT_POS__H("h", PropertyType.FLOAT, null);


	/** All values that the property can be, or null if user defined. */
	public final @Nullable Option[]options;
	public final String propertyName;
	public final PropertyType propertyType;
	public final String about;

	ControlPropertiesLookup(@NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable String about) {
		this(propertyName, propertyType, about, (Option[]) null);
	}

	ControlPropertiesLookup(@NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable String about, @Nullable Option... options) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.about = (about != null ? about : "No documentation.");
		this.options = options;
	}

	public boolean matches(ControlProperty property){
		return propertyName.equals(property.getName()) && propertyType == property.getType();
	}

	public ControlProperty getIntProperty(int defaultValue) {
		return new ControlProperty(propertyName, defaultValue);
	}

	public ControlProperty getFloatProperty(double defaultValue) {
		return new ControlProperty(propertyName, defaultValue);
	}

	public ControlProperty getFloatRangeProperty(double defaultValue) {
		return new ControlProperty(propertyName, PropertyType.FLOAT_RANGE, defaultValue);
	}

	public ControlProperty getBooleanProperty(boolean defaultValue) {
		return new ControlProperty(propertyName, defaultValue);
	}

	public ControlProperty getStringProperty(String defaultValue) {
		return new ControlProperty(propertyName, PropertyType.STRING, defaultValue);
	}

	public ControlProperty getArrayProperty(String[] defaultValue) {
		return new ControlProperty(propertyName, PropertyType.ARRAY, defaultValue);
	}

	public ControlProperty getColorProperty(AColor defaultValue) {
		return new ControlProperty(propertyName, defaultValue);
	}

	public ControlProperty getFontProperty(AFont defaultValue) {
		return new ControlProperty(propertyName, defaultValue);
	}

	public ControlProperty getHexColorProperty(AHexColor defaultValue) {
		return new ControlProperty(propertyName, defaultValue);
	}

	public ControlProperty getProperty(Object defaultValue) {
		return new ControlProperty(propertyName, propertyType, defaultValue);
	}

}
