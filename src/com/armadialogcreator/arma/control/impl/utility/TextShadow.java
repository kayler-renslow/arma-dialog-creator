package com.armadialogcreator.arma.control.impl.utility;

import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 07/03/2017 */
public enum TextShadow {
	/** Has no shadow */
	None,
	/**
	 Text is rendered twice: once at the original position,
	 but slightly offset with black text and second time is normally
	 at the original position.
	 */
	DropShadow,
	/** The text has a black stroke to it */
	Stroke;

	/**
	 @return the {@link TextShadow} for the given value. If the given value is null, will return
	 {@link TextShadow#None}
	 */
	@NotNull
	public static TextShadow getTextShadow(@Nullable SerializableValue newValue) {
		if (newValue != null) {
			return getTextShadow(newValue.toString());
		}
		return None;
	}

	/**
	 @return the {@link TextShadow} for the given value. If the given value is null, will return
	 {@link TextShadow#None}
	 */
	@NotNull
	public static TextShadow getTextShadow(@Nullable String v) {
		if (v != null) {
			if (v.equals("0")) {
				return None;
			} else if (v.equals("1")) {
				return DropShadow;
			} else if (v.equals("2")) {
				return Stroke;
			}
		}
		return None;
	}
}
