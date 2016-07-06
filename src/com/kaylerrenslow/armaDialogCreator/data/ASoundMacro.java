package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.util.ASound;
import org.jetbrains.annotations.NotNull;

/**
 Created by Kayler on 07/05/2016.
 */
public class ASoundMacro extends Macro<ASound> {
	/** Creates a macro for a sound */
	public ASoundMacro(@NotNull String key, @NotNull ASound value) {
		super(key, value);
	}
}
