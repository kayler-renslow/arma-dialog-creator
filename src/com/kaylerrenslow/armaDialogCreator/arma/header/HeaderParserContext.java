package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderParserContext extends DataContext {
	private final HashMap<String, String> macroMap = new HashMap<>();

	public HeaderParserContext() {
	}

	@NotNull
	public HashMap<String, String> getMacroMap() {
		return macroMap;
	}
}
