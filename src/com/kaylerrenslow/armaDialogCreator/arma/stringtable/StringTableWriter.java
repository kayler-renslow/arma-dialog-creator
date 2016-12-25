package com.kaylerrenslow.armaDialogCreator.arma.stringtable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 @author Kayler
 @since 12/25/2016 */
public interface StringTableWriter {
	void writeTable(@NotNull StringTable table) throws IOException;
}
