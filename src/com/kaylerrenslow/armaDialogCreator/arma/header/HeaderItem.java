package com.kaylerrenslow.armaDialogCreator.arma.header;

import org.jetbrains.annotations.NotNull;

/**
 @author Kayler
 @since 04/29/2017 */
public interface HeaderItem {
	/**
	 Used for printing the item in a String form (should look like part of a .h file)
	 <b>This method is primarily for debugging/testing.</b>
	 */
	@NotNull String getAsString();
}
