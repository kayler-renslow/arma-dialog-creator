package com.kaylerrenslow.armaDialogCreator.data;

import com.kaylerrenslow.armaDialogCreator.arma.util.ArmaResolution;
import com.kaylerrenslow.armaDialogCreator.expression.Env;
import com.kaylerrenslow.armaDialogCreator.util.Key;

/**
 Created by Kayler on 07/30/2016.
 */
public final class DataKeys {
	public static final Key<Env> ENV = new Key<>("expression.env", null);
	public static final Key<ArmaResolution> ARMA_RESOLUTION = new Key<>("resolution.armaResolution", null);
	
	private DataKeys(){}
		
}
