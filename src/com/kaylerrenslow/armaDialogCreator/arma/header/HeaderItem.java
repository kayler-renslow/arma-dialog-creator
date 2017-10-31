package com.kaylerrenslow.armaDialogCreator.arma.header;

import com.kaylerrenslow.armaDialogCreator.util.IndentedStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 @author Kayler
 @since 04/29/2017 */
public interface HeaderItem {
	/**
	 Used for printing the item in a String form (should look like part of a .h file)
	 <b>This method is primarily for debugging/testing.</b>

	 @param sb an {@link IndentedStringBuilder} to use, or null if indentation doesn't matter
	 @return the {@link IndentedStringBuilder}'s toString() value, or if it's null,
	 the String will be constructed and returned in different way
	 */
	@NotNull String getAsString(@Nullable IndentedStringBuilder sb);

}
