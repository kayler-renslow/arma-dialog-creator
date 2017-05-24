package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaUIScale;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.SimpleEnv;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.gui.uicanvas.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 Holds data that aren't specific to the current Project, but rather the entire application itself. Resets after
 {@link com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator#loadNewProject(boolean)} is invoked.
 <p>
 Also stores an {@link Env} and {@link ArmaResolution} instance for ADC. Use keys {@link DataKeys#ENV} and {@link DataKeys#ARMA_RESOLUTION}
 to get the respected values.

 @author Kayler
 @since 06/07/2016. */
public class ApplicationData extends DataContext {

	private Project currentProject;
	private final Changelog changelog = new Changelog(20);

	protected ApplicationData() {
		put(DataKeys.ARMA_RESOLUTION, new ArmaResolution(ScreenDimension.D960, ArmaUIScale.DEFAULT));
		put(DataKeys.ENV, globalEnv);
	}

	private final SimpleEnv globalEnv = new SimpleEnv() {
		@Override
		public @Nullable Value getValue(@NotNull String identifier) {
			ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(ApplicationData.this);
			if (resolution == null) {
				throw new IllegalStateException("resolution shouldn't be null");
			}

			//update the environment
			if (identifier.equalsIgnoreCase(PositionCalculator.SAFE_ZONE_X) || identifier.equalsIgnoreCase(PositionCalculator.SAFE_ZONE_X_ABS)) {
				return new Value.NumVal(resolution.getSafeZoneX());
			}
			if (identifier.equalsIgnoreCase(PositionCalculator.SAFE_ZONE_Y)) {
				return new Value.NumVal(resolution.getSafeZoneY());
			}
			if (identifier.equalsIgnoreCase(PositionCalculator.SAFE_ZONE_W) || identifier.equalsIgnoreCase(PositionCalculator.SAFE_ZONE_W_ABS)) {
				return new Value.NumVal(resolution.getSafeZoneW());
			}
			if (identifier.equalsIgnoreCase(PositionCalculator.SAFE_ZONE_H)) {
				return new Value.NumVal(resolution.getSafeZoneH());
			}

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

	/**
	 Set the current {@link Project} instance.

	 @param project instance to use
	 */
	public void setCurrentProject(@NotNull Project project) {
		this.currentProject = project;
	}
}
