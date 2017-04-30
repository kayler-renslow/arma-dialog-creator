package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author Kayler
 @since 03/21/2017 */
interface PreprocessCallback {
	void fileProcessed(@NotNull File file, @NotNull Preprocessor.PreprocessorInputStream fileContentStream) throws HeaderParseException;
}
