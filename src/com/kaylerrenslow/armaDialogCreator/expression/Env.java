package com.kaylerrenslow.armaDialogCreator.expression;

/**
 Created by Kayler on 07/14/2016.
 */
public abstract class Env {
	/** Returns the value for the given identifier */
	public abstract Value getValue(String identifier);
}
