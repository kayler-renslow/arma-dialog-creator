package com.armadialogcreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 @author Kayler
 @since 03/21/2017 */
public class DefineMacroContent implements HeaderMacroContent {
	private final String definedVar;
	private final DefineValue definedValue;

	public DefineMacroContent(@NotNull String definedVar, @NotNull DefineValue definedValue) {
		this.definedVar = definedVar;
		this.definedValue = definedValue;
	}

	@NotNull
	public String getDefinedVar() {
		return definedVar;
	}

	@NotNull
	public DefineValue getDefinedValue() {
		return definedValue;
	}

	@Override
	public String toString() {
		return "DefineMacroContent{" +
				"definedVar='" + definedVar + '\'' +
				", definedValue=" + definedValue +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof DefineMacroContent) {
			DefineMacroContent other = (DefineMacroContent) o;
			return this.definedVar.equals(other.definedVar) && this.definedValue.equals(other.definedValue);
		}
		return false;
	}

	public static class ParameterDefineValue implements DefineValue {

		private final @NotNull String[] params;
		private final String text;

		public ParameterDefineValue(@NotNull String[] params, @NotNull String text) {
			this.params = params;
			this.text = text;
		}

		@NotNull
		public String[] getParams() {
			return params;
		}

		@NotNull
		public String getResultTemplateText() {
			return text;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof ParameterDefineValue) {
				ParameterDefineValue other = (ParameterDefineValue) o;
				return Arrays.equals(this.params, other.params) && this.text.equals(other.text);
			}
			return false;
		}

		@Override
		public String toString() {
			return "ParameterDefineValue{" +
					"params=" + Arrays.toString(params) +
					", text='" + text + '\'' +
					'}';
		}
	}

	public static class StringDefineValue implements DefineValue {

		private String text;

		public StringDefineValue(@NotNull String text) {
			this.text = text;
		}

		@Override
		@NotNull
		public String getResultTemplateText() {
			return text;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof StringDefineValue) {
				StringDefineValue other = (StringDefineValue) o;
				return this.text.equals(other.text);
			}
			return false;
		}

		@Override
		public String toString() {
			return "StringDefineValue{" +
					"text='" + text + '\'' +
					'}';
		}
	}

	public interface DefineValue {
		@NotNull String getResultTemplateText();
	}
}
