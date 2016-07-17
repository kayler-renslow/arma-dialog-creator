package com.kaylerrenslow.armaDialogCreator.arma.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 @author Kayler
 The way of connecting to <a href="http://store.steampowered.com/app/233800/">Arma 3 tools</a> installation and using it's utilities
 Created on 07/16/2016. */
public class ArmaTools {
	
	private static final String IMAGE_TO_PAA_EXE = "\\ImageToPAA\\ImageToPAA.exe";
	
	/**
	 Runs text in the command line. This method will freeze the current Thread for up to timeout milliseconds.
	 
	 @param commandLineText text to run
	 @param timeout how many milliseconds the operation is allowed to run
	 @return true if the operation succeeded, false if it didn't
	 */
	private static boolean execCommandLineOperation(String commandLineText, long timeout) throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		Process currentProcess = rt.exec(commandLineText);
		return currentProcess.waitFor(timeout, TimeUnit.MILLISECONDS);
	}
	
	/**
	 Converts a *.paa image file into a new image type (must be specified in pngSafeTo file name. e.g. "something.paa" -> ("something.paa.png" or "something.jpg")).
	 This is done by invoking Arma 3 Tools's ImageToPAA.exe<br>
	 This method will freeze the current Thread for up to timeout milliseconds.
	 
	 @param arma3ToolsDirectory the directory of Arma 3 Tools installation (should be something like "Program Files x32\Steam\steamapps\common\Arma 3 Tools\")
	 @param paaFile the .paa image file. The image file must exist, or an IOException will be thrown
	 @param pngSaveTo the file to save the .png to. This is <b>overwrite</b> the current data in that file, so be sure that the file is the correct one.
	 @param timeout how many milliseconds the operation is allowed to take before it is suspended.
	 @return true if the operation succeeded. Returns false if the operation failed and for an unknown reason (something bad happened with the conversion).
	 @throws IOException when any of the given file parameters are invalid
	 */
	public static boolean convertPaaToImage(@NotNull File arma3ToolsDirectory, @NotNull File paaFile, @NotNull File pngSaveTo, long timeout) throws IOException {
		if (!arma3ToolsDirectory.exists()) {
			throw new FileNotFoundException("Arma 3 Tools Directory doesn't exist.");
		}
		if (!arma3ToolsDirectory.isDirectory()) {
			throw new IOException("Path to Arma 3 Tools directory isn't a directory.");
		}
		if (!paaFile.exists()) {
			throw new FileNotFoundException("The given .paa file doesn't exist.");
		}
		
		boolean good = false;
		try {
			String commandLine = String.format("\"%s\\\\%s\" \"%s\" \"%s\"", arma3ToolsDirectory.getPath(), IMAGE_TO_PAA_EXE, paaFile.getPath(), pngSaveTo.getPath());
			good = execCommandLineOperation(commandLine, timeout);
		} catch (InterruptedException ex) {
			ex.printStackTrace(System.out);
		}
		
		return good;
	}
	
}
