package com.armadialogcreator.arma.header;

import com.armadialogcreator.util.DataContext;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 @author Kayler
 @since 03/19/2017
 @see HeaderParser
 @see Preprocessor*/
class HeaderParserContext extends DataContext {
	private final List<HeaderMacro> macroList = new ArrayList<>();
	private File tempDirectory;

	/**
	 @param tempDirectory a directory used for making temporary files
	 */
	public HeaderParserContext(@NotNull File tempDirectory) {
		if (!tempDirectory.isDirectory()) {
			throw new IllegalArgumentException("tempDirectory is not a directory");
		}
		this.tempDirectory = tempDirectory;
	}

	@NotNull
	public List<HeaderMacro> getMacros() {
		return macroList;
	}

	/** @return a temporary directory used for storing things */
	@NotNull
	public File getTempDirectory() {
		return tempDirectory;
	}
}
