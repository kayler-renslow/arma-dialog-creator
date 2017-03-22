package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public interface HeaderArray extends HeaderValue {
	@NotNull List<HeaderArrayItem> getItems();
}
