package com.kaylerrenslow.armaDialogCreator.gui.fx;

import com.kaylerrenslow.armaDialogCreator.main.ExceptionHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 Created by Kayler on 05/31/2016.
 */
public class FXUtil {
	private static final FXUtil INSTANCE = new FXUtil();

	private FXUtil() {
	}

	/**
	 Loads a .fxml file from the buildpath and returns the loader that contains the loaded root element and the controller class

	 @param url String url that points to .fxml
	 @return fxml loader instance, or null if an exception occurred
	 */
	public static FXMLLoader loadFxml(@NotNull String url) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.load(INSTANCE.getClass().getResource(url).openStream());
			return loader;
		} catch (IOException e) {
			ExceptionHandler.error(e);
		}
		return null;
	}

	/**
	 As of Java 8, setting the tooltip show delay isn't possible. Until an official implementation, this is one way to configure the tooltip show delay

	 @param tooltip tooltip to change delay of
	 @param delayMillis milliseconds before the tooltip can appear
	 */
	public static void hackTooltipStartTiming(Tooltip tooltip, double delayMillis) {
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(delayMillis)));
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
