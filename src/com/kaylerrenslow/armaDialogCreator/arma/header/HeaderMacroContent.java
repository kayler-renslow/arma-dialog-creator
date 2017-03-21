package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/20/2017 */
public interface HeaderMacroContent {
	class StringContent implements HeaderMacroContent {
		private String value;

		public StringContent(@NotNull String value) {
			this.value = value;
		}

		@NotNull
		public String getValue() {
			return value;
		}
	}

	class Conditional implements HeaderMacroContent {
		private final String trueValue;
		private final String elseValue;

		public Conditional(@NotNull String trueValue, @NotNull String elseValue) {
			this.trueValue = trueValue;
			this.elseValue = elseValue;
		}

		@NotNull
		public String getTrueValue() {
			return trueValue;
		}

		@NotNull
		public String getElseValue() {
			return elseValue;
		}
	}
}
