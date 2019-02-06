package com.armadialogcreator.core.stringtable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 Used to construct a new {@link StringTable} instance

 @author Kayler
 @since 12/12/2016 */
public interface StringTableParser {
	/**
	 Get a new {@link StringTable} instance

	 @return the new instance
	 @throws IOException when the table couldn't be parsed
	 */
	@NotNull
	StringTable createStringTableInstance() throws IOException;
}
