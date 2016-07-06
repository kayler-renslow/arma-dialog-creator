package com.kaylerrenslow.armaDialogCreator.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 @author Kayler
 Holds all macros for the program.
 Created on 07/05/2016. */
public class MacroRegistry {
	static final MacroRegistry INSTANCE = new MacroRegistry();

	private final ObservableList<Macro> macros = FXCollections.observableArrayList(new ArrayList<Macro>());

	private MacroRegistry() {
	}

	public ObservableList<Macro> getMacros() {
		return macros;
	}
}
