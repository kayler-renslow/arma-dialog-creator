package com.kaylerrenslow.armaDialogCreator.arma.header.impl;

import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderArrayItem;
import com.kaylerrenslow.armaDialogCreator.arma.header.HeaderValue;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderArrayItemImpl implements HeaderArrayItem {

	private HeaderValue value;

	public HeaderArrayItemImpl(@NotNull HeaderValue value) {
		this.value = value;
	}

	@NotNull
	@Override
	public HeaderValue getValue() {
		return value;
	}
}
