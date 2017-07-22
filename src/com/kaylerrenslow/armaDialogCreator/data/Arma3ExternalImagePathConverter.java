package com.kaylerrenslow.armaDialogCreator.data;

import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 @author Kayler
 @since 07/22/2017 */
public class Arma3ExternalImagePathConverter {
	private static final Arma3ExternalImagePathConverter instance = new Arma3ExternalImagePathConverter();

	@NotNull
	public synchronized static Arma3ExternalImagePathConverter getInstance() {
		return instance;
	}

	private final HashMap<String, String> extToInternalMap = new HashMap<>();
	private final HashMap<String, Image> cache = new HashMap<>();

	private Arma3ExternalImagePathConverter() {
		final String prefix = "/com/kaylerrenslow/armaDialogCreator/arma/icons/";

		//slider
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Slider\\arrowEmpty_ca.paa", prefix + "slider/arrowEmpty.png");
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Slider\\arrowFull_ca.paa", prefix + "slider/arrowFull.png");
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Slider\\border_ca.paa", prefix + "slider/border.png");
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Slider\\thumb_ca.paa", prefix + "slider/thumb.png");

	}

	private void put(String externalPath, String internalPath) {
		InputStream stream = getClass().getResourceAsStream(internalPath);
		if (stream == null) {
			throw new IllegalArgumentException("internalPath '" + internalPath + "' doesn't map to an existing resource");
		}
		try {
			stream.close();
		} catch (IOException ignore) {
		}
		String put = extToInternalMap.put(externalPath, internalPath);
		if (put != null) {
			throw new IllegalArgumentException("externalPath '" + externalPath + "' is already used");
		}
	}

	/**
	 Convert an Arma 3 pbo path (something like \A3\ui_f\etc) into a JavaFX image stored inside the application.
	 <p>
	 The path may be valid, but this method can still return null for a valid path if there is no image for the valid
	 path in the application.
	 <p>
	 All images that are returned are cached to prevent multiple instances of the same image being created.

	 @param path the Arma 3 pbo path to get an image for
	 @return the matched {@link Image}, or null if the path didn't map do a stored image
	 */
	@Nullable
	public Image getImage(@NotNull String path) {
		String internalPath = extToInternalMap.get(path);
		if (internalPath == null) {
			return null;
		}
		return cache.computeIfAbsent(internalPath, s -> {
			return new Image(getClass().getResourceAsStream(s));
		});
	}

}
