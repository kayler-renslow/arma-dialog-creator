package com.armadialogcreator.data;

import com.armadialogcreator.control.Macro;
import com.armadialogcreator.control.MacroRegistry;
import com.armadialogcreator.control.sv.SVNumericValue;
import com.armadialogcreator.expression.Value;
import com.armadialogcreator.util.ReadOnlyList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 Holds all macros for the current {@link Project}.

 @author Kayler
 @since 07/05/2016. */
public class ProjectMacroRegistry implements MacroRegistry {

	private final List<Macro> macros = new ArrayList<>();
	private final ReadOnlyList<Macro> macrosReadOnly = new ReadOnlyList(macros);

	ProjectMacroRegistry() {
	}

	@NotNull
	public ReadOnlyList<Macro> getMacros() {
		return macrosReadOnly;
	}

	/**
	 Adds a macro to the registry.

	 @param m the macro to remove
	 @throws IllegalArgumentException If the macro key ({@link Macro#getKey()}) already exists in registry
	 */
	public void addMacro(@NotNull Macro m) {
		for (Macro macro : macros) {
			if (macro.getKey().equals(m.getKey())) {
				throw new IllegalArgumentException("duplicate key id:" + macro.getKey());
			}
		}
		macros.add(m);
	}

	public void removeMacro(@NotNull Macro m) {
		macros.removeIf(macro -> macro.equals(m));
	}

	/**
	 Get an {@link Value} instance by finding the {@link Macro} instance where it's {@link Macro#getKey()}.equals(identifier).
	 Only Macro's where their value extends {@link SVNumericValue} are allowed.

	 @return {@link Value.NumVal} instance,
	 or null if no identifier matches a macro where it's value extends {@link SVNumericValue}
	 */
	@Nullable
	public Value.NumVal getMacroValue(String identifier) {
		for (Macro m : macros) {
			if (m.getKey().equals(identifier)) {
				if (m.getValue() instanceof SVNumericValue) {
					return new Value.NumVal(((SVNumericValue) m.getValue()).toDouble());
				}
			}
		}
		return null;
	}

	@Override
	@Nullable
	public Macro findMacroByKey(@NotNull String macroKey) {
		for (Macro macro : getMacros()) {
			if (macro.getKey().equals(macroKey)) {
				return macro;
			}
		}
		return null;
	}
}
