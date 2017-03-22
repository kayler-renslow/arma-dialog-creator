package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
public class HeaderParserContext extends DataContext {
	private final List<HeaderMacro> macroList = new ArrayList<>();

	public HeaderParserContext() {
	}


	@NotNull
	public List<HeaderMacro> getMacros() {
		return macroList;
	}
}
