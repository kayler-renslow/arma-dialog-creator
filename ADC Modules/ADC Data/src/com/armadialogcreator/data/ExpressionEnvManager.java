package com.armadialogcreator.data;

import com.armadialogcreator.application.ApplicationManager;
import com.armadialogcreator.application.ApplicationStateSubscriber;
import com.armadialogcreator.application.Project;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.core.sv.SVNumericValue;
import com.armadialogcreator.expression.Env;
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
		ApplicationManager.getInstance().addStateSubscriber(instance);
	}

	private final MyEnv env = new MyEnv();

	@Override
	public void projectClosed(@NotNull Project project) {
		env.clearEnv();
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

}
