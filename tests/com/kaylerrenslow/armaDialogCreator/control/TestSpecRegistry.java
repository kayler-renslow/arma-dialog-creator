package com.kaylerrenslow.armaDialogCreator.control;

import com.kaylerrenslow.armaDialogCreator.control.sv.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 A {@link SpecificationRegistry} for testing purposes

 @author Kayler
 @since 05/30/2017 */
public class TestSpecRegistry implements SpecificationRegistry {
	public static SpecificationRegistry newInstance() {
		return new TestSpecRegistry();
	}

	private final HashMap<String, Macro> macros = new HashMap<>();

	public enum TestMacro {
		StringMacro(Macro.newMacro("STRING_MACRO", new SVString("String Macro"))),
		IntMacro(Macro.newMacro("INT_MACRO", new SVInteger(3))),
		DoubleMacro(Macro.newMacro("DOUBLE_MACRO", new SVDouble(3.14))),
		ColorMacro(Macro.newMacro("COLOR_MACRO", new SVColor(255, 255, 255, 255)));

		private final Macro<?> macro;

		TestMacro(Macro<?> macro) {
			this.macro = macro;
		}
	}


	public TestSpecRegistry() {
		for (TestMacro testMacro : TestMacro.values()) {
			macros.put(testMacro.macro.getKey(), testMacro.macro);
		}
	}

	@Override
	@Nullable
	public Macro findMacroByKey(@NotNull String macroKey) {
		return macros.get(macroKey);
	}

	@Override
	@Nullable
	public ControlClass findControlClassByName(@NotNull String className) {
		return null;
	}

	@Override
	@Nullable
	public SerializableValue getDefaultValue(@NotNull ControlPropertyLookupConstant lookup) {
		return null;
	}

	@Override
	public void prefetchValues(@NotNull List<ControlPropertyLookupConstant> tofetch) {

	}
}
