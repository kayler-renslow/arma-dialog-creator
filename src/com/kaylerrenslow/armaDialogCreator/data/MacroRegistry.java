package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.control.sv.SVNumber;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import org.jetbrains.annotations.Nullable;

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

	/**
	 Get an {@link Value} instance by finding the {@link Macro} instance where it's {@link Macro#getKey()}.equals(identifier). Only Macro's where their value extends {@link SVNumber} are allowed.

	 @return {@link Nullable} instance, or null if no identifier matches a macro where it's value extends {@link SVNumber}
	 */
	public Value.NumVal getMacroValue(String identifier) {
		for (Macro m : macros) {
			if (m.getKey().equals(identifier)) {
				if (m.getValue() instanceof SVNumber) {
					return new Value.NumVal(((SVNumber) m.getValue()).getNumber());
				}
			}
		}
		return null;
	}
}
