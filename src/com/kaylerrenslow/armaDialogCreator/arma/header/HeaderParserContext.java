package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 @author Kayler
 @since 03/19/2017 */
class HeaderParserContext extends DataContext {
	private final List<HeaderMacro> macroList = new ArrayList<>();

	private final LinkedList<HeaderClass> classStack = new LinkedList<>();


	@NotNull
	public List<HeaderMacro> getMacros() {
		return macroList;
	}

	@NotNull
	public LinkedList<HeaderClass> getClassStack() {
		return classStack;
	}
}
