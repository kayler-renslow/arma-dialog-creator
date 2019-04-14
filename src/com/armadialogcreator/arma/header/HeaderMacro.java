package com.armadialogcreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 03/20/2017 */
public class HeaderMacro {
	private final MacroType macroType;
	private final HeaderMacroContent content;

	public enum MacroType {
		Define, Undefine, IfDef, IfNDef
	}

	public HeaderMacro(@NotNull MacroType macroType, @NotNull HeaderMacroContent content) {
		this.macroType = macroType;
		this.content = content;
	}

	@NotNull
	public MacroType getMacroType() {
		return macroType;
	}

	@NotNull
	public HeaderMacroContent getContent() {
		return content;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof HeaderMacro) {
			HeaderMacro other = (HeaderMacro) o;
			return this.macroType == other.macroType && this.content.equals(other.content);
		}
		return false;
	}

	@Override
	public String toString() {
		return "HeaderMacro{" +
				"macroType=" + macroType +
				", content=" + content +
				'}';
	}
}
