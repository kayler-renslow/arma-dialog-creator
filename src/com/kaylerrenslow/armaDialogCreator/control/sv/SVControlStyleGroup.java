package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.control.PropertyType;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 A {@link SVControlStyleGroup} has a 1 length {@link #getAsStringArray()}.

 @author Kayler
 @since 08/05/2016 */
public class SVControlStyleGroup extends SerializableValue {
	private final ControlStyle[] styles;

	public static final String DEFAULT_DELIMITER = "+";

	public static final ValueConverter<SVControlStyleGroup> CONVERTER = new ValueConverter<SVControlStyleGroup>() {
		@Override
		public SVControlStyleGroup convert(DataContext context, @NotNull String... values) throws Exception {
			String[] split = values[0].split("\\+");
			ArrayList<ControlStyle> styles = new ArrayList<>(split.length);
			for (String s : split) {
				int num;
				try {
					num = Integer.parseInt(s);
				} catch (IllegalArgumentException ignore) { //will catch number format exception
					continue;
				}
				try {
					styles.add(ControlStyle.findById(num));
				} catch (IllegalArgumentException e) { //will catch number format exception
					try {
						styles.add(ControlStyle.findByValue(num));
					} catch (IllegalArgumentException ignore) {

					}
				}
			}
			return new SVControlStyleGroup(styles.toArray(new ControlStyle[styles.size()]));
		}
	};

	public SVControlStyleGroup(@NotNull ControlStyle[] styles) {
		this.styles = styles;
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
			s.append(values[i].styleValue).append(i != values.length - 1 ? DEFAULT_DELIMITER : "");
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
