package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderArrayItem extends HeaderItem {
	@NotNull HeaderValue getValue();

	default boolean arrayItemEquals(@NotNull HeaderArrayItem o) {
		return o == this || this.getValue().equals(o.getValue());
	}

	@Override
	@NotNull
	default String getAsString(@Nullable IndentedStringBuilder sb) {
		return getValue().getContent();
	}
}
