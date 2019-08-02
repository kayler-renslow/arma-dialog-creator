package com.armadialogcreator.canvas;

import com.armadialogcreator.util.ScreenDimension;
import com.armadialogcreator.util.UpdateListenerGroup;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 06/18/2016.
 */
public interface Resolution {

	/** Get the screen width */
	int getScreenWidth();

	/** Get the screen height */
	int getScreenHeight();

	/** Set the screen dimension (must be 16:9 ratio) */
	void setScreenDimension(@NotNull ScreenDimension dimension);

	/** Set the UIScale */
	void setUIScale(@NotNull UIScale uiScale);

	@NotNull
	UIScale getUIScale();

	/** Sets all properties of this resolution equal to r */
	void setTo(@NotNull Resolution r);

	@NotNull
	UpdateListenerGroup<Resolution> getUpdateGroup();

	/** @return the screen's aspect ratio */
	default double getAspectRatio() {
		return getScreenWidth() * 1d / getScreenHeight();
	}
}
