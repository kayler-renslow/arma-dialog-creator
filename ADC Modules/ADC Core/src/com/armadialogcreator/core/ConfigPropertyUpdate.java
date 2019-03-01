package com.armadialogcreator.core;

import com.armadialogcreator.core.sv.SerializableValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 02/12/2019 */
public interface ConfigPropertyUpdate {
	enum Type {
		Value,
		Macro
	}

	@NotNull Type getType();

	class ValueUpdate implements ConfigPropertyUpdate {
		private final SerializableValue oldValue;
		private final SerializableValue newValue;

		public ValueUpdate(@NotNull SerializableValue oldValue, @NotNull SerializableValue newValue) {
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		@NotNull
		public SerializableValue getOldValue() {
			return oldValue;
		}

		@NotNull
		public SerializableValue getNewValue() {
			return newValue;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.Value;
		}
	}

	class MacroUpdate implements ConfigPropertyUpdate {
		private final Macro oldMacro;
		private final Macro newMacro;
		private final SerializableValue valueBeforeMacroUpdate;

		public MacroUpdate(@Nullable Macro oldMacro, @Nullable Macro newMacro, @NotNull SerializableValue valueBeforeMacroUpdate) {
			this.oldMacro = oldMacro;
			this.newMacro = newMacro;
			this.valueBeforeMacroUpdate = valueBeforeMacroUpdate;
		}

		@NotNull
		public SerializableValue getValueBeforeMacroUpdate() {
			return valueBeforeMacroUpdate;
		}

		@Nullable
		public Macro getOldMacro() {
			return oldMacro;
		}

		@Nullable
		public Macro getNewMacro() {
			return newMacro;
		}

		@Override
		@NotNull
		public Type getType() {
			return Type.Macro;
		}
	}
}
