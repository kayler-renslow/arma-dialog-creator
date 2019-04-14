package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.control.ArmaResolution;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.sv.SVNumericValue;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.expression.NularCommandValueProvider;
import com.armadialogcreator.expression.SimpleEnv;
import com.armadialogcreator.expression.Value;
import com.armadialogcreator.util.ApplicationSingleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author K
 @since 01/07/2019 */
@ApplicationSingleton
public class ExpressionEnvManager implements ApplicationStateSubscriber {
	public static final ExpressionEnvManager instance = new ExpressionEnvManager();

	static {
		ApplicationManager.instance.addStateSubscriber(instance);
	}

	private final MyEnv env = new MyEnv();
	private CommandProvider commandProvider;

	@Override
	public void projectClosed(@NotNull Project project) {
		env.clearEnv();
	}

	@Override
	public void projectDataLoaded(@NotNull Project project) {
		commandProvider = new CommandProvider(EditorManager.instance.getResolution());
		env.setUnaryCommandProvider(commandProvider);
	}

	@NotNull
	public NularCommandValueProvider getCommandProvider() {
		return commandProvider;
	}

	@NotNull
	public Env getEnv() {
		return env;
	}

	private final class MyEnv extends SimpleEnv {

		public void clearEnv() {
			super.map.clear();
		}

		@Override
		@Nullable
		public Value getValue(@NotNull String identifier) {
			Value v = super.getValue(identifier);
			if (v != null) {
				return v;
			}
			for (Macro m : MacroRegistry.instance.iterateAllMacros()) {
				if (m.getKey().equals(identifier)) {
					if (m.getValue() instanceof SVNumericValue) {
						return new Value.NumVal(((SVNumericValue) m.getValue()).toDouble());
					}
				}
			}

			return null;
		}
	}

	public static class CommandProvider implements NularCommandValueProvider {

		private ArmaResolution resolution;

		public CommandProvider(@NotNull ArmaResolution resolution) {
			this.resolution = resolution;
		}

		@Override
		@NotNull
		public Value safeZoneX() {
			return new Value.NumVal(resolution.getSafeZoneX());
		}

		@Override
		@NotNull
		public Value safeZoneY() {
			return new Value.NumVal(resolution.getSafeZoneY());
		}

		@Override
		@NotNull
		public Value safeZoneW() {
			return new Value.NumVal(resolution.getSafeZoneW());
		}

		@Override
		@NotNull
		public Value safeZoneH() {
			return new Value.NumVal(resolution.getSafeZoneH());
		}

		@Override
		@NotNull
		public Value getResolution() {
			return NularCommandValueProvider.helper_getResolution(
					resolution.getScreenWidth(),
					resolution.getScreenHeight(),
					resolution.getViewportWidth(),
					resolution.getViewportHeight(),
					resolution.getAspectRatio(),
					resolution.getUIScaleValue()
			);
		}
	}

}
