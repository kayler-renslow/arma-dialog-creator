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
		for (HeaderArrayItem ai : getItems()) {
			sb.append(ai.getAsString());
		}
		sb.append("}");
		return sb.toString();
	}

}
