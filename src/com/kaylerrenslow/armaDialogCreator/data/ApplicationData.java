package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.SimpleEnv;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.main.ArmaDialogCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Calendar;

/**
 @author Kayler
 Holds data that aren't specific to the current Project, but rather the entire application itself
 Created on 06/07/2016. */
public class ApplicationData {
	
	private Project currentProject;
	
	public ApplicationData(@NotNull File appSaveDataDirectory) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int hour = Calendar.getInstance().get(Calendar.HOUR);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		String date = String.format("%d-%d-%d %d-%d", year, month, day, hour, minute);
		this.currentProject = new Project("untitled " + date, appSaveDataDirectory);
	}
	
	public ApplicationData(@NotNull File appSaveDataDirectory, @NotNull String projectName) {
		this.currentProject = new Project(projectName, appSaveDataDirectory);
	}
	
	private final SimpleEnv globalEnv = new SimpleEnv() {
		@Override
		public @Nullable Value getValue(String identifier) {
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
	
	public void setCurrentProject(@NotNull Project currentProject) {
		this.currentProject = currentProject;
	}
	
	/**
	 Get the {@link Env} instance used for converting identifiers that match any Macro instances key retrieved from {@link Macro#getKey()}. The identifiers can also be
	 {@link PositionCalculator#SAFE_ZONE_X},
	 {@link PositionCalculator#SAFE_ZONE_Y},
	 {@link PositionCalculator#SAFE_ZONE_W},
	 {@link PositionCalculator#SAFE_ZONE_H},
	 */
	public Env getGlobalExpressionEnvironment() {
		//update the environment
		globalEnv.put(PositionCalculator.SAFE_ZONE_X, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneX()));
		globalEnv.put(PositionCalculator.SAFE_ZONE_Y, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneY()));
		globalEnv.put(PositionCalculator.SAFE_ZONE_W, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneW()));
		globalEnv.put(PositionCalculator.SAFE_ZONE_H, new Value.NumVal(ArmaDialogCreator.getCanvasView().getCurrentResolution().getSafeZoneH()));
		
		return globalEnv;
	}
}
