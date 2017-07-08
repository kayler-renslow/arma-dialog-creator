package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 A place to find ALL known control properties for all controls.
 This is where the name of the property, property type, description, and options (if allowed) are listed.

 *****DO NOT MODIFY THE ID'S.*****

 @author Kayler
 @since 05/22/2016. */
public enum ControlPropertyLookup implements ControlPropertyLookupConstant {
	IDC(0, 0, "idc", PropertyType.Int),
	X(1, 1, "x", PropertyType.Float),
	Y(2, 2, "y", PropertyType.Float),
	W(3, 3, "w", PropertyType.Float),
	H(4, 4, "h", PropertyType.Float),
	TYPE(5, -1, "type", PropertyType.Int),
	STYLE(6, 5, "style", PropertyType.ControlStyle),
	ACCESS(7, "access", PropertyType.Int, new ControlPropertyOption("Read and Write", "0", "Default case where properties can still be added or overridden."), new ControlPropertyOption("Read and Create", "1", "Only allows creating new properties."), new ControlPropertyOption("Read Only", "2", "Does not allow to do anything in deriving classes."), new ControlPropertyOption("Read Only Verified", "3", "Does not allow to do anything either in deriving classes, and a CRC check will be performed.")),

	/*Common*/
	MOVING(8, "moving", PropertyType.Boolean),
	SIZE_EX(9, "sizeEx", PropertyType.Float),
	FONT(10, "font", PropertyType.Font),
	COLOR_TEXT(11, "colorText", PropertyType.Color),
	COLOR_BACKGROUND(12, "colorBackground", PropertyType.Color),
	TEXT(13, 6, "text", PropertyType.String),
	SHADOW(14, "shadow", PropertyType.Int,  //does absolutely nothing inside the Attributes class for structured text
			new ControlPropertyOption("None (0)", "0", "No shadow."),
			new ControlPropertyOption("Drop Shadow (1)", "1", "Drop shadow with soft edges."),
			new ControlPropertyOption("Stroke (2)", "2", "Stroke")
	),
	TOOLTIP(15, "tooltip", PropertyType.String),
	TOOLTIP_COLOR_SHADE(16, "tooltipColorShade", PropertyType.Color),
	TOOLTIP_COLOR_TEXT(17, "tooltipColorText", PropertyType.Color),
	TOOLTIP_COLOR_BOX(18, "tooltipColorBox", PropertyType.Color),
	ALIGN(19, "align", PropertyType.String, new ControlPropertyOption("Left", "left", "Left align."), new ControlPropertyOption("Center", "center", "Center align."), new ControlPropertyOption("Right", "right", "Right align.")),
	VALIGN(20, "valign", PropertyType.String, new ControlPropertyOption("Top", "top", "Top align."), new ControlPropertyOption("Middle", "middle", "Middle align."), new ControlPropertyOption("Bottom", "bottom", "Bottom align.")),
	COLOR__HEX(21, "color", PropertyType.HexColorString),
	SHADOW_COLOR(22, "shadowColor", PropertyType.HexColorString), //default shadow color
	BLINKING_PERIOD(23, "blinkingPeriod", PropertyType.Float),

	AUTO_PLAY(24, "autoPlay", PropertyType.Boolean),
	KEY(25, "key", PropertyType.String),
	LOOPS(26, "loops", PropertyType.Int),
	LINE_SPACING(27, "lineSpacing", PropertyType.Float),
	FIXED_WIDTH(28, "fixedWidth", PropertyType.Boolean),
	SIZE(29, "size", PropertyType.Float),
	CYCLE_LINKS(30, "cycleLinks", PropertyType.Boolean),
	FILE_NAME(31, "filename", PropertyType.FileName),
	COLOR_BOLD(32, "colorBold", PropertyType.Color),
	COLOR_LINK(33, "colorLink", PropertyType.Color),
	COLOR_LINK_ACTIVE(34, "colorLinkActive", PropertyType.Color),
	COLOR_PICTURE(35, "colorPicture", PropertyType.Color),
	COLOR_PICTURE_BORDER(36, "colorPictureBorder", PropertyType.Color),
	COLOR_PICTURE_LINK(37, "colorPictureLink", PropertyType.Color),
	COLOR_PICTURE_SELECTED(38, "colorPictureSelected", PropertyType.Color),
	PREV_PAGE(39, "prevPage", PropertyType.Image),
	NEXT_PAGE(40, "nextPage", PropertyType.Image),
	ACTION(41, "action", PropertyType.SQF),
	BORDER_SIZE(42, "borderSize", PropertyType.Float),
	COLOR_BACKGROUND_ACTIVE(43, "colorBackgroundActive", PropertyType.Color),
	COLOR_BACKGROUND_DISABLED(44, "colorBackgroundDisabled", PropertyType.Color),
	COLOR_BORDER(45, "colorBorder", PropertyType.Color),
	COLOR_DISABLED(46, "colorDisabled", PropertyType.Color),
	COLOR_FOCUSED(47, "colorFocused", PropertyType.Color),
	COLOR_SHADOW(48, "colorShadow", PropertyType.Color),
	DEFAULT(49, "default", PropertyType.Boolean),
	OFFSET_PRESSED_X(50, "offsetPressedX", PropertyType.Float),
	OFFSET_PRESSED_Y(51, "offsetPressedY", PropertyType.Float),
	OFFSET_X(52, "offsetX", PropertyType.Float),
	OFFSET_Y(53, "offsetY", PropertyType.Float),
	SOUND_CLICK(54, "soundClick", PropertyType.Sound),
	SOUND_ENTER(55, "soundEnter", PropertyType.Sound),
	SOUND_ESCAPE(56, "soundEscape", PropertyType.Sound),
	SOUND_PUSH(57, "soundPush", PropertyType.Sound),
	TILE_W(58, "tileW", PropertyType.Int),
	TILE_H(59, "tileH", PropertyType.Int),
	COLOR_FOCUSED2(60, "colorFocused2", PropertyType.Color),
	ANIM_TEXTURE_NORMAL(61, "animTextureNormal", PropertyType.Texture),
	ANIM_TEXTURE_DISABLED(62, "animTextureDisabled", PropertyType.Texture),
	ANIM_TEXTURE_OVER(63, "animTextureOver", PropertyType.Texture),
	ANIM_TEXTURE_PRESSED(64, "animTexturePressed", PropertyType.Texture),
	ANIM_TEXTURE_FOCUSED(65, "animTextureFocused", PropertyType.Texture),
	ANIM_TEXTURE_DEFAULT(66, "animTextureDefault", PropertyType.Texture),
	TEXTURE_NO_SHORTCUT(67, "textureNoShortcut", PropertyType.Texture),
	COLOR(68, "color", PropertyType.Color),
	COLOR2(69, "color2", PropertyType.Color),
	COLOR_BACKGROUND_FOCUSED(70, "colorBackgroundFocused", PropertyType.Color),
	COLOR_BACKGROUND2(71, "colorBackground2", PropertyType.Color),
	LEFT(72, "left", PropertyType.Float),
	TOP(73, "top", PropertyType.Float),
	RIGHT(74, "right", PropertyType.Float),
	BOTTOM(75, "bottom", PropertyType.Float),
	PERIOD_FOCUS(76, "periodFocus", PropertyType.Float),
	PERIOD_OVER(77, "periodOver", PropertyType.Float),
	SHORTCUTS(78, "shortcuts", PropertyType.Array),

	/*event handlers*/
	EVENT_ON_LOAD(1000, "onLoad", PropertyType.SQF),
	EVENT_ON_UNLOAD(1001, "onUnload", PropertyType.SQF),
	EVENT_ON_CHILD_DESTROYED(1002, "onChildDestroyed", PropertyType.SQF),
	EVENT_ON_MOUSE_ENTER(1003, "onMouseEnter", PropertyType.SQF),
	EVENT_ON_MOUSE_EXIT(1004, "onMouseExit", PropertyType.SQF),
	EVENT_ON_SET_FOCUS(1005, "onSetFocus", PropertyType.SQF),
	EVENT_ON_KILL_FOCUS(1006, "onKillFocus", PropertyType.SQF),
	EVENT_ON_TIMER(1007, "onTimer", PropertyType.SQF),
	EVENT_ON_KEY_DOWN(1008, "onKeyDown", PropertyType.SQF),
	EVENT_ON_KEY_UP(1009, "onKeyUp", PropertyType.SQF),
	EVENT_ON_CHAR(1010, "onChar", PropertyType.SQF),
	EVENT_ON_IME_CHAR(1011, "onIMEChar", PropertyType.SQF),
	EVENT_ON_IME_COMPOSITION(1012, "onIMEComposition", PropertyType.SQF),
	EVENT_ON_JOYSTICK_BUTTON(1013, "onJoystickButton", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_DOWN(1014, "onMouseButtonDown", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_UP(1015, "onMouseButtonUp", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_CLICK(1016, "onMouseButtonClick", PropertyType.SQF),
	EVENT_ON_MOUSE_BUTTON_DBL_CLICK(1017, "onMouseButtonDblClick", PropertyType.SQF),
	EVENT_ON_MOUSE_MOVING(1018, "onMouseMoving", PropertyType.SQF),
	EVENT_ON_MOUSE_HOLDING(1019, "onMouseHolding", PropertyType.SQF),
	EVENT_ON_MOUSE_ZCHANGED(1020, "onMouseZChanged", PropertyType.SQF),
	EVENT_ON_CAN_DESTROY(1021, "onCanDestroy", PropertyType.SQF),
	EVENT_ON_DESTROY(1022, "onDestroy", PropertyType.SQF),
	EVENT_ON_BUTTON_CLICK(1023, "onButtonClick", PropertyType.SQF),
	EVENT_ON_BUTTON_DBL_CLICK(1024, "onButtonDblClick", PropertyType.SQF),
	EVENT_ON_BUTTON_DOWN(1025, "onButtonDown", PropertyType.SQF),
	EVENT_ON_BUTTON_UP(1026, "onButtonUp", PropertyType.SQF),
	EVENT_ON_LB_SEL_CHANGED(1027, "onLBSelChanged", PropertyType.SQF),
	EVENT_ON_LB_LIST_SEL_CHANGED(1028, "onLBListSelChanged", PropertyType.SQF),
	EVENT_ON_LB_DBL_CLICK(1029, "onLBDblClick", PropertyType.SQF),
	EVENT_ON_LB_DRAG(1030, "onLBDrag", PropertyType.SQF),
	EVENT_ON_LB_DRAGGING(1031, "onLBDragging", PropertyType.SQF),
	EVENT_ON_LB_DROP(1032, "onLBDrop", PropertyType.SQF),
	EVENT_ON_TREE_SEL_CHANGED(1033, "onTreeSelChanged", PropertyType.SQF),
	EVENT_ON_TREE_LBUTTON_DOWN(1034, "onTreeLButtonDown", PropertyType.SQF),
	EVENT_ON_TREE_DBL_CLICK(1035, "onTreeDblClick", PropertyType.SQF),
	EVENT_ON_TREE_EXPANDED(1036, "onTreeExpanded", PropertyType.SQF),
	EVENT_ON_TREE_COLLAPSED(1037, "onTreeCollapsed", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_MOVE(1038, "onTreeMouseMove", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_HOLD(1039, "onTreeMouseHold", PropertyType.SQF),
	EVENT_ON_TREE_MOUSE_EXIT(1040, "onTreeMouseExit", PropertyType.SQF),
	EVENT_ON_TOOL_BOX_SEL_CHANGED(1041, "onToolBoxSelChanged", PropertyType.SQF),
	EVENT_ON_CHECKED(1042, "onChecked", PropertyType.SQF),
	EVENT_ON_CHECKED_CHANGED(1043, "onCheckedChanged", PropertyType.SQF),
	EVENT_ON_CHECK_BOXES_SEL_CHANGED(1044, "onCheckBoxesSelChanged", PropertyType.SQF),
	EVENT_ON_HTML_LINK(1045, "onHTMLLink", PropertyType.SQF),
	EVENT_ON_SLIDER_POS_CHANGED(1046, "onSliderPosChanged", PropertyType.SQF),
	EVENT_ON_OBJECT_MOVED(1047, "onObjectMoved", PropertyType.SQF),
	EVENT_ON_MENU_SELECTED(1048, "onMenuSelected", PropertyType.SQF),
	EVENT_ON_DRAW(1049, "onDraw", PropertyType.SQF),
	EVENT_ON_VIDEO_STOPPED(1050, "onVideoStopped", PropertyType.SQF);


	public static final ReadOnlyList<ControlPropertyLookup> EMPTY = new ReadOnlyList<>(new ArrayList<>());

	private final @Nullable ControlPropertyOption[] options;
	private final String propertyName;
	private final PropertyType propertyType;
	private final int propertyId;
	private final int priority;

	ControlPropertyLookup(int propertyId, int priority, @NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable ControlPropertyOption... options) {
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
		this.options = options != null && options.length == 0 ? null : options;
		this.priority = priority;
	}

	ControlPropertyLookup(int propertyId, @NotNull String propertyName, @NotNull PropertyType propertyType, @Nullable ControlPropertyOption... options) {
		this(propertyId, Integer.MAX_VALUE, propertyName, propertyType, options);
	}


	@Override
	public String toString() {
		return propertyName;
	}

	@Nullable
	@Override
	public ControlPropertyOption[] getOptions() {
		return options;
	}

	@NotNull
	@Override
	public String getPropertyName() {
		return propertyName;
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
	 Get all {@link ControlPropertyLookup} instances where {@link ControlPropertyLookup#getPropertyType()}
	 is equal to find
	 @param find type to find

	 @return list of matched
	 */
	@NotNull
	public static List<ControlPropertyLookup> getAllOfByType(@NotNull PropertyType find) {
		ArrayList<ControlPropertyLookup> props = new ArrayList<>(values().length);
		for (ControlPropertyLookup lookup : values()) {
			if (lookup.propertyType == find) {
				props.add(lookup);
			}
		}
		return props;
	}

	/**
	 Get all {@link ControlPropertyLookup} instances where {@link ControlPropertyLookup#getPropertyName()}
	 is equal to find

	 @param find name to find
	 @param caseSensitive true if case sensitivity matters, false if it doesn't
	 @return list of matched
	 */
	@NotNull
	public static List<ControlPropertyLookup> getAllOfByName(@NotNull String find, boolean caseSensitive) {
		ArrayList<ControlPropertyLookup> props = new ArrayList<>(values().length);
		for (ControlPropertyLookup lookup : values()) {
			String propertyName = lookup.getPropertyName();
			if (caseSensitive ? propertyName.equals(find) : propertyName.equalsIgnoreCase(find)) {
				props.add(lookup);
			}
		}
		return props;
	}

	/** @throws IllegalArgumentException when id couldn't be matched */
	@NotNull
	public static ControlPropertyLookup findById(int id) {
		for (ControlPropertyLookup lookup : values()) {
			if (lookup.propertyId == id) {
				return lookup;
			}
		}
		throw new IllegalArgumentException("id " + id + " couldn't be matched");
	}

	private static class PropertiesLookupData {
		static ArrayList<Integer> usedIds = new ArrayList<>();
	}

}
