package com.armadialogcreator.gui.main;

import org.jetbrains.annotations.NotNull;

/**
 Interface for passing around a reference of {@link ADCWindow}

 @author Kayler
 @since 07/21/2017 */
public interface ADCMainWindow {

	@NotNull
	CanvasView getCanvasView();

	void setToFullScreen(boolean fullScreen);

	boolean isShowing();

	void hide();

	void initialize();

	void show();

	void runWhenReady(@NotNull Runnable r);
}
