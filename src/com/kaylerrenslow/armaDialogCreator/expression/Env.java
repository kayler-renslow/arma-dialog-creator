package com.kaylerrenslow.armaDialogCreator.expression;

import org.jetbrains.annotations.Nullable;

/**
 Created by Kayler on 07/14/2016.
 */
public abstract class Env {
	/** Returns the value for the given identifier. Can be null. */
	@Nullable
	public abstract Value getValue(String identifier);
}
