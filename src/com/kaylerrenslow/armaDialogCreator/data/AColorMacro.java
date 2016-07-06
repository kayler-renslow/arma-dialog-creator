package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.util.AColor;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/05/2016.
 */
public class AColorMacro extends Macro<AColor> {
	/** Creates a macro for a color */
	public AColorMacro(@NotNull String key, @NotNull AColor value) {
		super(key, value);
	}
}
