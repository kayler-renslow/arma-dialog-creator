package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderArrayAssignment extends HeaderAssignment, HeaderItem {
	@NotNull HeaderArray getArray();

	boolean isConcatenated();

	@Override
	default boolean equalsAssignment(@NotNull HeaderAssignment o) {
		if (o == this) {
			return true;
		}
		if (o instanceof HeaderArrayAssignment) {
			HeaderArrayAssignment obj = ((HeaderArrayAssignment) o);
			return (isConcatenated() == obj.isConcatenated()) && getArray().equalsArray(obj.getArray());
		}
		return false;
	}

	@Override
	@NotNull
	default HeaderValue getValue() {
		return getArray();
	}
}
