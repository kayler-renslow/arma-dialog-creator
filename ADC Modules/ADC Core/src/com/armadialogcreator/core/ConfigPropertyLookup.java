package com.armadialogcreator.core;

import com.armadialogcreator.util.ReadOnlyArray;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 A place to find ALL known control properties for all controls.
 This is where the name of the property, property type, description, and options (if allowed) are listed.
 <p>
 DO NOT MODIFY THE ID'S.*****

 @author Kayler
 @since 05/22/2016. */
public enum ConfigPropertyLookup implements ConfigPropertyLookupConstant {
	IDC(0, 0, false, "idc", PropertyType.Int),
	X(1, 1, false, "x", PropertyType.Float),
	Y(2, 2, false, "y", PropertyType.Float),
	W(3, 3, false, "w", PropertyType.Float),
	H(4, 4, false, "h", PropertyType.Float),
	TYPE(5, -1, false, "type", PropertyType.Int),
	STYLE(6, 5, false, "style", PropertyType.ControlStyle),
	ACCESS(7, false, "access", PropertyType.Int,
			new ConfigPropertyValueOption("Read and Write", "0", "Default case where properties can still be added or overridden."),
			new ConfigPropertyValueOption("Read and Create", "1", "Only allows creating new properties."),
			new ConfigPropertyValueOption("Read Only", "2", "Does not allow to do anything in deriving classes."),
			new ConfigPropertyValueOption("Read Only Verified", "3", "Does not allow to do anything either in deriving classes, and a CRC check will be performed.")
	),

	/*Common*/
	MOVING(8, false, "moving", PropertyType.Boolean),
	SIZE_EX(9, false, "sizeEx", PropertyType.Float),
	FONT(10, false, "font", PropertyType.Font),
	COLOR_TEXT(11, false, "colorText", PropertyType.Color),
	COLOR_BACKGROUND(12, false, "colorBackground", PropertyType.Color),
	TEXT(13, 6, false, "text", PropertyType.String),
	SHADOW(14, false, "shadow", PropertyType.Int,  //does absolutely nothing inside the Attributes class for structured text
			new ConfigPropertyValueOption("None (0)", "0", "No shadow."),
			new ConfigPropertyValueOption("Drop Shadow (1)", "1", "Drop shadow with soft edges."),
			new ConfigPropertyValueOption("Stroke (2)", "2", "Stroke")
	),
	TOOLTIP(15, false, "tooltip", PropertyType.String),
	TOOLTIP_COLOR_SHADE(16, false, "tooltipColorShade", PropertyType.Color),
	TOOLTIP_COLOR_TEXT(17, false, "tooltipColorText", PropertyType.Color),
	TOOLTIP_COLOR_BOX(18, false, "tooltipColorBox", PropertyType.Color),
	ALIGN(19, false, "align", PropertyType.String,
			new ConfigPropertyValueOption("Left", "left", "Left align."),
			new ConfigPropertyValueOption("Center", "center", "Center align."),
			new ConfigPropertyValueOption("Right", "right", "Right align.")
	),
	VALIGN(20, false, "valign", PropertyType.String,
			new ConfigPropertyValueOption("Top", "top", "Top align."),
			new ConfigPropertyValueOption("Middle", "middle", "Middle align."),
			new ConfigPropertyValueOption("Bottom", "bottom", "Bottom align.")
	),
	COLOR__HEX(21, false, "color", PropertyType.HexColorString),
	SHADOW_COLOR(22, false, "shadowColor", PropertyType.HexColorString),
	BLINKING_PERIOD(23, false, "blinkingPeriod", PropertyType.Float),

	AUTO_PLAY(24, false, "autoPlay", PropertyType.Boolean),
	KEY(25, false, "key", PropertyType.String),
	LOOPS(26, false, "loops", PropertyType.Int),
	LINE_SPACING(27, false, "lineSpacing", PropertyType.Float),
	FIXED_WIDTH(28, false, "fixedWidth", PropertyType.Boolean),
	SIZE(29, false, "size", PropertyType.Float),
	CYCLE_LINKS(30, false, "cycleLinks", PropertyType.Boolean),
	FILE_NAME(31, false, "filename", PropertyType.FileName),
	COLOR_BOLD(32, false, "colorBold", PropertyType.Color),
	COLOR_LINK(33, false, "colorLink", PropertyType.Color),
	COLOR_LINK_ACTIVE(34, false, "colorLinkActive", PropertyType.Color),
	COLOR_PICTURE(35, false, "colorPicture", PropertyType.Color),
	COLOR_PICTURE_BORDER(36, false, "colorPictureBorder", PropertyType.Color),
	COLOR_PICTURE_LINK(37, false, "colorPictureLink", PropertyType.Color),
	COLOR_PICTURE_SELECTED(38, false, "colorPictureSelected", PropertyType.Color),
	PREV_PAGE(39, false, "prevPage", PropertyType.Image),
	NEXT_PAGE(40, false, "nextPage", PropertyType.Image),
	ACTION(41, false, "action", PropertyType.SQF),
	BORDER_SIZE(42, false, "borderSize", PropertyType.Float),
	COLOR_BACKGROUND_ACTIVE(43, false, "colorBackgroundActive", PropertyType.Color),
	COLOR_BACKGROUND_DISABLED(44, false, "colorBackgroundDisabled", PropertyType.Color),
	COLOR_BORDER(45, false, "colorBorder", PropertyType.Color),
	COLOR_DISABLED(46, false, "colorDisabled", PropertyType.Color),
	COLOR_FOCUSED(47, false, "colorFocused", PropertyType.Color),
	COLOR_SHADOW(48, false, "colorShadow", PropertyType.Color),
	DEFAULT(49, false, "default", PropertyType.Boolean),
	OFFSET_PRESSED_X(50, false, "offsetPressedX", PropertyType.Float),
	OFFSET_PRESSED_Y(51, false, "offsetPressedY", PropertyType.Float),
	OFFSET_X(52, false, "offsetX", PropertyType.Float),
	OFFSET_Y(53, false, "offsetY", PropertyType.Float),
	SOUND_CLICK(54, false, "soundClick", PropertyType.Sound),
	SOUND_ENTER(55, false, "soundEnter", PropertyType.Sound),
	SOUND_ESCAPE(56, false, "soundEscape", PropertyType.Sound),
	SOUND_PUSH(57, false, "soundPush", PropertyType.Sound),
	TILE_W(58, false, "tileW", PropertyType.Int),
	TILE_H(59, false, "tileH", PropertyType.Int),
	COLOR_FOCUSED2(60, false, "colorFocused2", PropertyType.Color),
	ANIM_TEXTURE_NORMAL(61, false, "animTextureNormal", PropertyType.String),
	ANIM_TEXTURE_DISABLED(62, false, "animTextureDisabled", PropertyType.String),
	ANIM_TEXTURE_OVER(63, false, "animTextureOver", PropertyType.String),
	ANIM_TEXTURE_PRESSED(64, false, "animTexturePressed", PropertyType.String),
	ANIM_TEXTURE_FOCUSED(65, false, "animTextureFocused", PropertyType.String),
	ANIM_TEXTURE_DEFAULT(66, false, "animTextureDefault", PropertyType.String),
	TEXTURE_NO_SHORTCUT(67, false, "textureNoShortcut", PropertyType.String),
	COLOR(68, false, "color", PropertyType.Color),
	COLOR2(69, false, "color2", PropertyType.Color),
	COLOR_BACKGROUND_FOCUSED(70, false, "colorBackgroundFocused", PropertyType.Color),
	COLOR_BACKGROUND2(71, false, "colorBackground2", PropertyType.Color),
	LEFT(72, false, "left", PropertyType.Float),
	TOP(73, false, "top", PropertyType.Float),
	RIGHT(74, false, "right", PropertyType.Float),
	BOTTOM(75, false, "bottom", PropertyType.Float),
	PERIOD_FOCUS(76, false, "periodFocus", PropertyType.Float),
	PERIOD_OVER(77, false, "periodOver", PropertyType.Float),
	SHORTCUTS(78, false, "shortcuts", PropertyType.Array),
	COLOR_SELECTION(79, false, "colorSelection", PropertyType.Color),
	AUTO_COMPLETE(80, false, "autocomplete", PropertyType.String,
			new ConfigPropertyValueOption("None", "\"\"", "No autocomplete."),
			new ConfigPropertyValueOption("Scripting", "\"scripting\"", "SQF autocomplete.")
	),
	HTML_CONTROL(81, false, "htmlControl", PropertyType.Boolean),
	COLOR_ACTIVE(82, false, "colorActive", PropertyType.Color),
	ARROW_EMPTY(83, false, "arrowEmpty", PropertyType.String),
	ARROW_FULL(84, false, "arrowFull", PropertyType.String),
	BORDER(85, false, "border", PropertyType.String),
	THUMB(86, false, "thumb", PropertyType.String),
	COLOR_SELECT(87, false, "colorSelect", PropertyType.Color),
	SOUND_SELECT(88, false, "soundSelect", PropertyType.Sound),
	SOUND_EXPAND(89, false, "soundExpand", PropertyType.Sound),
	SOUND_COLLAPSE(90, false, "soundCollapse", PropertyType.Sound),
	COLOR_SELECT_BACKGROUND(91, false, "colorSelectBackground", PropertyType.Color),
	WHOLE_HEIGHT(92, false, "wholeHeight", PropertyType.Float),
	MAX_HISTORY_DELAY(93, false, "maxHistoryDelay", PropertyType.Float),
	CAN_MODIFY(94, false, "canModify", PropertyType.Boolean),
	MAX_CHARS(95, false, "maxChars", PropertyType.Int),
	FORCE_DRAW_CARET(96, false, "forceDrawCaret", PropertyType.Boolean),
	COLOR_SELECT_BACKGROUND2(97, false, "colorSelectBackground2", PropertyType.Color),
	COLOR_SELECT2(98, false, "colorSelect2", PropertyType.Color),
	ROW_HEIGHT(99, false, "rowHeight", PropertyType.Float),
	PERIOD(100, false, "period", PropertyType.Float),
	COLOR_BAR(101, false, "colorBar", PropertyType.Color),
	COLOR_FRAME(102, false, "colorFrame", PropertyType.Color),
	TEXTURE(103, false, "texture", PropertyType.String),

	/*event handlers*/
	EVENT_ON_LOAD(1000, true, "onLoad", PropertyType.SQF),
	EVENT_ON_UNLOAD(1001, true, "onUnload", PropertyType.SQF),
	EVENT_ON_CHILD_DESTROYED(1002, true, "onChildDestroyed", PropertyType.SQF),
	EVENT_ON_MOUSE_ENTER(1003, true, "onMouseEnter", PropertyType.SQF),
	EVENT_ON_MOUSE_EXIT(1004, true, "onMouseExit", PropertyType.SQF),
	EVENT_ON_SET_FOCUS(1005, true, "onSetFocus", PropertyType.SQF),
	EVENT_ON_KILL_FOCUS(1006, true, "onKillFocus", PropertyType.SQF),
	EVENT_ON_TIMER(1007, true, "onTimer", PropertyType.SQF),
	EVENT_ON_KEY_DOWN(1008, true, "onKeyDown", PropertyType.SQF),
	EVENT_ON_KEY_UP(1009, true, "onKeyUp", PropertyType.SQF),
	EVENT_ON_CHAR(1010, true, "onChar", PropertyType.SQF),
	EVENT_ON_IME_CHAR(1011, true, "onIMEChar", PropertyType.SQF),
	EVENT_ON_IME_COMPOSITION(1012, true, "onIMEComposition", PropertyType.SQF),
	EVENT_ON_JOYSTICK_BUTTON(1013, true, "onJoystickButton", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_DOWN(1014, true, "onMouseButtonDown", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_UP(1015, true, "onMouseButtonUp", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_CLICK(1016, true, "onMouseButtonClick", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_DBL_CLICK(1017, true, "onMouseButtonDblClick", PropertyType.SQF),
	EVENT_ON_MOUSE_MOVING(1018, true, "onMouseMoving", PropertyType.SQF),
	EVENT_ON_MOUSE_HOLDING(1019, true, "onMouseHolding", PropertyType.SQF),
	EVENT_ON_MOUSE_ZCHANGED(1020, true, "onMouseZChanged", PropertyType.SQF),
	EVENT_ON_CAN_DESTROY(1021, true, "onCanDestroy", PropertyType.SQF),
	EVENT_ON_DESTROY(1022, true, "onDestroy", PropertyType.SQF),
	EVENT_ON_BUTTON_CLICK(1023, true, "onButtonClick", PropertyType.SQF),
	EVENT_ON_BUTTON_DBL_CLICK(1024, true, "onButtonDblClick", PropertyType.SQF),
	EVENT_ON_BUTTON_DOWN(1025, true, "onButtonDown", PropertyType.SQF),
	EVENT_ON_BUTTON_UP(1026, true, "onButtonUp", PropertyType.SQF),
	EVENT_ON_LB_SEL_CHANGED(1027, true, "onLBSelChanged", PropertyType.SQF),
	EVENT_ON_LB_LIST_SEL_CHANGED(1028, true, "onLBListSelChanged", PropertyType.SQF),
	EVENT_ON_LB_DBL_CLICK(1029, true, "onLBDblClick", PropertyType.SQF),
	EVENT_ON_LB_DRAG(1030, true, "onLBDrag", PropertyType.SQF),
	EVENT_ON_LB_DRAGGING(1031, true, "onLBDragging", PropertyType.SQF),
	EVENT_ON_LB_DROP(1032, true, "onLBDrop", PropertyType.SQF),
	EVENT_ON_TREE_SEL_CHANGED(1033, true, "onTreeSelChanged", PropertyType.SQF),
	EVENT_ON_TREE_LBUTTON_DOWN(1034, true, "onTreeLButtonDown", PropertyType.SQF),
	EVENT_ON_TREE_DBL_CLICK(1035, true, "onTreeDblClick", PropertyType.SQF),
	EVENT_ON_TREE_EXPANDED(1036, true, "onTreeExpanded", PropertyType.SQF),
	EVENT_ON_TREE_COLLAPSED(1037, true, "onTreeCollapsed", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_MOVE(1038, true, "onTreeMouseMove", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_HOLD(1039, true, "onTreeMouseHold", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_EXIT(1040, true, "onTreeMouseExit", PropertyType.SQF),
	EVENT_ON_TOOL_BOX_SEL_CHANGED(1041, true, "onToolBoxSelChanged", PropertyType.SQF),
	EVENT_ON_CHECKED(1042, true, "onChecked", PropertyType.SQF),
	EVENT_ON_CHECKED_CHANGED(1043, true, "onCheckedChanged", PropertyType.SQF),
	EVENT_ON_CHECK_BOXES_SEL_CHANGED(1044, true, "onCheckBoxesSelChanged", PropertyType.SQF),
	EVENT_ON_HTML_LINK(1045, true, "onHTMLLink", PropertyType.SQF),
	EVENT_ON_SLIDER_POS_CHANGED(1046, true, "onSliderPosChanged", PropertyType.SQF),
	EVENT_ON_OBJECT_MOVED(1047, true, "onObjectMoved", PropertyType.SQF),
	EVENT_ON_MENU_SELECTED(1048, true, "onMenuSelected", PropertyType.SQF),
	EVENT_ON_DRAW(1049, true, "onDraw", PropertyType.SQF),
	EVENT_ON_VIDEO_STOPPED(1050, true, "onVideoStopped", PropertyType.SQF);


	public static final ReadOnlyList<ConfigPropertyLookup> EMPTY = new ReadOnlyList<>(new ArrayList<>());

	private final @Nullable ReadOnlyArray<ConfigPropertyValueOption> options;
	private final String propertyName;
	private final PropertyType propertyType;
	private final int propertyId;
	private final int priority;
	private final ConfigPropertyKey key;
	private final boolean isEvent;

	ConfigPropertyLookup(int propertyId, int priority, boolean isEvent, @NotNull String propertyName, @NotNull PropertyType propertyType, @NotNull ConfigPropertyValueOption... options) {
		this.isEvent = isEvent;
		if (PropertiesLookupData.usedIds.contains(propertyId)) {
			int canUse;
			for (int i = 0; true; i++) {
				if (!PropertiesLookupData.usedIds.contains(i)) {
					canUse = i;
					break;
				}
			}
			throw new IllegalStateException("id '" + propertyId + "' is already taken for property enum name:" + name() + ". Here is an unused id for your convenience: " + canUse);
		}
		if (propertyId == -1) {
			throw new IllegalStateException("-1 propertyId is reserved for user-defined properties");
		}
		PropertiesLookupData.usedIds.add(propertyId);
		this.propertyId = propertyId;
		this.propertyName = propertyName;
		this.propertyType = propertyType;
		this.options = options.length == 0 ? ReadOnlyArray.EMPTY : new ReadOnlyArray<>(options);
		this.priority = priority;
		key = new ConfigPropertyKey.Simple(propertyName, priority);
	}

	ConfigPropertyLookup(int propertyId, boolean isEvent, @NotNull String propertyName, @NotNull PropertyType propertyType, @NotNull ConfigPropertyValueOption... options) {
		this(propertyId, 100, isEvent, propertyName, propertyType, options);
	}

	@Override
	public String toString() {
		return propertyName;
	}

	@Nullable
	@Override
	public ReadOnlyArray<ConfigPropertyValueOption> getOptions() {
		return options;
	}

	@NotNull
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	@NotNull
	public ConfigPropertyKey getHashSafeKey() {
		return key;
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return propertyType;
	}

	@Override
	public int getPropertyId() {
		return propertyId;
	}

	@Override
	public int priority() {
		return priority;
	}

	/**
	 Get all {@link ConfigPropertyLookup} instances where {@link ConfigPropertyLookup#getPropertyType()}
	 is equal to find

	 @param find type to find
	 @return list of matched
	 */
	@NotNull
	public static List<ConfigPropertyLookup> getAllOfByType(@NotNull PropertyType find) {
		ArrayList<ConfigPropertyLookup> props = new ArrayList<>(values().length);
		for (ConfigPropertyLookup lookup : values()) {
			if (lookup.propertyType == find) {
				props.add(lookup);
			}
		}
		return props;
	}

	/**
	 Get all {@link ConfigPropertyLookup} instances where {@link ConfigPropertyLookup#getPropertyName()}
	 is equal to find

	 @param find name to find
	 @param caseSensitive true if case sensitivity matters, false if it doesn't
	 @return list of matched
	 */
	@NotNull
	public static List<ConfigPropertyLookup> getAllOfByName(@NotNull String find, boolean caseSensitive) {
		ArrayList<ConfigPropertyLookup> props = new ArrayList<>(values().length);
		for (ConfigPropertyLookup lookup : values()) {
			String propertyName = lookup.getPropertyName();
			if (caseSensitive ? propertyName.equals(find) : propertyName.equalsIgnoreCase(find)) {
				props.add(lookup);
			}
		}
		return props;
	}

	/** @throws IllegalArgumentException when id couldn't be matched */
	@NotNull
	public static ConfigPropertyLookup findById(int id) {
		for (ConfigPropertyLookup lookup : values()) {
			if (lookup.propertyId == id) {
				return lookup;
			}
		}
		throw new IllegalArgumentException("id " + id + " couldn't be matched");
	}

	@Override
	public boolean isEvent() {
		return isEvent;
	}

	private static class PropertiesLookupData {
		static ArrayList<Integer> usedIds = new ArrayList<>();
	}

}
