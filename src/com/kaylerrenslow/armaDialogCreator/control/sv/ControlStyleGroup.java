package com.kaylerrenslow.armaDialogCreator.control.sv;

import com.kaylerrenslow.armaDialogCreator.control.ControlStyle;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import com.kaylerrenslow.armaDialogCreator.util.ValueConverter;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 08/05/2016.
 */
public class ControlStyleGroup extends SerializableValue {
	private ControlStyle[] values;
	
	public static final String DEFAULT_DELIMITER = "+";
	
	public static final ValueConverter<ControlStyleGroup> CONVERTER = new ValueConverter<ControlStyleGroup>() {
		@Override
		public ControlStyleGroup convert(DataContext context, @NotNull String... values) throws Exception {
			String[] split = values[0].split("\\" + DEFAULT_DELIMITER);
			ControlStyle[] styles = new ControlStyle[split.length];
			int styleInd = 0;
			for (String s : split) {
				try {
					int id = Integer.parseInt(s);
					styles[styleInd++] = ControlStyle.findById(id);
				} catch (IllegalArgumentException e) { //will catch number format exception
					return null;
				}
			}
			return new ControlStyleGroup(styles);
		}
	};
	
	public ControlStyleGroup(@NotNull ControlStyle[] values) {
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
	
	@Override
	public SerializableValue deepCopy() {
		ControlStyle[] copy = new ControlStyle[values.length];
		System.arraycopy(values, 0, copy, 0, copy.length);
		return new ControlStyleGroup(copy);
	}
	
	public static String toString(ControlStyle[] values) {
		String s = "";
		for (int i = 0; i < values.length; i++) {
			s += values[i].styleId + (i != values.length - 1 ? "+" : "");
		}
		return s;
	}
	
	
	@Override
	public String toString() {
		return toString(values);
	}
}
