package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 @author kayler
 @since 12/15/17 */
public class HeaderParseResult {
	@NotNull
	private final HeaderFile file;
	@NotNull
	private final Map<String, String> defineMacros;

	public HeaderParseResult(@NotNull HeaderFile file, @NotNull Map<String, String> defineMacros) {
		this.file = file;
		this.defineMacros = defineMacros;
	}

	/** @return the {@link HeaderFile} that was resulted from the preprocess and parse */
	@NotNull
	public HeaderFile getFile() {
		return file;
	}

	/**
	 Get a Map that contains all #define macros detected by the preprocessor. The key of the map is the name of the #define
	 and the value is the result template text.
	 <p>
	 Example 1: #define VARIABLE myTemplateText(VARIABLE) //"VARIABLE" is key, "myTemplateText(VARIABLE)" is template text
	 <p>
	 Example 2: #define PARAM(A,B) A+B //"PARAM(A,B)" is key, "A+B" is template text

	 @return a map
	 */
	@NotNull
	public Map<String, String> getDefineMacros() {
		return defineMacros;
	}
}
