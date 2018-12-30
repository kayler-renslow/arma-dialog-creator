package com.armadialogcreator.data;

import com.armadialogcreator.arma.util.ArmaResolution;
import com.armadialogcreator.expression.Env;
import com.armadialogcreator.util.Key;

/**
 Created by Kayler on 07/30/2016.
 */
public final class DataKeys {
	public static final Key<Env> ENV = new Key<>("expression.env", null);
	public static final Key<ArmaResolution> ARMA_RESOLUTION = new Key<>("resolution.armaResolution", null);
	public static final Key<String> CONTROL_PROPERTY_DOCUMENTATION_PATH = new Key<String>("controlClassDocumentationPath");

	private DataKeys(){}
		
}
