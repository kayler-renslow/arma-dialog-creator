package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 @author Kayler
 @since 03/21/2017 */
public interface PreprocessCallback {
	void fileProcessed(@NotNull File file, @Nullable File includedFrom, @NotNull StringBuilder textContent);
}
