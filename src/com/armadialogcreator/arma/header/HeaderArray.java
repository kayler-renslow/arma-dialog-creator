package com.armadialogcreator.arma.header;

import com.armadialogcreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	@Override
	@NotNull
	default String getContent() {
		return getAsString(null);
	}

	default boolean equalsArray(@NotNull HeaderArray o) {
		return getItems().equals(o.getItems());
	}

	@Override
	@NotNull
	default String getAsString(@Nullable IndentedStringBuilder indentedBuilder) {
		if (indentedBuilder == null) {
			StringBuilder sb = new StringBuilder(getItems().size() * 10);
			sb.append('{');
			int i = 0;
			for (HeaderArrayItem ai : getItems()) {
				sb.append(ai.getAsString(null));
				if (i != getItems().size() - 1) {
					sb.append(',');
				}
				i++;
			}
			sb.append('}');
			return sb.toString();
		}
		indentedBuilder.append('{');
		int i = 0;
		for (HeaderArrayItem ai : getItems()) {
			indentedBuilder.append(ai.getAsString(indentedBuilder));
			if (i != getItems().size() - 1) {
				indentedBuilder.append(',');
			}
			i++;
		}
		indentedBuilder.append('}');
		return indentedBuilder.toString();
	}

}
