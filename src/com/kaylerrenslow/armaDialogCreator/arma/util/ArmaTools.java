/*
 * Copyright (c) 2016 Kayler Renslow
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. in no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the software.
 */

package com.kaylerrenslow.armaDialogCreator.arma.util;

import javafx.concurrent.Task;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 The way of connecting to <a href="http://store.steampowered.com/app/233800/">Arma 3 tools</a> installation and using it's utilities
 @author Kayler
 @since 07/16/2016. */
public class ArmaTools {

	private static final String IMAGE_TO_PAA_EXE = "\\ImageToPAA\\ImageToPAA.exe";

	/** Test if the given File is a valid path to Arma 3 Tools. In order to be valid, the path must be something like 'STEAM_INSTALLATION_ROOT\steamapps\common\Arma 3 Tools\' */
	public static boolean isValidA3ToolsDirectory(@NotNull File file) {
		if (!file.exists()) {
			return false;
		}
		return new File(file.getPath() + IMAGE_TO_PAA_EXE).exists();
	}

	/**
	 Runs text in the command line. This method will freeze the current Thread for up to timeout milliseconds.

	 @param commandLineText text to run
	 @param timeout how many milliseconds the operation is allowed to run
	 @return true if the operation succeeded, false if it didn't
	 */
	private static boolean execCommandLineOperation(String commandLineText, long timeout) {
		ProcessBuilder pb = new ProcessBuilder(commandLineText);
		Process p;
		try {
			p = pb.start();
		} catch (IOException e) {
			return false;
		}
		try {
			if(!p.waitFor(timeout, TimeUnit.MILLISECONDS)){
				p.destroyForcibly();
				return false;
			}
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/**
	 Converts a *.paa image file into a new image type (must be specified in pngSafeTo file name. e.g. "something.paa" -> ("something.paa.png" or "something.jpg")).
	 This method can also convert a .jpg or .png into a .paa (just pass the .jpg as toConvert and make saveTo's path end with .paa).
	 Besides .paa, this method can also convert .tga into another file format mentioned earlier.<br>
	 This conversion is done by invoking Arma 3 Tools's ImageToPAA.exe<br>
	 This method will freeze the current Thread for up to timeout milliseconds, so run this on a {@link Task} if using with JavaFX.

	 @param arma3ToolsDirectory the directory of Arma 3 Tools installation (should be something like "Program Files x32\Steam\steamapps\common\Arma 3 Tools\")
	 @param toConvert the image file to convert (.paa,.png, .jpg). The image file must exist, or an IOException will be thrown
	 @param saveTo the file to save the converted image to. This is <b>overwrite</b> the current data in that file, so be sure that the file is the correct one.
	 @param timeout how many milliseconds the operation is allowed to take before it is suspended.
	 @return true if the operation succeeded. Returns false if the operation failed and for an unknown reason (something bad happened with the conversion).
	 @throws IOException when any of the given file parameters are invalid
	 */
	public static boolean imageToPAA(@NotNull File arma3ToolsDirectory, @NotNull File toConvert, @NotNull File saveTo, long timeout) throws IOException {
		if (!arma3ToolsDirectory.exists()) {
			throw new FileNotFoundException("Arma 3 Tools Directory doesn't exist.");
		}
		if (!isValidA3ToolsDirectory(arma3ToolsDirectory)) {
			throw new FileNotFoundException("Path to Arma 3 Tools directory is incorrect.");
		}
		if (!toConvert.exists()) {
			throw new FileNotFoundException("The file to be converted doesn't exist.");
		}

		String commandLine = String.format("\"%s\\\\%s\" \"%s\" \"%s\"", arma3ToolsDirectory.getPath(), IMAGE_TO_PAA_EXE, toConvert.getPath(), saveTo.getPath());
		return execCommandLineOperation(commandLine, timeout);
	}

}
