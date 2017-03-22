package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 03/21/2017 */
public class DefineMacroContent implements HeaderMacroContent {
	private final String definedVar;
	private final DefineValue defineValue;

	public DefineMacroContent(@NotNull String definedVar, @NotNull DefineValue defineValue) {
		this.definedVar = definedVar;
		this.defineValue = defineValue;
	}

	@NotNull
	public String getDefinedVar() {
		return definedVar;
	}

	@NotNull
	public DefineValue getDefineValue() {
		return defineValue;
	}

	@Override
	public String toString() {
		return "DefineMacroContent{" +
				"definedVar='" + definedVar + '\'' +
				", defineValue=" + defineValue +
				'}';
	}

	public static class ParameterDefineValue implements DefineValue {

		private final @NotNull String[] params;
		private final String output;

		public ParameterDefineValue(@NotNull String[] params, @NotNull String output) {
			this.params = params;
			this.output = output;
		}

		@Override
		@NotNull
		public String getValue(@NotNull String[] paramVals) {
			String value = output;
			int i = 0;
			for (String param : params) {
				value = value.replaceAll(param, paramVals[i++]);
			}
			return value;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof ParameterDefineValue) {
				ParameterDefineValue other = (ParameterDefineValue) o;
				return Arrays.equals(this.params, other.params) && this.output.equals(other.output);
			}
			return false;
		}

		@Override
		public String toString() {
			return "ParameterDefineValue{" +
					"params=" + Arrays.toString(params) +
					", output='" + output + '\'' +
					'}';
		}
	}

	public static class StringDefineValue implements DefineValue {

		private String v;

		public StringDefineValue(@NotNull String v) {
			this.v = v;
		}

		@Override
		@NotNull
		public String getValue(@NotNull String[] paramVals) {
			return v;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof StringDefineValue) {
				StringDefineValue other = (StringDefineValue) o;
				return this.v.equals(other.v);
			}
			return false;
		}

		@Override
		public String toString() {
			return "StringDefineValue{" +
					"v='" + v + '\'' +
					'}';
		}
	}

	public interface DefineValue {
		@NotNull String getValue(@NotNull String[] paramVals);
	}
}
