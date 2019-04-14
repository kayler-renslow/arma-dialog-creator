package com.armadialogcreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/21/2017 */
public class ConditionalMacroContent implements HeaderMacroContent {
	private String cond;

	public ConditionalMacroContent(@NotNull String cond) {
		this.cond = cond;
	}

	@NotNull
	public String getCond() {
		return cond;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof ConditionalMacroContent) {
			ConditionalMacroContent other = (ConditionalMacroContent) o;
			return this.cond.equals(other.cond);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Conditional{" +
				"cond='" + cond + "\'}";
	}
}
