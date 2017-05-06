package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderArray extends HeaderValue, HeaderArrayItem, HeaderItem {
	@NotNull List<HeaderArrayItem> getItems();

	@Override
	@NotNull
	default HeaderValue getValue() {
		return this;
	}

	@NotNull
	@Override
	default String getContent() {
		return getAsString();
	}

	default boolean equalsArray(@NotNull HeaderArray o) {
		return getItems().equals(o.getItems());
	}

	@Override
	@NotNull
	default String getAsString() {
		StringBuilder sb = new StringBuilder(getItems().size() * 10);
		sb.append('{');
		int i = 0;
		for (HeaderArrayItem ai : getItems()) {
			sb.append(ai.getAsString());
			if (i != getItems().size() - 1) {
				sb.append(',');
			}
			i++;
		}
		sb.append("}");
		return sb.toString();
	}

}
