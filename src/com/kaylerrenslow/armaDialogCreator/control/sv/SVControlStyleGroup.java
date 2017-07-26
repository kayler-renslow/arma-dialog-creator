package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 A {@link SVControlStyleGroup} has a 1 length {@link #getAsStringArray()}.

 @author Kayler
 @since 08/05/2016 */
public class SVControlStyleGroup extends SerializableValue {
	private final ControlStyle[] styles;

	public static final ValueConverter<SVControlStyleGroup> CONVERTER = new ValueConverter<SVControlStyleGroup>() {
		@Override
		public SVControlStyleGroup convert(DataContext context, @NotNull String... values) throws Exception {
			String val = values[0];
			boolean asId = false;
			if (val.startsWith("ID:")) {
				// This is most useful with default value providers since they have no idea what the styles are being loaded for.
				// By loading by style id, the matched styles will match perfectly. If not by id, the first style value will be matched,
				// which there can be many styles with the same style value.
				val = val.substring("ID:".length());
				asId = true;
			}
			String[] split = val.split("\\+");
			for (int i = 0; i < split.length; i++) {
				split[i] = split[i].trim();
			}
			ArrayList<ControlStyle> styles = new ArrayList<>(split.length);
			for (String s : split) {
				int num;
				try {
					num = Integer.parseInt(s);
				} catch (IllegalArgumentException ignore) { //will catch number format exception
					continue;
				}
				try {
					if (asId) {
						styles.add(ControlStyle.findById(num));
					} else {
						styles.add(ControlStyle.findByValue(num));
					}
				} catch (IllegalArgumentException ignore) { //will catch number format exception
					ignore.printStackTrace(System.out);
				}
			}
			if (styles.isEmpty()) {
				return null;
			}
			return new SVControlStyleGroup(styles);
		}
	};

	/**
	 Create a new style group.

	 @param styles all styles
	 @throws IllegalArgumentException if styles.length is 0
	 */
	public SVControlStyleGroup(@NotNull ControlStyle[] styles) {
		if (styles.length == 0) {
			throw new IllegalArgumentException("styles.length is 0");
		}
		this.styles = styles;
	}

	/**
	 Create a new style group.

	 @param styles all styles
	 @throws IllegalArgumentException if styles.size() is 0
	 */
	public SVControlStyleGroup(@NotNull List<ControlStyle> styles) {
		if (styles.size() == 0) {
			throw new IllegalArgumentException("styles.size() is 0");
		}
		this.styles = new ControlStyle[styles.size()];
		for (int i = 0; i < styles.size(); i++) {
			this.styles[i] = styles.get(i);
		}
	}

	@NotNull
	public ControlStyle[] getStyleArray() {
		return styles;
	}

	@NotNull
	@Override
	public String[] getAsStringArray() {
		return new String[]{toString()};
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		ControlStyle[] copy = new ControlStyle[styles.length];
		System.arraycopy(styles, 0, copy, 0, copy.length);
		return new SVControlStyleGroup(copy);
	}

	@NotNull
	@Override
	public PropertyType getPropertyType() {
		return PropertyType.ControlStyle;
	}

	public static String toString(@NotNull ControlStyle[] values) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			s.append(values[i].styleValue).append(i != values.length - 1 ? "+" : "");
		}
		return s.toString();
	}


	@Override
	public String toString() {
		return toString(styles);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVControlStyleGroup) {
			SVControlStyleGroup other = (SVControlStyleGroup) o;
			return Arrays.equals(this.styles, other.styles);
		}
		return false;
	}

	/**
	 @param s style to search for
	 @return true if this group has the provided {@link ControlStyle}, false if doesn't have it
	 */
	public boolean hasStyle(@NotNull ControlStyle s) {
		for (ControlStyle style : styles) {
			if (style == s) {
				return true;
			}
		}
		return false;
	}
}
