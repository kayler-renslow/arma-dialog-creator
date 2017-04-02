package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/31/2017 */
public class BasicHeaderValue implements HeaderValue {
	private final String content;

	public BasicHeaderValue(@NotNull String content) {
		this.content = content;
	}

	@NotNull
	@Override
	public String getContent() {
		return content;
	}
}
