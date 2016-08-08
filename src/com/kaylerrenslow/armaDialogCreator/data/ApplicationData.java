/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaUIScale;
import com.kaylerrenslow.armaDialogCreator.arma.util.PositionCalculator;
import com.kaylerrenslow.armaDialogCreator.control.Macro;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.expression.SimpleEnv;
import com.kaylerrenslow.armaDialogCreator.expression.Value;
import com.kaylerrenslow.armaDialogCreator.gui.canvas.api.ScreenDimension;
import com.kaylerrenslow.armaDialogCreator.main.ApplicationLoadListener;
import com.kaylerrenslow.armaDialogCreator.main.ApplicationLoader;
import com.kaylerrenslow.armaDialogCreator.util.DataContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 Holds data that aren't specific to the current Project, but rather the entire application itself
 Created on 06/07/2016. */
public class ApplicationData extends DataContext {
	
	private Project currentProject;
	private final Changelog changelog = new Changelog(20);
	private ApplicationData self = this;
	
	public ApplicationData(@NotNull ApplicationLoader.ApplicationLoadRequest request) {
		request.addOnComplete(new ApplicationLoadListener() {
			@Override
			public void loaded(ApplicationLoader.ApplicationLoadConfig config) {
				setCurrentProject(config.getNewProject());
			}
		});
		put(DataKeys.ARMA_RESOLUTION, new ArmaResolution(ScreenDimension.D960, ArmaUIScale.DEFAULT));
		put(DataKeys.ENV, globalEnv);
	}
	
	private final SimpleEnv globalEnv = new SimpleEnv() {
		@Override
		public @Nullable Value getValue(String identifier) {
			ArmaResolution resolution = DataKeys.ARMA_RESOLUTION.get(self);
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
	
	private void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}
}
