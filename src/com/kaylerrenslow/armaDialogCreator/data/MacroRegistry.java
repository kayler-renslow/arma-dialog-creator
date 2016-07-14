package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.Macro;

import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 Holds all macros for the program.
 Created on 07/05/2016. */
public class MacroRegistry {
	static final MacroRegistry INSTANCE = new MacroRegistry();

	private final List<Macro> macros = new ArrayList<>();

	private MacroRegistry() {
	}

	public List<Macro> getMacros() {
		return macros;
	}
}
