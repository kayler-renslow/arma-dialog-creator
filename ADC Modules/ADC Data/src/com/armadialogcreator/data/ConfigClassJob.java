package com.armadialogcreator.data;

import com.armadialogcreator.core.ConfigClass;
import com.armadialogcreator.core.Macro;
import org.jetbrains.annotations.NotNull;

/**
 @author kayler
 @since 4/16/19 */
public interface ConfigClassJob {
	void doWork();

	class ExtendConfigClassJob implements ConfigClassJob {

		private final ConfigClass cc;
		private final String extendClass;

		public ExtendConfigClassJob(@NotNull ConfigClass cc, @NotNull String extendClass) {
			this.cc = cc;
			this.extendClass = extendClass;
		}

		@Override
		public void doWork() {
			ConfigClass extend = ConfigClassRegistry.instance.findConfigClassByName(extendClass);
			if (extend == null) {
				throw new IllegalStateException();
			}
			cc.extendConfigClass(extend);
		}
	}

	class SetMacroJob implements ConfigClassJob {

		private final ConfigClass cc;
		private final String property;
		private final String macro;

		public SetMacroJob(@NotNull ConfigClass cc, @NotNull String property, @NotNull String macro) {
			this.cc = cc;
			this.property = property;
			this.macro = macro;
		}

		@Override
		public void doWork() {
			Macro macro = MacroRegistry.instance.findMacroByName(this.macro);
			if (macro == null) {
				throw new IllegalStateException();
			}
			cc.findProperty(property).bindToMacro(macro);
		}
	}

}
