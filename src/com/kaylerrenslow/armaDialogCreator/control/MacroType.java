package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.main.Lang;
import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 12/28/2016 */
public enum MacroType {
	USER_DEFINED(Lang.ApplicationBundle().getString("Macros.Type.user_defined")),
	SYSTEM(Lang.ApplicationBundle().getString("Macros.Type.system")),
	GLOBAL(Lang.ApplicationBundle().getString("Macros.Type.global")),
	STRING_TABLE(Lang.ApplicationBundle().getString("Macros.Type.string_table"));

	private final String displayText;

	MacroType(String displayText) {
		this.displayText = displayText;
	}

	@NotNull
	public String getDisplayText() {
		return displayText;
	}
}
