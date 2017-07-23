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

		//scrollbar
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Scrollbar\\arrowEmpty_ca.paa", prefix + "scrollbar/arrowEmpty.png");
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Scrollbar\\arrowFull_ca.paa", prefix + "scrollbar/arrowFull.png");
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Scrollbar\\border_ca.paa", prefix + "scrollbar/border.png");
		put("\\A3\\ui_f\\data\\GUI\\Cfg\\Scrollbar\\thumb_ca.paa", prefix + "scrollbar/thumb.png");

		//combo
		put("\\A3\\ui_f\\data\\GUI\\RscCommon\\RscCombo\\arrow_combo_ca.paa", prefix + "combo/arrow_combo.png");
		put("\\A3\\ui_f\\data\\GUI\\RscCommon\\RscCombo\\arrow_combo_active_ca.paa", prefix + "combo/arrow_combo_active.png");

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
		String put = extToInternalMap.put(extPathCaseConvert(externalPath), internalPath);
		if (put != null) {
			throw new IllegalArgumentException("externalPath '" + externalPath + "' is already used");
		}
	}

	/**
	 Convert an Arma 3 pbo path (something like &#92;A3&#92;ui_f&#92;etc) into a JavaFX image stored inside the application.
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
		String internalPath = extToInternalMap.get(extPathCaseConvert(path));
		if (internalPath == null) {
			return null;
		}
		return cache.computeIfAbsent(internalPath, s -> {
			return new Image(getClass().getResourceAsStream(s));
		});
	}

	private String extPathCaseConvert(String p) {
		int lastBackslashInd = p.lastIndexOf('\\');
		if (lastBackslashInd < 0) {
			return "";
		}
		if (lastBackslashInd == 0) {
			return p;
		}

		//allow the text left of the last \ to be non case-sensitive, however, any text afterwards is case sensitive for .equals()
		String left = p.substring(0, lastBackslashInd);
		return left.toLowerCase() + p.substring(lastBackslashInd);
	}

}
