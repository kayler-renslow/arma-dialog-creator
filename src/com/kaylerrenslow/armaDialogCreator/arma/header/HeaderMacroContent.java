package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/20/2017 */
public interface HeaderMacroContent {
	class Raw implements HeaderMacroContent {
		private String value;

		public Raw(@NotNull String value) {
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
			if (o instanceof Raw) {
				Raw other = (Raw) o;
				return this.value.equals(other.value);
			}
			return false;
		}

		@Override
		public String toString() {
			return "Raw{" +
					"value='" + value + '\'' +
					'}';
		}
	}

	class Conditional implements HeaderMacroContent {
		private String cond;
		private final String trueValue;
		private final String elseValue;

		public Conditional(@NotNull String cond, @NotNull String trueValue, @NotNull String elseValue) {
			this.cond = cond;
			this.trueValue = trueValue;
			this.elseValue = elseValue;
		}

		@NotNull
		public String getCond() {
			return cond;
		}

		@NotNull
		public String getTrueValue() {
			return trueValue;
		}

		@NotNull
		public String getElseValue() {
			return elseValue;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof Conditional) {
				Conditional other = (Conditional) o;
				return this.trueValue.equals(other.trueValue) && this.elseValue.equals(other.elseValue);
			}
			return false;
		}

		@Override
		public String toString() {
			return "Conditional{" +
					"cond='" + cond + '\'' +
					", trueValue='" + trueValue + '\'' +
					", elseValue='" + elseValue + '\'' +
					'}';
		}
	}
}
