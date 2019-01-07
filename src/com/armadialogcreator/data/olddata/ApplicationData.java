package com.armadialogcreator.data.olddata;

import com.armadialogcreator.ArmaDialogCreator;
import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.arma.util.ArmaUIScale;
import com.armadialogcreator.arma.util.PositionCalculator;
import com.armadialogcreator.canvas.Resolution;
import com.armadialogcreator.canvas.ScreenDimension;
import com.armadialogcreator.core.Macro;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.expression.SimpleEnv;
import com.armadialogcreator.expression.UnaryCommandValueProvider;
import com.armadialogcreator.expression.Value;
import com.armadialogcreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Holds data that aren't specific to the current Project, but rather the entire application itself. Resets after
 {@link ArmaDialogCreator#loadNewProject(boolean)} is invoked.
 <p>
 Also stores an {@link Env} and {@link ArmaResolution} instance for ADC. Use keys {@link Env#ENV} and {@link DataKeys#ARMA_RESOLUTION}
 to get the respected values.

 @author Kayler
 @since 06/07/2016. */
public class ApplicationData extends DataContext {

	private Project currentProject;
	private final Changelog changelog = new Changelog(20);

	protected ApplicationData() {
		put(DataKeys.ARMA_RESOLUTION, new ArmaResolution(ScreenDimension.D960, ArmaUIScale.DEFAULT));
		put(Env.ENV, globalEnv);
	}

	private final SimpleEnv globalEnv = new SimpleEnv(new UnaryCommandValueProviderImpl(this)) {
		@Override
		public @Nullable Value getValue(@NotNull String identifier) {
			Value v = super.getValue(identifier);
			if (v == null) {
				v = getCurrentProject().getMacroRegistry().getMacroValue(identifier);
			}
			return v;
		}
	};

	@NotNull
	public Project getCurrentProject() {
		return currentProject;
	}


	/**
	 Get the {@link Env} instance used for converting identifiers that match any Macro instances key retrieved from {@link Macro#getKey()}. The identifiers can also be
	 {@link PositionCalculator#SAFE_ZONE_X},
	 {@link PositionCalculator#SAFE_ZONE_Y},
	 {@link PositionCalculator#SAFE_ZONE_W},
	 {@link PositionCalculator#SAFE_ZONE_H},
	 {@link PositionCalculator#SAFE_ZONE_X_ABS},
	 {@link PositionCalculator#SAFE_ZONE_W_ABS},
	 */
	@NotNull
	public Env getGlobalExpressionEnvironment() {
		return globalEnv;
	}

	@NotNull
	public Changelog getChangelog() {
		return changelog;
	}

	/**
	 Shortcut for {@link ApplicationDataManager#getApplicationData()}.
	 This method will throw an exception if {@link ApplicationData} isn't initialized for the {@link ApplicationDataManager}.

	 @return data
	 */
	@NotNull
	public static ApplicationData getManagerInstance() {
		return ApplicationDataManager.getInstance().getApplicationData();
	}

	/** Sets {@link #getCurrentProject()} */
	protected void setProject(Project project) {
		this.currentProject = project;
	}

	/**
	 An implementation of {@link UnaryCommandValueProvider} that
	 uses {@link ApplicationData} for certain command values.
	 <p>
	 This class will not mutate {@link ApplicationData}.

	 @since 6/28/2017
	 */
	public static class UnaryCommandValueProviderImpl implements UnaryCommandValueProvider {

		//Note: DO NOT cache any of the values. The values can change without notice!

		private final ApplicationData applicationData;

		public UnaryCommandValueProviderImpl(@NotNull ApplicationData applicationData) {
			this.applicationData = applicationData;
		}

		private ArmaResolution armaResolution() {
			return DataKeys.ARMA_RESOLUTION.get(applicationData);
		}

		@Override
		public @NotNull Value safeZoneX() {
			return new Value.NumVal(armaResolution().getSafeZoneX());
		}

		@Override
		public @NotNull Value safeZoneY() {
			return new Value.NumVal(armaResolution().getSafeZoneY());
		}

		@Override
		public @NotNull Value safeZoneW() {
			return new Value.NumVal(armaResolution().getSafeZoneW());
		}

		@Override
		public @NotNull Value safeZoneH() {
			return new Value.NumVal(armaResolution().getSafeZoneH());
		}

		@Override
		public @NotNull Value getResolution() {
			Resolution resolution = armaResolution();
			Value.NumVal width = new Value.NumVal(resolution.getScreenWidth());
			Value.NumVal height = new Value.NumVal(resolution.getScreenHeight());
			Value.NumVal viewportWidth = new Value.NumVal(resolution.getViewportWidth());
			Value.NumVal viewportHeight = new Value.NumVal(resolution.getViewportHeight());
			Value.NumVal aspectRatio = new Value.NumVal(resolution.getAspectRatio());
			Value.NumVal uiScale = new Value.NumVal(resolution.getUIScale().getValue());

			return _getResolution(
					width,
					height,
					viewportWidth,
					viewportHeight,
					aspectRatio,
					uiScale
			);
		}
	}
}
