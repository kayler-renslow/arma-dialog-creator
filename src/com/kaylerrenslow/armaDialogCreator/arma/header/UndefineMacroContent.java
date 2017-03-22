package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/21/2017 */
public class UndefineMacroContent implements HeaderMacroContent {
	private String value;

	public UndefineMacroContent(@NotNull String value) {
		this.value = value;
	}


	@NotNull
	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof UndefineMacroContent) {
			UndefineMacroContent other = (UndefineMacroContent) o;
			return this.value.equals(other.value);
		}
		return false;
	}

	@Override
	public String toString() {
		return "UndefineMacroContent{" +
				"value='" + value + '\'' +
				'}';
	}
}
