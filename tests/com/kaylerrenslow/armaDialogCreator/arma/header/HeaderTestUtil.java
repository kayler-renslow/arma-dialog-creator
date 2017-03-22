package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 @author Kayler
 @since 03/22/2017 */
public class HeaderTestUtil {
	@NotNull
	public static File getFile(@NotNull String fileName) {
		return new File("tests/com/kaylerrenslow/armaDialogCreator/arma/header/" + fileName);
	}
}
