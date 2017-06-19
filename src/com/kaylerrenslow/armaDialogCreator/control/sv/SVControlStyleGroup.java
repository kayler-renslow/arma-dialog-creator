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
 @since 08/05/2016
 */
public class SVControlStyleGroup extends SerializableValue {
	private ControlStyle[] values;

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

	public SVControlStyleGroup(@NotNull String[] values) {
		super(values);
		try {
			this.values = CONVERTER.convert(null, values).values;
		} catch (Exception e) {
			throw new RuntimeException("conversion failed when it shouldn't have");
		}
	}

	public SVControlStyleGroup(@NotNull ControlStyle[] values) {
		super(toString(values));
		this.values = values;
	}

	@NotNull
	public ControlStyle[] getValues() {
		return values;
	}

	public void setValues(@NotNull ControlStyle[] values) {
		this.values = values;
		valuesAsArray[0] = toString(values);
	}

	@NotNull
	@Override
	public SerializableValue deepCopy() {
		ControlStyle[] copy = new ControlStyle[values.length];
		System.arraycopy(values, 0, copy, 0, copy.length);
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
		return toString(values);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof SVControlStyleGroup) {
			SVControlStyleGroup other = (SVControlStyleGroup) o;
			return Arrays.equals(this.values, other.values);
		}
		return false;
	}
}
