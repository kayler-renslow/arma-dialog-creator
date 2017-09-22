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
	private static final String BANK_REV = "\\BankRev\\BankRev.exe";

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
			throw new FileNotFoundException("Arma Tools: Arma 3 Tools Directory doesn't exist. Dir=" +
					arma3ToolsDirectory.getPath());
		}
		if (!isValidA3ToolsDirectory(arma3ToolsDirectory)) {
			throw new FileNotFoundException("Arma Tools: Path to Arma 3 Tools directory is incorrect. Dir=" +
					arma3ToolsDirectory.getPath());
		}
		if (!toConvert.exists()) {
			throw new FileNotFoundException("Arma Tools: The paa image to be converted doesn't exist. File=" +
					toConvert.getPath());
		}

		String commandLine = String.format("\"%s\\\\%s\" \"%s\" \"%s\"", arma3ToolsDirectory.getPath(), IMAGE_TO_PAA_EXE, toConvert.getPath(), saveTo.getPath());
		return execCommandLineOperation(commandLine, timeout);
	}

	/**
	 Utilizes "BankRev.exe", which extracts PBO's. It should be noted that this will extract the entire PBO, which could
	 take a lot of disk memory and time.<br>
	 This method will freeze the current Thread for up to timeout milliseconds, so run this on a {@link Task} if using with JavaFX.

	 @param arma3ToolsDirectory the directory of Arma 3 Tools installation (should be something like "Program Files x32\Steam\steamapps\common\Arma 3 Tools\")
	 @param pboToExtract the .pbo file to extract
	 @param saveToDirectory the directory to save the extracted contents to
	 @param timeout how many milliseconds the operation is allowed to take before it is suspended.
	 @return true if the operation succeeded. Returns false if the operation failed and for an unknown reason (something bad happened with the conversion).
	 @throws IOException when any of the given file parameters are invalid
	 */
	public static boolean extractPBO(@NotNull File arma3ToolsDirectory, @NotNull File pboToExtract, @NotNull File saveToDirectory, long timeout) throws IOException {
		if (!arma3ToolsDirectory.exists()) {
			throw new FileNotFoundException("Arma Tools: Arma 3 Tools Directory doesn't exist. Dir=" +
					arma3ToolsDirectory.getPath());
		}
		if (!isValidA3ToolsDirectory(arma3ToolsDirectory)) {
			throw new FileNotFoundException("Arma Tools: Path to Arma 3 Tools directory is incorrect. Dir=" +
					arma3ToolsDirectory.getPath());
		}
		if (!pboToExtract.exists()) {
			throw new FileNotFoundException("Arma Tools: The pbo file doesn't exist. File=" +
					pboToExtract.getPath());
		}

		String commandLine = String.format(
				"\"%s\\\\%s\" -f \"%s\" \"%s\"",
				arma3ToolsDirectory.getPath(),
				BANK_REV,
				saveToDirectory.getPath(),
				pboToExtract.getPath()
		);
		return execCommandLineOperation(commandLine, timeout);
	}

}
